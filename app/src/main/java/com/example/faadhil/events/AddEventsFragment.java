package com.example.faadhil.events;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventsFragment extends Fragment {

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


    public AddEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_add_events,container,false);
//        imageView1 = (ImageView) rootView.findViewById(R.id.imageView1);
        setRetainInstance(true);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReferenceEvents = FirebaseDatabase.getInstance().getReference("events");
        storageReferenceimages = FirebaseStorage.getInstance().getReference("events");



        username = (TextView) getView().findViewById(R.id.signedUsername);
        imageView1 = (ImageView) getView().findViewById(R.id.imageViewAdd);
        uploadImage = (Button) getView().findViewById(R.id.uploadImage);
        eventName = (EditText) getView().findViewById(R.id.EventNameText);
        eventDescription = (EditText) getView().findViewById(R.id.EventDescriptionText);
        eventLocation = (EditText) getView().findViewById(R.id.EventLocationText);
        eventDate = (EditText) getView().findViewById(R.id.EventDateText);
        eventTime = (EditText) getView().findViewById(R.id.EventTimeText);
        optionalDeal = (EditText) getView().findViewById(R.id.EventDealsText);
        uploadEvent = (Button) getView().findViewById(R.id.uploadButton);
        addEventSpinner = (Spinner) getView().findViewById(R.id.addEventSpinner);

        if (!(uri == null)){
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                imageView1.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        username.setText(mAuth.getDisplayName());

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("log", "Upload image onClick: " + Main2Activity.viewPager.getFocusedChild() );
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                AddEventsFragment.this.startActivityForResult(intent.createChooser(intent, "Select Picture"), 1);
            }
        });


        uploadEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadevent();
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

                new DatePickerDialog(getContext(), date, myCalendar
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
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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
        String myFormat = "E dd/MM/yy"; //In which you need put here
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
            Toast.makeText(getActivity(), uri.toString()+ " ", Toast.LENGTH_SHORT).show();


            try {
                Log.d("log inside try", data.getData().toString());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
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
                    Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();

                    Events event = new Events(category,date,deal,description,eventId,name,location, time, downloadUri, mAuth.getDisplayName());


                    databaseReferenceEvents.child(eventId).setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "event uploaded", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Fill all the edit text", Toast.LENGTH_SHORT).show();

    }
}
