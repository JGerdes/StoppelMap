package com.jonasgerdes.stoppelmap.usecases.map.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult;
import com.jonasgerdes.stoppelmap.views.SearchCardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 22.07.2016.
 */
public class MapEntitySearchAdapter extends SearchCardView.ResultAdapter<SearchResultHolder> {

    private List<SearchResult> mSearchResults;
    private List<MapEntity> mMapEntities;
    private OnResultSelectedListener mSelectedListener;

    public interface OnResultSelectedListener {
        void onResultSelected(SearchResult result);
    }

    public MapEntitySearchAdapter(List<MapEntity> mapEntities) {
        mMapEntities = mapEntities;
        mSearchResults = new ArrayList<>();
    }

    @Override
    public void onQueryChanged(String query) {
        mSearchResults.clear();
        if (!query.trim().isEmpty()) {
            query = query.toLowerCase();
            for (MapEntity mapEntity : mMapEntities) {
                if (mapEntity.getName().toLowerCase().contains(query)) {
                    SearchResult result = new SearchResult();
                    result.mapEntity = mapEntity;
                    mSearchResults.add(result);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public SearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.map_search_result_item, parent, false);
        return new SearchResultHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultHolder holder, int position) {
        final SearchResult result = mSearchResults.get(position);
        holder.onBind(result);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedListener != null) {
                    mSelectedListener.onResultSelected(result);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }

    public void setSelectedListener(OnResultSelectedListener selectedListener) {
        mSelectedListener = selectedListener;
    }
}
