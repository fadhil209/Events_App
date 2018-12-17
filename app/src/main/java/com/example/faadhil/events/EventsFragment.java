package com.example.faadhil.events;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.faadhil.events.R.id.spinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    Spinner categoriesSpinner;
    ListView listView;
    List<Events> events;
    List<Events> event;



    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("eventsImages");


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_view,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoriesSpinner = (Spinner) getView().findViewById(spinner);
        listView = (ListView) getView().findViewById(R.id.eventsListView);

        events = new ArrayList<>();
        event = new ArrayList<>();



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events.clear();

                String category = "All";

                for (DataSnapshot eventsdata: dataSnapshot.getChildren()){
                    event.add(eventsdata.getValue(Events.class));

                }
                Collections.reverse(event);
                for (Events events2: event) {
                    if (category == "All") {
                        events.add(events2);
                        viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                        listView.setAdapter(viewAdapter);
                        Log.d("LOG", "onDataChange: inside All");
                    } else if (events2.getCategory() == category) {
                        events.add(events2);
                        viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                        listView.setAdapter(viewAdapter);
                    }
                }
                Log.d("LOG", "Outside IF " + categoriesSpinner.getSelectedItem().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String categories = adapterView.getItemAtPosition(i).toString();
                events.clear();
                listView.setAdapter(null);
                for (Events events2: event) {
                    if (categories.equals("All")) {
                        events.add(events2);
                        viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                        listView.setAdapter(viewAdapter);
                        Log.d("LOG", "onItemSelected: first if");
                    } else if (events2.getCategory().equals(categories)) {
                        events.add(events2);
                        viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                        listView.setAdapter(viewAdapter);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





    }
}
