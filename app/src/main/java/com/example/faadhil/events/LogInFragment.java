package com.example.faadhil.events;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends Fragment {

    Button LogInFragment;
    Button SignUpFragment;


    public LogInFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sign_in_fragment,container,false);
        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        LogInFragment = (Button) getView().findViewById(R.id.SignInFragment);
        SignUpFragment = (Button) getView().findViewById(R.id.SignUpFragment);


        LogInFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LogIn.class);
                startActivity(intent);
            }
        });

        SignUpFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), signUp.class);
                startActivity(intent);
            }
        });

    }
}
