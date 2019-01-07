package com.example.faadhil.events;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Faadhil on 12/8/2018.
 */

public class viewAdapter extends ArrayAdapter {

    Context context;
    List<Events> eventsList;

    public viewAdapter(Context context, List<Events> eventsList) {
        super(context, R.layout.events_view_layout, eventsList);
        this.context = context;
        this.eventsList = eventsList;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View listViewItem = inflater.inflate(R.layout.events_view_layout, null, true);

        ImageView imageView = (ImageView) listViewItem.findViewById(R.id.imageView);
        TextView eventName = (TextView) listViewItem.findViewById(R.id.eventTitle);
        TextView dateTextView = (TextView) listViewItem.findViewById(R.id.dateTextView);
        TextView locationTextView = (TextView) listViewItem.findViewById(R.id.locationTextView);
        TextView category = (TextView) listViewItem.findViewById(R.id.categorytext);


        Events event = eventsList.get(position);

        eventName.setText(event.getEventName());
        dateTextView.setText(event.getDate() );
        locationTextView.setText(event.getLocation());
        category.setText(event.getCategory());

        Picasso.with(context).load(Uri.parse(event.getUri()))
                .transform(new RoundedCornersTransformation(10,0))
                .resize(100,100)
                .centerCrop()
                .into(imageView);

        return listViewItem;



    }
}
