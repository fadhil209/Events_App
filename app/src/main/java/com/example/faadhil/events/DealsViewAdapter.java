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
        super(context, R.layout.deals_grid_view, dealsList);
        this.context = context;
        this.dealsList = dealsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View listViewItem = inflater.inflate(R.layout.deals_grid_view, null, true);
        ImageView imageView = (ImageView) listViewItem.findViewById(R.id.gridviewImage);
        Deals deal = dealsList.get(position);
        imageView.setPadding(15, 15, 15, 15);
        Picasso.with(context).load(Uri.parse(deal.getUri())).fit().into(imageView);

        return listViewItem;
    }
}
