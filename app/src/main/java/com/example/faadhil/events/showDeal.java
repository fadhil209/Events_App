package com.example.faadhil.events;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class showDeal extends AppCompatActivity {


    Deals deal = DealsFragment.publicdeal;
    ImageView dealImage;
    TextView dealName;
    TextView dealDescription;
    TextView dealLocation;
    TextView dealLink;
    TextView dealDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_deal);

        dealImage = (ImageView) findViewById(R.id.dealsImage);
        dealName = (TextView) findViewById(R.id.DealName);
        dealDescription = (TextView) findViewById(R.id.dealDescription);
        dealLocation = (TextView) findViewById(R.id.dealLocation);
        dealDate = (TextView) findViewById(R.id.dealDate);
        dealLink = (TextView) findViewById(R.id.dealLink);


        dealName.setText(deal.getDealName());
        dealDescription.setText(deal.getDescription());
        dealLocation.setText(deal.getLocation());
        dealDate.setText(deal.getStartdate() + " to " + deal.getEnddate());
        dealLink.setText(deal.getLink());


        Picasso.with(getBaseContext()).load(deal.getUri()).transform(new RoundedCornersTransformation(10,10)).fit().into(dealImage);


        dealLink.setTextColor(Color.parseColor("#0000EE"));
        dealLink.setPaintFlags(dealLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        dealLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(dealLink.getText().toString()));
                    startActivity(i);
            }
        });
    }
}
