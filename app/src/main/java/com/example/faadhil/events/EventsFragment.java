package com.example.faadhil.events;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import java.util.Map;

import static com.example.faadhil.events.R.id.spinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    Spinner categoriesSpinner;
    public static ListView listView;
    List<Events> events;
    public static List<Events> event;
    List<SimilarWord> smWords;
    List<String> listOrder;
    EditText searchedit;
    public static Events publicEvent;
    boolean searchboolean;
    boolean searched;
    Date c;


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("events");
    DatabaseReference databaseReferencesimilarWords = FirebaseDatabase.getInstance().getReference("smwords");


    public EventsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_view, container, false);
        events = new ArrayList<>();
        event = new ArrayList<>();
        smWords = new ArrayList<>();
        listOrder = new ArrayList<>();
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
        searchboolean = false;
        searched = false;




        searchedit.setFocusableInTouchMode(false);


        searchedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchedit.setFocusable(true);
                searchedit.setFocusableInTouchMode(true);
            }
        });


        databaseReferencesimilarWords.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                smWords.clear();

                for(DataSnapshot smWordData : dataSnapshot.getChildren()){
                    smWords.add(smWordData.getValue(SimilarWord.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG", "onViewCreated: for testing");
                events.clear();

                String category = "All";

                for (DataSnapshot eventsdata : dataSnapshot.getChildren()) {
                    event.add(eventsdata.getValue(Events.class));

                }
                Collections.reverse(event);
                if(!searchedit.getText().toString().equals("")){
                    performSearch();
                }else {
                    for (Events events2 : event) {
                        if (category == "All") {
                            if (!events.contains(events2)) {
                                events.add(events2);
                                viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                                listView.setAdapter(viewAdapter);

                            }
                        } else if (events2.getCategory() == category) {
                            if (!events.contains(events2)) {
                                events.add(events2);
                                viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                                listView.setAdapter(viewAdapter);
                            }
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(searchedit.getText().toString().equals("")) {
                    String categories = adapterView.getItemAtPosition(i).toString();
                    Log.d("TAG", "onViewCreated: for testing selected listener");
                    events.clear();
                    listView.setAdapter(null);
                    for (Events events2 : event) {
                        if (categories.equals("All")) {
                            events.add(events2);
                            viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                            listView.setAdapter(viewAdapter);
                        } else if (events2.getCategory().toLowerCase().equals(categories.toLowerCase())) {
                            events.add(events2);
                            viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                            listView.setAdapter(viewAdapter);
                        }
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
                    if(!searchedit.getText().toString().equals("")) {
                        performSearch();
                        searched = true;
                    }
                    else {
                        events.clear();
                        for (Events events2 : EventsFragment.event) {
                            events.add(events2);
                            viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                            listView.setAdapter(viewAdapter);
                            searched = false;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
//        searchedit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                performSearch();
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

        if(!searchedit.getText().toString().equals("")){
            performSearch();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                publicEvent = events.get(i);
                Toast.makeText(getContext(), "inside listView on Click listener", Toast.LENGTH_SHORT).show();
                if("sports".equals(publicEvent.getCategory().toLowerCase())){
                    Main2Activity.userSports++;
                }
                else if("arts".equals(publicEvent.getCategory().toLowerCase())){
                    Main2Activity.userArts++;
                }
                else if("music".equals(publicEvent.getCategory().toLowerCase())){
                    Main2Activity.userMusic++;
                }
                else if("family".equals(publicEvent.getCategory().toLowerCase())){
                    Main2Activity.userFamily++;
                }
                else if("others".equals(publicEvent.getCategory().toLowerCase())){
                    Main2Activity.userOthers++;
                }


                if(searched) {
                    String clickedSearchTerm = listOrder.get(i);
                    for (SimilarWord sm : smWords) {
                        Log.d("TAG", "onItemClick: " + sm.getRelevantWord());
                        if (sm.getSearchTerm().toLowerCase().equals(searchedit.getText().toString().toLowerCase())) {
                            Map<String, Integer> temp = sm.getRelevantWord();
                            for (Map.Entry<String, Integer> entry : sm.getRelevantWord().entrySet()){
                                if (clickedSearchTerm.toLowerCase().equals(entry.getKey().toLowerCase()))
                                    clickedSearchTerm = entry.getKey();
                            }

//                            Log.d("TAG", "onItemClick: " + temp.get(clickedSearchTerm) + " " + clickedSearchTerm);
                            temp.put(clickedSearchTerm, temp.get(clickedSearchTerm) + 1 );
                            SimilarWord newTemp = new SimilarWord(searchedit.getText().toString());
                            newTemp.setRelevantWord(temp);
                            databaseReferencesimilarWords.child(searchedit.getText().toString()).setValue(newTemp);
                        }
                    }
                }


                Intent intent = new Intent(getActivity(), showEvent.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: " + searchedit.getText().toString());
        if(!searchedit.getText().toString().equals("")){
            Log.d("TAG", "onResume: inside if");
            events.clear();
            performSearch();
        }
    }

    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public void performSearch() {
        searchboolean = false;
        String search = searchedit.getText().toString();
        events.clear();
        listOrder.clear();
        listView.setAdapter(null);
        for (Events events2 : event) {
            if (search.equals("")) {
                if (!eventContains(events, events2)) {
                    events.add(events2);
                    viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                    listView.setAdapter(viewAdapter);
                }
            } else if (events2.getCategory().toLowerCase().contains(search.toLowerCase())) {
                if (!eventContains(events, events2)) {
                    events.add(events2);
                    listOrder.add(search);
                    viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                    listView.setAdapter(viewAdapter);
                }
            }else if (events2.getEventName().toLowerCase().contains(search.toLowerCase())) {
                if (!eventContains(events, events2)) {
                    events.add(events2);
                    listOrder.add(search);
                    viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                    listView.setAdapter(viewAdapter);
                }
            }else if (events2.getLocation().toLowerCase().contains(search.toLowerCase())) {
                if (!eventContains(events, events2)) {
                    events.add(events2);
                    listOrder.add(search);
                    viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                    listView.setAdapter(viewAdapter);
                }
            }
        }

        for (SimilarWord sm: smWords){
            if (sm.getSearchTerm().equals(search)){
                searchboolean = true;
                sm.sort();
                for (Map.Entry<String, Integer> entry : sm.getRelevantWord().entrySet()) {
                    Log.d("Tag entry", "performSearch: similar word is : " + entry.getKey());
                    for (Events events2 : event) {
                        if (events2.getCategory().toLowerCase().contains(entry.getKey().toLowerCase())) {
                            if (!eventContains(events, events2)) {
                                events.add(events2);
                                listOrder.add(entry.getKey());
                                viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                                listView.setAdapter(viewAdapter);
                            }
                        } else if (events2.getEventName().toLowerCase().contains(entry.getKey().toLowerCase())) {
                            if (!eventContains(events, events2)) {
                                events.add(events2);
                                listOrder.add(entry.getKey());
                                viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                                listView.setAdapter(viewAdapter);
                            }
                        } else if (events2.getLocation().toLowerCase().contains(entry.getKey().toLowerCase())) {
                            if (!eventContains(events, events2)) {
                                events.add(events2);
                                listOrder.add(entry.getKey());
                                viewAdapter viewAdapter = new viewAdapter(getActivity(), events);
                                listView.setAdapter(viewAdapter);
                            }
                        }
                    }

                }
            }
        }
        if(!searchboolean){
            if(events.size() > 0) {
                Events temp = events.get(0);
                String[] words = temp.getEventName().split(" ");
                SimilarWord newTemp = new SimilarWord(search);

                for (String word : words) {
                    if (word.length()>2)
                        newTemp.relevantWord.put(word, 0);
                }
                newTemp.relevantWord.put(temp.getLocation(), 0);
                databaseReferencesimilarWords.child(search).setValue(newTemp);
            }
        }

        for (String order: listOrder){
            Log.d("TAG Order", "performSearch: " + order);
        }
    }

    public boolean eventContains(List<Events> arr, Events one){
        for (Events all: arr){
            if (one.getCategory().toLowerCase().equals(all.getCategory().toLowerCase()) &&
                    one.getLocation().toLowerCase().equals(all.getLocation().toLowerCase()) &&
                    one.getEventName().toLowerCase().equals(all.getEventName().toLowerCase()) &&
                    one.getDate().toLowerCase().equals(all.getDate().toLowerCase()))
            {
                return true;
            }
        }
        return false;
    }
}




