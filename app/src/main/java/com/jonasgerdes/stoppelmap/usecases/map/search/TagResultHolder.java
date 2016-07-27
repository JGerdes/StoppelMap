package com.jonasgerdes.stoppelmap.usecases.map.search;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.Tag;
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult;
import com.jonasgerdes.stoppelmap.model.map.search.TagSearchResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 22.07.2016.
 */
public class TagResultHolder extends SearchResultHolder {

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.image)
    ImageView mIconView;

    public TagResultHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(SearchResult result) {
        TagSearchResult tagResult = (TagSearchResult) result;
        mTitle.setText(tagResult.getTag().getName());
        if (tagResult.getTag().getIcon() == Tag.ICON_NONE) {
            mIconView.setImageDrawable(null);
        } else {
            mIconView.setImageResource(tagResult.getTag().getIcon());
        }
        Context context = itemView.getContext();
    }
}
