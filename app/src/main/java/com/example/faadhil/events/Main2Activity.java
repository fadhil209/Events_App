package com.example.faadhil.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static boolean userboolean ;
    public static TabLayout tabLayout;
    static ViewPager viewPager;
    Fragment[] fragment = new Fragment[4];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mAuth = FirebaseAuth.getInstance();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);

        tabLayout.addTab(tabLayout.newTab().setText("Events"));
        tabLayout.addTab(tabLayout.newTab().setText("Deals"));
        tabLayout.addTab(tabLayout.newTab().setText("Add Event"));
        viewPager.setSaveFromParentEnabled(false);
        fragment[0] = new EventsFragment();
        fragment[1] = new DealsFragment();
        fragment[2] = new LogInFragment();
        fragment[3] = new AddEventsFragment();
        final PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), fragment);

        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    userboolean = true;
                    final PageAdapter pageAdapterNew = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), fragment);
                    viewPager.setAdapter(pageAdapterNew);

                }
                else {
                    userboolean = false;
                    final PageAdapter pageAdapterNew = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), fragment);
                    viewPager.setAdapter(pageAdapterNew);
                }
            }
        };



    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        viewPager.postDelayed(new Runnable() {

            @Override
            public void run() {
                viewPager.setCurrentItem(2);
            }
        }, 50);


        Log.d("Log", "onActivityResult: in Mainactivity" + viewPager.getCurrentItem());
    }
}
