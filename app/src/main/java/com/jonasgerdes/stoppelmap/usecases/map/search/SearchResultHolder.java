package com.jonasgerdes.stoppelmap.usecases.map.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jonasgerdes.stoppelmap.model.map.search.SearchResult;

/**
 * Created by Jonas on 27.07.2016.
 */
public abstract class SearchResultHolder extends RecyclerView.ViewHolder {

    public SearchResultHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBind(SearchResult result);
}
