package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 06.07.17
 */
abstract class SearchResultHolder<in E : MapSearchResult>(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(result: E)
}