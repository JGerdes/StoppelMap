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
        List<SearchResult> mTempResults = new ArrayList<>();
        if (!query.trim().isEmpty()) {
            query = query.toLowerCase();
            for (MapEntity mapEntity : mMapEntities) {
                if (mapEntity.getName().toLowerCase().contains(query)) {
                    SearchResult result = new SearchResult();
                    result.mapEntity = mapEntity;
                    mTempResults.add(result);
                }
            }
        }
        animateTo(mTempResults);
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
                if (mSelectedListener != null) {
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

    public void animateTo(List<SearchResult> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    public SearchResult removeItem(int position) {
        final SearchResult model = mSearchResults.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, SearchResult model) {
        mSearchResults.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final SearchResult model = mSearchResults.remove(fromPosition);
        mSearchResults.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    private void applyAndAnimateRemovals(List<SearchResult> newModels) {
        for (int i = mSearchResults.size() - 1; i >= 0; i--) {
            final SearchResult model = mSearchResults.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<SearchResult> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final SearchResult model = newModels.get(i);
            if (!mSearchResults.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<SearchResult> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final SearchResult model = newModels.get(toPosition);
            final int fromPosition = mSearchResults.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}
