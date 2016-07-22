package com.jonasgerdes.stoppelmap.usecases.map.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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

    public SearchResultHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(SearchResult result) {
        mTitle.setText(result.title);
    }
}
