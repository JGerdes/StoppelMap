package com.jonasgerdes.stoppelmap.usecases.map.search;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult;
import com.jonasgerdes.stoppelmap.model.map.search.SynoynmSearchResult;

import butterknife.BindView;

/**
 * Created by Jonas on 22.07.2016.
 */
public class SynonymResultHolder extends EntityResultHolder {

    @Nullable
    @BindView(R.id.synonym)
    TextView mSynonym;


    public SynonymResultHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(SearchResult result) {
        super.onBind(result);
        SynoynmSearchResult entityResult = (SynoynmSearchResult) result;
        mSynonym.setText(entityResult.getSynonym());
    }
}
