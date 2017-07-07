package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 06.07.17
 */
class SearchResultAdapter : RecyclerView.Adapter<SearchResultHolder<*>>() {

    private var resultList: List<MapSearchResult> = ArrayList()

    var results
        get() = resultList
        set(value) {
            resultList = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return when (resultList[position]) {
            is MapSearchResult -> R.layout.map_search_result_single
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchResultHolder<*> {
        val view = LayoutInflater.from(parent?.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.map_search_result_single -> SearchResultHolder.SingleResult(view)
            else -> SearchResultHolder.SingleResult(view)
        }
    }

    override fun onBindViewHolder(holder: SearchResultHolder<*>?, position: Int) {
        when (holder) {
            is SearchResultHolder.SingleResult -> holder.onBind(resultList[position] as SingleEntitySearchResult)
        }
    }

    override fun getItemCount(): Int {
        return resultList.size
    }
}