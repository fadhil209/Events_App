package com.example.faadhil.events;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class showEvent extends AppCompatActivity {

    Events event = EventsFragment.publicEvent;

    ImageView eventImage;
    TextView eventName;
    TextView eventDescription;
    TextView eventCategory;
    TextView eventLocation;
    TextView eventDate;
    TextView eventTime;
    TextView eventDeal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        eventImage = (ImageView) findViewById(R.id.showEventImage);
        eventName = (TextView) findViewById(R.id.showEventName);
        eventDescription = (TextView) findViewById(R.id.showEventDescription);
        eventCategory = (TextView) findViewById(R.id.showEventCategory);
        eventLocation = (TextView) findViewById(R.id.showEventLocation);
        eventDate = (TextView) findViewById(R.id.showEventDate);
        eventTime = (TextView) findViewById(R.id.showEventTime);
        eventDeal = (TextView) findViewById(R.id.showEventDeal);


        eventName.setText(event.getEventName());
        eventDescription.setText(event.getDescription());
        eventCategory.setText(event.getCategory());
        eventLocation.setText(event.getLocation());
        eventDate.setText(event.getDate());
        eventTime.setText(event.getTime());
        eventDeal.setText(event.getDeals());

        Picasso.with(getBaseContext()).load(event.getUri()).transform(new RoundedCornersTransformation(10,10)).fit().into(eventImage);

    }
}
