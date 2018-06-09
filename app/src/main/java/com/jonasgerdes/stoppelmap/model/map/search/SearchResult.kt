package com.jonasgerdes.stoppelmap.model.map.search

import android.support.v7.util.DiffUtil
import com.jonasgerdes.stoppelmap.model.map.entity.Stall

sealed class SearchResult(val id: String) {

    data class SingleStallResult(val stall: Stall) : SearchResult(stall.slug)

    object DiffCallback : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) =
                oldItem.id == newItem.id

    }

}