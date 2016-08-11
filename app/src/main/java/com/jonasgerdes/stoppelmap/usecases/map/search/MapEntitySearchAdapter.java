package com.jonasgerdes.stoppelmap.usecases.map.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.map.Tag;
import com.jonasgerdes.stoppelmap.model.map.search.EntitySearchResult;
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult;
import com.jonasgerdes.stoppelmap.model.map.search.SynoynmSearchResult;
import com.jonasgerdes.stoppelmap.model.map.search.TagSearchResult;
import com.jonasgerdes.stoppelmap.model.shared.RealmString;
import com.jonasgerdes.stoppelmap.views.SearchCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<SearchResult> tempResults = new ArrayList<>();
        Map<String, TagSearchResult> tagResults = new HashMap<>();
        if (!query.trim().isEmpty()) {
            query = query.toLowerCase().trim();
            for (MapEntity mapEntity : mMapEntities) {

                //name search
                if (mapEntity.getName().toLowerCase().contains(query)
                        && !mapEntity.isHiddenFromSearch()) {
                    SearchResult result = new EntitySearchResult(mapEntity);
                    tempResults.add(result);
                }

                //synonym search
                if (mapEntity.getSynonyms() != null) {
                    for (RealmString synonym : mapEntity.getSynonyms()) {
                        if (synonym.getVal().toLowerCase().contains(query)) {
                            SearchResult result
                                    = new SynoynmSearchResult(synonym.getVal(), mapEntity);
                            tempResults.add(result);
                        }
                    }
                }

                //tag search
                for (Tag tag : mapEntity.getTags()) {
                    if (tag.getName().toLowerCase().contains(query)) {
                        TagSearchResult result;
                        if (!tagResults.containsKey(tag.getName())) {
                            result = new TagSearchResult(tag);
                            tagResults.put(tag.getName(), result);
                        } else {
                            result = tagResults.get(tag.getName());
                        }
                        result.addEntity(mapEntity);
                    }
                }
            }

            tempResults.addAll(tagResults.values());
        }
        animateTo(tempResults);
    }

    @Override
    public void onSearch(String query) {
        if (mSelectedListener != null && mSearchResults != null && mSearchResults.size() > 0) {
            mSelectedListener.onResultSelected(mSearchResults.get(0));
        }
    }

    @Override
    public SearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        switch (viewType) {
            case R.layout.map_search_entity_result_item:
                return new EntityResultHolder(view);
            case R.layout.map_search_synonym_result_item:
                return new SynonymResultHolder(view);
            case R.layout.map_search_tag_result_item:
                return new TagResultHolder(view);


        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return mSearchResults.get(position).getLayout();
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
//        mSearchResults = models;
//        notifyDataSetChanged();
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
