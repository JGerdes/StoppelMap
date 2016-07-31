package com.jonasgerdes.stoppelmap.model.map.search;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;

/**
 * Created by Jonas on 27.07.2016.
 */
public class SynoynmSearchResult extends EntitySearchResult {


    private String mSynonym;

    public SynoynmSearchResult(String synonym, MapEntity mapEntity) {
        super(mapEntity);
        mSynonym = synonym;
    }

    @Override
    public String getIdentifier() {
        return getMapEntity().getUuid() + mSynonym;
    }

    @Override
    public int getLayout() {
        return R.layout.map_search_synonym_result_item;
    }

    public String getSynonym() {
        return mSynonym;
    }
}
