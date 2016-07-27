package com.jonasgerdes.stoppelmap.usecases.map.search;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.search.EntitySearchResult;
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 22.07.2016.
 */
public class EntityResultHolder extends SearchResultHolder {

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.image)
    ImageView mImageView;

    public EntityResultHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(SearchResult result) {
        EntitySearchResult entityResult = (EntitySearchResult) result;
        mTitle.setText(entityResult.getMapEntity().getName());
        Context context = itemView.getContext();
        String headerFile = entityResult.getMapEntity().getHeaderImageFile();
        String headerPath = context.getString(R.string.asset_map_entity_header_dir, headerFile);
        Glide.with(context)
                .load(Uri.parse(headerPath))
                .centerCrop()
                .into(mImageView);
    }
}
