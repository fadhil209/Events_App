package com.example.faadhil.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class signUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText username;
    EditText email;
    EditText password;

    Button signUp;
    TextView signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.emailSignUp);
        password = (EditText) findViewById(R.id.passwordSignUp);

        signUp = (Button) findViewById(R.id.SignUp);
        signIn = (TextView) findViewById(R.id.SignInSignUp);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signUp.this, LogIn.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText() != null && email.getText() != null && password != null){
                    SignUp();
                }
                else
                    Toast.makeText(signUp.this, "fill all the necessary values", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void SignUp(){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(signUp.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    mAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder()
                            .setDisplayName(username.getText().toString()).build());
                    Toast.makeText(signUp.this, mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(signUp.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();

            }
        });

    }


}
