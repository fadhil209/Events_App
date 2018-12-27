package com.example.faadhil.events;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.faadhil.events.R.id.spinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    Spinner categoriesSpinner;
    public static ListView listView;
    List<Events> events;
    List<Events> event;
    EditText searchedit;
    public static Events publicEvent;
    Date c;


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");


    public EventsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_view, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        c = Calendar.getInstance().getTime();
        SimpleDateFormat sp = new SimpleDateFormat("dd/mm/yy");
        String formattedc = sp.format(c);
        categoriesSpinner = (Spinner) getView().findViewById(spinner);
        listView = (ListView) getView().findViewById(R.id.eventsListView);
        searchedit = (EditText) getView().findViewById(R.id.searchbox);



        events = new ArrayList<>();
        event = new ArrayList<>();
        searchedit.setFocusableInTouchMode(false);


        searchedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchedit.setFocusable(true);
                searchedit.setFocusableInTouchMode(true);
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events.clear();

                String category = "All";

                for (DataSnapshot eventsdata : dataSnapshot.getChildren()) {
                    event.add(eventsdata.getValue(Events.class));

                }
                Collections.reverse(event);
                for (Events events2 : event) {
                    if (category == "All") {
                        if (!events.contains(events2)) {
                            events.add(events2);
                            viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                            listView.setAdapter(viewAdapter);
                            Log.d("LOG", "onDataChange: inside All");
                        }
                    } else if (events2.getCategory() == category) {
                        if (!events.contains(events2)) {
                            events.add(events2);
                            viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                            listView.setAdapter(viewAdapter);
                        }
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
                for (Events events2 : event) {
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

        searchedit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    closeKeyboard();
                    performSearch();
                    return true;
                }
                return false;
            }
        });
        searchedit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                performSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(), "inside listView on Click listener", Toast.LENGTH_SHORT).show();
                publicEvent = events.get(i);
                Intent intent = new Intent(getActivity(), showEvent.class);
                startActivity(intent);
            }
        });

    }


    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public void performSearch() {
        String search = searchedit.getText().toString();
        events.clear();
        listView.setAdapter(null);
        for (Events events2 : event) {
            if (search.equals("")) {
                if (!events.contains(events2)) {
                    events.add(events2);
                    viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                    listView.setAdapter(viewAdapter);
                    Log.d("LOG", "onItemSelected: first if");
                }
            } else if (events2.getCategory().toLowerCase().contains(search.toLowerCase())) {
                if (!events.contains(events2)) {
                    events.add(events2);
                    viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                    listView.setAdapter(viewAdapter);
                }
            }else if (events2.getEventName().toLowerCase().contains(search.toLowerCase())) {
                if (!events.contains(events2)) {
                    events.add(events2);
                    viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                    listView.setAdapter(viewAdapter);
                }
            }else if (events2.getLocation().toLowerCase().contains(search.toLowerCase())) {
                if (!events.contains(events2)) {
                    events.add(events2);
                    viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                    listView.setAdapter(viewAdapter);
                }
            }
        }
    }
}




