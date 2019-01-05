package com.example.faadhil.events;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Faadhil on 1/2/2019.
 */

public class RecommendationFragment extends Fragment{

    ListView listView;
    int Sports = Main2Activity.userSports;
    int Arts = Main2Activity.userArts;
    int Music = Main2Activity.userMusic;
    int Family = Main2Activity.userFamily;
    int Others = Main2Activity.userOthers;
    int[] sorted = {Sports, Arts, Music, Family, Others};

    List<Events> allEvents ;
    List<Events> recommended;

    public RecommendationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        int x =0;
        for(int i = 4; i>=2; i--) {
            if(Sports == sorted[i] && !contains(sortedCategory, "Sports")){
                sortedCategory[x] = "Sports";
                x++;
            }
            else if (Arts == sorted[i] && !contains(sortedCategory, "Arts")){
                sortedCategory[x] = "Arts";
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
            else if (Others == sorted[i] && !contains(sortedCategory, "Others")){
                sortedCategory[x] = "Others";
                x++;
            }
        }

        Log.d("Recommendation", sortedCategory[0]  +  " " + sortedCategory[1]+  " " + sortedCategory[2]);
        Log.d("Recommendation clicks", Main2Activity.userSports +  " " + Main2Activity.userArts +  " " + Main2Activity.userMusic +  " " + Main2Activity.userFamily);
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

    @Override
    public void onResume() {
        super.onResume();
        listView = (ListView) getView().findViewById(R.id.recommendationListView);
        recommended = new ArrayList<>();
        allEvents = EventsFragment.event;
        Sports = Main2Activity.userSports;
        Arts = Main2Activity.userArts;
        Music = Main2Activity.userMusic;
        Family = Main2Activity.userFamily;
        Others = Main2Activity.userOthers;


        sorted[0] = Sports;
        sorted[1] = Arts;
        sorted[2] = Music;
        sorted[3] = Family;
        sorted[4] = Others;


        String[] sortedCategory = new String[3];
        Log.d(" Recommendation","On resume before " + sortedCategory[0]  +  " " + sortedCategory[1]+  " " + sortedCategory[2]);

        Arrays.sort(sorted);

        int x =0;
        for(int i = 4; i>=2; i--) {
            if(Sports == sorted[i] && !contains(sortedCategory, "Sports")){
                sortedCategory[x] = "Sports";
                x++;
            }
            else if (Arts == sorted[i] && !contains(sortedCategory, "Arts")){
                sortedCategory[x] = "Arts";
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
            else if (Others == sorted[i] && !contains(sortedCategory, "Others")){
                sortedCategory[x] = "Others";
                x++;
            }
        }

        Log.d(" Recommendation","On resume" + sortedCategory[0]  +  " " + sortedCategory[1]+  " " + sortedCategory[2]);
        Log.d("Recommendation clicks","On resume" + Main2Activity.userSports +  " " + Main2Activity.userArts +  " " + Main2Activity.userMusic +  " " + Main2Activity.userFamily);
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
