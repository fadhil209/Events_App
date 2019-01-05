package com.example.faadhil.events;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Faadhil on 12/27/2018.
 */

public class SimilarWord {
    String searchTerm;
    Map<String, Integer> relevantWord;

    public SimilarWord() {
    }

    public SimilarWord(String searchTerm) {
        this.searchTerm = searchTerm;
        relevantWord = new HashMap<String, Integer>();
    }

    public Map<String, Integer> getRelevantWord() {
        return relevantWord;
    }

    public void setRelevantWord(Map<String, Integer> relevantWord) {
        this.relevantWord = relevantWord;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }


    public void sort(){
    // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(relevantWord.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }

        relevantWord = temp;
    }
}
