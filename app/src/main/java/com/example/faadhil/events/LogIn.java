package com.example.faadhil.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText email;
    EditText password;
    Button Signin;
    TextView Signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);



        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.emailSignIn);
        password = (EditText) findViewById(R.id.PasswordSignIn);
        Signin = (Button) findViewById(R.id.SignIn);
        Signup = (TextView) findViewById(R.id.SignUpLogin);


        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                    SignIn(email.getText().toString(), password.getText().toString());
                }
                else
                {
                    Toast.makeText(LogIn.this, "Fill username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, signUp.class);
                startActivity(intent);
                finish();
            }
        });

    }



    public void SignIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            finish();
                            Toast.makeText(LogIn.this, mAuth.getCurrentUser().getDisplayName()+" Sign in Successful", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }


}
