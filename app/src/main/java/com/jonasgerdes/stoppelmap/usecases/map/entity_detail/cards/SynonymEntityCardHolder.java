package com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.shared.RealmString;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCard;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCardHolder;
import com.jonasgerdes.stoppelmap.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 19.07.2016.
 */
public class SynonymEntityCardHolder extends EntityCardHolder {

    public static final int LAYOUT = R.layout.map_entity_card_synonym;

    @BindView(R.id.container)
    ViewGroup mContainer;

    public SynonymEntityCardHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(EntityCard entityCard) {
        Context context = itemView.getContext();
        for (RealmString name : entityCard.getMapEntity().getSynonyms()) {
            TextView textView = new TextView(itemView.getContext());
            textView.setText(name.getVal());
            int padding = ViewUtil.dpToPx(context, 4);
            textView.setPadding(padding, padding, padding, padding);
            textView.setGravity(Gravity.CENTER);
            mContainer.addView(textView);
        }
    }
}
