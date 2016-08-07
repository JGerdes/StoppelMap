package com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.transportation.Taxi;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCard;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCardHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 19.07.2016.
 */
public class TaxiEntityCardHolder extends EntityCardHolder {

    public static final int LAYOUT = R.layout.map_entity_card_taxis;

    @BindView(R.id.container)
    ViewGroup mContainer;

    public TaxiEntityCardHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(EntityCard entityCard) {
        final Context context = itemView.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        List<Taxi> taxis = entityCard.getExtraData();
        mContainer.removeAllViews();
        List<View> taxiViews = new ArrayList<>();
        for (final Taxi taxi : taxis) {
            View taxiView = inflater.inflate(R.layout.transportation_taxi_item, mContainer, false);

            ((TextView) taxiView.findViewById(R.id.title)).setText(taxi.getName());
            ((TextView) taxiView.findViewById(R.id.location)).setText(taxi.getLocation());
            ((TextView) taxiView.findViewById(R.id.phone)).setText(taxi.getPhoneText());

            taxiView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + taxi.getPhone()));
                    context.startActivity(callIntent);
                }
            });
            taxiViews.add(taxiView);

        }

        for (View taxiView : taxiViews) {
            mContainer.addView(taxiView);
        }
    }
}
