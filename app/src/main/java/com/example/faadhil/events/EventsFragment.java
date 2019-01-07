package com.example.faadhil.events;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
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
    public FloatingActionButton fab;

    SharedPreferences prefs ;


    public int userSports  ;
    public int userArts  ;
    public int userMusic ;
    public int userFamily;
    public int userOthers;


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

        prefs = getActivity().getPreferences(MODE_PRIVATE);
        userSports = prefs.getInt("userSports", 0) ;
        userArts = prefs.getInt("userArts", 0) ;
        userMusic =  prefs.getInt("userMusic", 0) ;
        userFamily = prefs.getInt("userFamily", 0) ;
        userOthers = prefs.getInt("userOthers", 0) ;

        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        c = Calendar.getInstance().getTime();
        SimpleDateFormat sp = new SimpleDateFormat("yyMMdd");
        final String formattedc = sp.format(c);

        categoriesSpinner = (Spinner) getView().findViewById(spinner);
        listView = (ListView) getView().findViewById(R.id.eventsListView);
        searchedit = (EditText) getView().findViewById(R.id.searchbox);
        searchboolean = false;
        searched = false;
        fab = (FloatingActionButton) getView().findViewById(R.id.floatingActionButton);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.userboolean) {
                    Intent intent = new Intent(getActivity(), LogIn.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), AddEvents.class);
                    startActivity(intent);
                }
            }

        });




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
                events.clear();

                String category = "All";

                for (DataSnapshot eventsdata : dataSnapshot.getChildren()) {
                    String currentEventDate = eventsdata.getValue(Events.class).getDate();
                    String formattedDate = currentEventDate.substring(11, 13) + currentEventDate.substring(8, 10) + currentEventDate.substring(5, 7) ;
                    if (Integer.parseInt(formattedDate) >= Integer.parseInt(formattedc)) {
                        event.add(eventsdata.getValue(Events.class));
                    }

                }

                new MyAsyncTask(getContext()).execute();




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

        if(!searchedit.getText().toString().equals("")){
            performSearch();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                publicEvent = events.get(i);
                if("sport".equals(publicEvent.getCategory().toLowerCase())){
                    userSports++;
                }
                else if("art".equals(publicEvent.getCategory().toLowerCase())){
                    userArts++;
                }
                else if("music".equals(publicEvent.getCategory().toLowerCase())){
                    userMusic++;
                }
                else if("family".equals(publicEvent.getCategory().toLowerCase())){
                    userFamily++;
                }
                else if("other".equals(publicEvent.getCategory().toLowerCase())){
                    userOthers++;
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
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getActivity().getPreferences(MODE_PRIVATE).edit();
        editor.putInt("userSports", userSports);
        editor.putInt("userArts", userArts);
        editor.putInt("userMusic", userMusic);
        editor.putInt("userFamily", userFamily);
        editor.putInt("userOthers", userOthers);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!searchedit.getText().toString().equals("")){
            new MyAsyncTaskOnResume().execute();
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
                    if (word.length()>2){
                        if(!word.toLowerCase().equals(search.toLowerCase())) {
                            newTemp.relevantWord.put(word, 0);
                        }

                    }

                }
                newTemp.relevantWord.put(temp.getLocation(), 0);
                databaseReferencesimilarWords.child(search).setValue(newTemp);
            }
        }


    }

    public boolean eventContains(List<Events> arr, Events one){
        for (Events all: arr){
            if (one.getLocation().toLowerCase().equals(all.getLocation().toLowerCase()) &&
                    one.getEventName().toLowerCase().equals(all.getEventName().toLowerCase()) &&
                    one.getDate().toLowerCase().equals(all.getDate().toLowerCase()))
            {
                return true;
            }
        }
        return false;
    }

    void bubbleSort(List<Events> events)
    {
        int n = events.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                String DateJ = events.get(j).getDate().substring(11, 13) +
                        events.get(j).getDate().substring(8, 10) +
                        events.get(j).getDate().substring(5, 7) ;
                String DateJ1 = events.get(j+1).getDate().substring(11, 13) +
                        events.get(j+1).getDate().substring(8, 10) +
                        events.get(j+1).getDate().substring(5, 7) ;
                if ( Integer.parseInt(DateJ) >  Integer.parseInt(DateJ1)) {
                    Events temp = events.get(j);
                    events.set(j, events.get(j+1));
                    events.set(j+1, temp);
                }
            }
        }
    }

    class MyAsyncTask extends AsyncTask<String, String, String >{

        Context context;

        public MyAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("TAG", "doInBackground: ");
            bubbleSort(event);
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String category = "All";
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
    }
    class MyAsyncTaskOnResume extends AsyncTask<String, String, String>{

        public MyAsyncTaskOnResume() {
        }

        @Override
        protected String doInBackground(String... strings) {
                events.clear();

            return null;
        }
    }



}




