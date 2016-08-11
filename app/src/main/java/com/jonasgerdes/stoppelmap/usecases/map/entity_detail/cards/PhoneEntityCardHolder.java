package com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.shared.PhoneNumber;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCard;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCardHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 11.08.2016.
 */
public class PhoneEntityCardHolder extends EntityCardHolder {

    public static final int LAYOUT = R.layout.map_entity_card_phone;

    @BindView(R.id.container)
    ViewGroup mContainer;

    public PhoneEntityCardHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(EntityCard entityCard) {
        final Context context = itemView.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        mContainer.removeAllViews();
        List<View> phoneViews = new ArrayList<>();
        for (final PhoneNumber phoneNumber : entityCard.getMapEntity().getPhoneNumbers()) {
            View taxiView = inflater.inflate(R.layout.transportation_taxi_item, mContainer, false);

            ((TextView) taxiView.findViewById(R.id.title)).setText(phoneNumber.getName());
            ((TextView) taxiView.findViewById(R.id.description)).setText(phoneNumber.getDescription());
            ((TextView) taxiView.findViewById(R.id.phone)).setText(phoneNumber.getPhoneText());

            taxiView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phoneNumber.getPhone()));
                    context.startActivity(callIntent);
                }
            });
            phoneViews.add(taxiView);

        }

        for (View taxiView : phoneViews) {
            mContainer.addView(taxiView);
        }
    }
}
