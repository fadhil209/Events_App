package com.example.faadhil.events;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Faadhil on 1/2/2019.
 */

public class RecommendationFragment extends Fragment{

    ListView listView;

    SharedPreferences prefs ;
    int Sports ;
    int Arts ;
    int Music ;
    int Family ;
    int Others ;


    int[] sorted = new int[5];

    List<Events> allEvents ;
    List<Events> recommended;

    public RecommendationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        prefs = getActivity().getPreferences( MODE_PRIVATE);
        Sports = prefs.getInt("userSports", 0) ;
        Arts = prefs.getInt("userArts", 0) ;
        Music = prefs.getInt("userMusic", 0) ;
        Family = prefs.getInt("userFamily", 0) ;
        Others = prefs.getInt("userOthers", 0) ;

        sorted[0] = Sports;
        sorted[1] = Arts;
        sorted[2] = Music;
        sorted[3] = Family;
        sorted[4] = Others;


        return inflater.inflate(R.layout.activity_recommendation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.recommendationListView);
        recommended = new ArrayList<>();
        allEvents = EventsFragment.event;

        String[] sortedCategory = new String[3];

        Arrays.sort(sorted);

        Log.d("TAG", "onViewCreated: Arts" + Arts);
        int x =0;
        for(int i = 4; i>=2; i--) {
            if(Sports == sorted[i] && !contains(sortedCategory, "Sport")){
                sortedCategory[x] = "Sport";
                x++;
            }
            else if (Arts == sorted[i] && !contains(sortedCategory, "Art")){
                sortedCategory[x] = "Art";
                x++;
            }
            else if (Music == sorted[i] && !contains(sortedCategory, "Music")) {
                sortedCategory[x] = "Music";
                x++;
            }
            else if (Family == sorted[i] && !contains(sortedCategory, "Family")){
                sortedCategory[x] = "Family";
                x++;
            }
            else if (Others == sorted[i] && !contains(sortedCategory, "Other")){
                sortedCategory[x] = "Other";
                x++;
            }
        }

        recommended.clear();

        boolean add;
        for (int i = 0 ; i < 3; i++){
            for (int j = 0; j < sortedCategory.length; j++){
                add=true;
                Log.d("TAG", "onViewCreated: " + sortedCategory[j]);
                for (Events events2 : allEvents) {
                    if (events2.getCategory().toLowerCase().equals(sortedCategory[j].toLowerCase()) && add) {
                        if (!recommended.contains(events2)) {
                            recommended.add(events2);
                            viewAdapter viewAdapter = new viewAdapter(getActivity(), recommended);
                            listView.setAdapter(viewAdapter);
                            add= false;

                        }
                    }
                }
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventsFragment.publicEvent = recommended.get(i);
                Intent intent = new Intent(getActivity(), showEvent.class);
                startActivity(intent);
            }
        });







    }

    @Override
    public void onResume() {
        super.onResume();
        listView = (ListView) getView().findViewById(R.id.recommendationListView);
        recommended = new ArrayList<>();
        allEvents = EventsFragment.event;

        Sports = prefs.getInt("userSports", 0) ;
        Arts = prefs.getInt("userArts", 0) ;
        Music = prefs.getInt("userMusic", 0) ;
        Family = prefs.getInt("userFamily", 0) ;
        Others = prefs.getInt("userOthers", 0) ;


        sorted[0] = Sports;
        sorted[1] = Arts;
        sorted[2] = Music;
        sorted[3] = Family;
        sorted[4] = Others;


        String[] sortedCategory = new String[3];

        Arrays.sort(sorted);

        int x =0;
        for(int i = 4; i>=2; i--) {
            if(Sports == sorted[i] && !contains(sortedCategory, "Sport")){
                sortedCategory[x] = "Sport";
                x++;
            }
            else if (Arts == sorted[i] && !contains(sortedCategory, "Art")){
                sortedCategory[x] = "Art";
                x++;
            }
            else if (Music == sorted[i] && !contains(sortedCategory, "Music")) {
                sortedCategory[x] = "Music";
                x++;
            }
            else if (Family == sorted[i] && !contains(sortedCategory, "Family")){
                sortedCategory[x] = "Family";
                x++;
            }
            else if (Others == sorted[i] && !contains(sortedCategory, "Other")){
                sortedCategory[x] = "Other";
                x++;
            }
        }

        recommended.clear();

        boolean add;
        for (int i = 0 ; i < 3; i++){
            for (int j = 0; j < sortedCategory.length; j++){
                add=true;
                for (Events events2 : allEvents) {
                    if (events2.getCategory().toLowerCase().equals(sortedCategory[j].toLowerCase()) && add) {
                        if (!recommended.contains(events2)) {
                            recommended.add(events2);
                            viewAdapter viewAdapter = new viewAdapter(getActivity(), recommended);
                            listView.setAdapter(viewAdapter);
                            add= false;

                        }
                    }
                }
            }
        }





    }

    public boolean contains(String[] arr, String str){

            for (String temp : arr) {
                if(temp != null) {
                    if (temp.equals(str))
                        return true;
                }
            }

        return false;
    }

}
