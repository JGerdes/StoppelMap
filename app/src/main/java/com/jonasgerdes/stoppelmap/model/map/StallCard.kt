package com.jonasgerdes.stoppelmap.model.map

import android.support.v7.util.DiffUtil
import com.jonasgerdes.stoppelmap.model.map.entity.Image
import com.jonasgerdes.stoppelmap.model.map.entity.Stall

sealed class StallCard(val contentHash: String) {
    object DiffCallback : DiffUtil.ItemCallback<StallCard>() {
        override fun areItemsTheSame(old: StallCard, new: StallCard) = old.contentHash == new.contentHash

        override fun areContentsTheSame(old: StallCard, new: StallCard) = false //always update

    }
}

data class SingleStallCard(val stall: Stall, val images: List<Image>) : StallCard(stall.slug)

data class StallCollectionCard(val title: String, val type: String, val subType: String? = null,
                               val stalls: List<Stall>, val areAdditional:Boolean = false)
    : StallCard(stalls.joinToString { it.slug })