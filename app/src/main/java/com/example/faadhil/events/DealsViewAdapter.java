package com.example.faadhil.events;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Faadhil on 12/26/2018.
 */

public class DealsViewAdapter extends ArrayAdapter {

    Context context;
    List<Deals> dealsList;

    public DealsViewAdapter(Context context, List<Deals> dealsList) {
        super(context, R.layout.deals_view_layout, dealsList);
        this.context = context;
        this.dealsList = dealsList;
    }

//    @NonNull
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//
//        View listViewItem = inflater.inflate(R.layout.deals_view_layout, null, true);
//
//        ImageView imageView = (ImageView) listViewItem.findViewById(R.id.dealsImage);
//        TextView dealsName = (TextView) listViewItem.findViewById(R.id.DealsName);
//        TextView timeTextView = (TextView) listViewItem.findViewById(R.id.timeTextView);
//        TextView locationTextView = (TextView) listViewItem.findViewById(R.id.dealsLocationTextView);
//
//
//        Deals deal = dealsList.get(position);
//
//
//        dealsName.setText(deal.getEventName());
//        timeTextView.setText(deal.getStartdate() + " to " + deal.getEnddate());
//        locationTextView.setText(deal.getLocation());
//
//
//
//        Picasso.with(context).load(Uri.parse(deal.getUri())).fit().into(imageView);
//
//        return listViewItem;
//
//    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View listViewItem = inflater.inflate(R.layout.deals_grid_view, null, true);

        ImageView imageView = (ImageView) listViewItem.findViewById(R.id.gridviewImage);
//        TextView dealsName = (TextView) listViewItem.findViewById(R.id.DealsName);
//        TextView timeTextView = (TextView) listViewItem.findViewById(R.id.timeTextView);
//        TextView locationTextView = (TextView) listViewItem.findViewById(R.id.dealsLocationTextView);


        Deals deal = dealsList.get(position);


//        dealsName.setText(deal.getEventName());
//        timeTextView.setText(deal.getStartdate() + " to " + deal.getEnddate());
//        locationTextView.setText(deal.getLocation());

        //imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85));
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(15, 15, 15, 15);



        Picasso.with(context).load(Uri.parse(deal.getUri())).fit().into(imageView);

        return listViewItem;
    }
}
