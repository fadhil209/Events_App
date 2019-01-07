package com.example.faadhil.events;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealsFragment extends Fragment {

    ListView dealsList;
    List<Deals> deals; // for all deals from firebase
    List<Deals> deal;  // for all deals searched that will be passed to the view adapter to inflate the list view
    GridView dealsgridview;
    public static Deals publicdeal;


    DatabaseReference databaseReferenceDeals = FirebaseDatabase.getInstance().getReference("deals");



    public DealsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deals, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deals = new ArrayList<>();
        deal = new ArrayList<>();
        dealsgridview = (GridView) getView().findViewById(R.id.GridViewDeals);


        databaseReferenceDeals.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deals.clear();
                for (DataSnapshot dealsdata : dataSnapshot.getChildren()) {
                    deals.add(dealsdata.getValue(Deals.class));
                }

                Collections.reverse(deals);

                for(Deals deals1 : deals){
                    if (!deal.contains(deals1)){
                        deal.add(deals1);
                        DealsViewAdapter viewAdapter = new DealsViewAdapter(getActivity(), deal);
                        dealsgridview.setAdapter(viewAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dealsgridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                publicdeal = deals.get(i);

                Intent intent = new Intent(getActivity(), showDeal.class);
                startActivity(intent);
            }
        });


    }

}
