package com.example.faadhil.events;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    Spinner categoriesSpinner;
    ListView listView;
    List<Events> events;



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

        categoriesSpinner = (Spinner) getView().findViewById(R.id.spinner);
        listView = (ListView) getView().findViewById(R.id.eventsListView);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                TODO
//                events.clear();

                for (DataSnapshot eventsdata: dataSnapshot.getChildren()){
                    Events event = eventsdata.getValue(Events.class);

                    if (categoriesSpinner.getSelectedItem().toString() == "All") {
                        events.add(event);
                        viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                        listView.setAdapter(viewAdapter);
                    }
                    else if (event.getCategory() == categoriesSpinner.getSelectedItem().toString()){
                        events.add(event);
                        viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                        listView.setAdapter(viewAdapter);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        TODO:
//        set on spinner change listener or item selected listener




    }
}
