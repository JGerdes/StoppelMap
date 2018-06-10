package com.jonasgerdes.stoppelmap.map

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult
import com.jonasgerdes.stoppelmap.util.inflate
import kotlinx.android.synthetic.main.map_search_result_item_single_stall.view.*
import java.lang.IllegalArgumentException

class SearchResultAdapter : ListAdapter<SearchResult,
        SearchResultAdapter.ResultHolder>(SearchResult.DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        return parent.inflate(viewType).let {
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
        when (holder) {
            is ResultHolder.SingleStallHolder -> holder.bind(getItem(position) as SearchResult.SingleStallResult)
        }
    }

    sealed class ResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class SingleStallHolder(itemView: View) : ResultHolder(itemView) {
            fun bind(result: SearchResult.SingleStallResult) {
                itemView.title.text = result.stall.name
            }
        }
    }
}