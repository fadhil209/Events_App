package com.example.faadhil.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEvents extends AppCompatActivity {

    FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

    TextView username;
    ImageView imageView1;
    Button uploadImage;
    EditText eventName;
    EditText eventDescription;
    EditText eventLocation;
    EditText eventDate;
    EditText eventTime;
    EditText optionalDeal;
    Spinner addEventSpinner;
    Uri uri;
    Button uploadEvent;


    String eventId;
    String name;
    String description;
    String location;
    String date;
    String time;
    String deal;
    String category;

    final Calendar myCalendar= Calendar.getInstance();

    private DatabaseReference databaseReferenceEvents;
    private StorageReference storageReferenceimages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);



        databaseReferenceEvents = FirebaseDatabase.getInstance().getReference("events");
        storageReferenceimages = FirebaseStorage.getInstance().getReference("events");



        username = (TextView) findViewById(R.id.signedUsername);
        imageView1 = (ImageView) findViewById(R.id.imageViewAdd);
        uploadImage = (Button) findViewById(R.id.uploadImage);
        eventName = (EditText) findViewById(R.id.EventNameText);
        eventDescription = (EditText) findViewById(R.id.EventDescriptionText);
        eventLocation = (EditText) findViewById(R.id.EventLocationText);
        eventDate = (EditText) findViewById(R.id.EventDateText);
        eventTime = (EditText) findViewById(R.id.EventTimeText);
        optionalDeal = (EditText) findViewById(R.id.EventDealsText);
        uploadEvent = (Button) findViewById(R.id.uploadButton);
        addEventSpinner = (Spinner) findViewById(R.id.addEventSpinner);

        if (!(uri == null)){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageView1.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        username.setText(mAuth.getDisplayName());

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("log", "Upload image onClick: " + MainActivity.viewPager.getFocusedChild() );
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                AddEvents.this.startActivityForResult(intent.createChooser(intent, "Select Picture"), 1);
            }
        });


        uploadEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri != null)
                    uploadevent();
                else
                    Toast.makeText(AddEvents.this, "Upload an Image", Toast.LENGTH_SHORT).show();
            }
        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        eventDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddEvents.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        eventTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEvents.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        eventTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });



    }
    private void updateLabel() {
        String myFormat = "E, dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        eventDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("log inside fragment", data.getData().toString());


        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null ){
            Log.d("log inside fragment if ", data.getData().toString());
            uri = data.getData();
            Toast.makeText(AddEvents.this, uri.toString()+ " ", Toast.LENGTH_SHORT).show();


            try {
                Log.d("log inside try", data.getData().toString());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(AddEvents.this.getContentResolver(),uri);
                if (!(bitmap == null)){
                    Log.d("log bitmap is not null", bitmap.toString());
                }
                imageView1.setImageBitmap(bitmap);


            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }



    public void uploadevent(){
        name = eventName.getText().toString().trim();
        description = eventDescription.getText().toString().trim();
        location = eventLocation.getText().toString().trim();
        date = eventDate.getText().toString().trim();
        time = eventTime.getText().toString().trim();
        deal = optionalDeal.getText().toString().trim();
        category = addEventSpinner.getSelectedItem().toString();

        if(!(TextUtils.isEmpty(name)&&TextUtils.isEmpty(description)&&TextUtils.isEmpty(location)&&
                TextUtils.isEmpty(date)&&TextUtils.isEmpty(time)&&uri == null)){
            eventId = databaseReferenceEvents.push().getKey();

            StorageReference fileName = storageReferenceimages.child(uri.getLastPathSegment());
            fileName.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadUri = taskSnapshot.getDownloadUrl().toString();
                    Toast.makeText(AddEvents.this, "Image Uploaded", Toast.LENGTH_SHORT).show();

                    Events event = new Events(category,date,deal,description,eventId,name,location, time, downloadUri, mAuth.getDisplayName());


                    databaseReferenceEvents.child(eventId).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddEvents.this, "event uploaded", Toast.LENGTH_SHORT).show();
                            imageView1.setImageBitmap(null);
                            eventName.setText("");
                            eventDescription.setText("");
                            eventLocation.setText("");
                            eventDate.setText("");
                            eventTime.setText("");
                            optionalDeal.setText("");

                        }
                    });




                }
            });


        }
        else
            Toast.makeText(AddEvents.this, "Fill all the edit text", Toast.LENGTH_SHORT).show();

    }
}
