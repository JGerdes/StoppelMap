package com.jonasgerdes.stoppelmap.usecases.map.search;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 22.07.2016.
 */
public class SearchResultHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.image)
    ImageView mImageView;

    public SearchResultHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(SearchResult result) {
        mTitle.setText(result.mapEntity.getName());
        Context context = itemView.getContext();
        String headerFile = result.mapEntity.getHeaderImageFile();
        String headerPath = context.getString(R.string.asset_map_entity_header_dir, headerFile);
        Glide.with(context)
                .load(Uri.parse(headerPath))
                .centerCrop()
                .into(mImageView);
    }
}
