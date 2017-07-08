package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult
import kotlinx.android.synthetic.main.map_search_result_single.view.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 06.07.17
 */
abstract class SearchResultHolder<in E : MapSearchResult>(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(result: E)

    class SingleResult(itemView: View)
        : SearchResultHolder<SingleEntitySearchResult>(itemView) {
        override fun onBind(result: SingleEntitySearchResult) {
            Log.d("SearchResultHolder", "onBindCalled with " + result.entity.slug)
            itemView.title.text = result.title
            if (result.fromAlias != null) {
                itemView.alias.visibility = View.VISIBLE
                itemView.alias.text = result.fromAlias
            } else {
                itemView.alias.visibility = View.GONE
            }
        }

    }
}