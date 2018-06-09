package com.jonasgerdes.stoppelmap.map

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult
import com.jonasgerdes.stoppelmap.util.inflate
import java.lang.IllegalArgumentException

class SearchResultAdapter : ListAdapter<SearchResult,
        SearchResultAdapter.ResultHolder>(SearchResult.DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        return parent.inflate(getItemViewType(viewType)).let {
            when (viewType) {
                R.layout.map_search_result_item_single_stall -> ResultHolder.SingleStallHolder(it)
                else -> throw IllegalArgumentException("invalid view type $viewType")
            }
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is SearchResult.SingleStallResult -> R.layout.map_search_result_item_single_stall
    }

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    sealed class ResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        class SingleStallHolder(itemView: View) : ResultHolder(itemView)
    }
}