package com.jonasgerdes.stoppelmap;

import android.content.Context;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.jonasgerdes.stoppelmap.data.DataController;
import com.jonasgerdes.stoppelmap.data.SearchResult;

import java.util.List;

public class SearchController implements SearchView.OnQueryTextListener {

    private CursorAdapter adapter;
    private DataController data;
    private List<SearchResult> currentResults;

    public SearchController(DataController data, Context context){
        this.data = data;
        final String[] from = new String[] {"title"};
        final int[] to = new int[] {android.R.id.text1};
        adapter = new SimpleCursorAdapter(context,
                android.R.layout.simple_list_item_1,
                null,
                from,
                to,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final MatrixCursor c = new MatrixCursor(new String[]{ BaseColumns._ID, "title" });
        List<SearchResult> result = data.query(newText);
        currentResults = result;
        for (int i = 0; i <result.size(); i++) {
                c.addRow(new Object[] {i, result.get(i).getTitle()});
        }
        adapter.changeCursor(c);
        return true;
    }


    public CursorAdapter getAdpater(){
        return adapter;
    }

    public SearchResult getSelectedById(int id){
        return currentResults == null ? null : currentResults.get(id);
    }
}
