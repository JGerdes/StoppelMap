package com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards;

import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.Info;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCard;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCardHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 19.07.2016.
 */
public class InfoEntityCardHolder extends EntityCardHolder {

    public static final int LAYOUT = R.layout.map_entity_card_info;

    @BindView(R.id.title)
    TextView mTitleView;

    @BindView(R.id.text)
    TextView mTextView;

    public InfoEntityCardHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(EntityCard entityCard) {
        Info info = entityCard.getMapEntity().getInfo();
        if (info.getTitle() == null || info.getTitle().trim().isEmpty()) {
            mTitleView.setVisibility(View.GONE);
        } else {
            mTitleView.setVisibility(View.VISIBLE);
            mTitleView.setText(info.getTitle());
        }

        mTextView.setText(Html.fromHtml(info.getText()));
    }
}
