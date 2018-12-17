package com.example.faadhil.events;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseAuth.getInstance().signOut();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent nextIntent = new Intent(Splash.this, Main2Activity.class);
                    startActivity(nextIntent);
                    finish();
                }
            }, 4000);
//        }





    }
}
