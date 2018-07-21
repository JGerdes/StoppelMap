package com.jonasgerdes.stoppelmap.model.map

import android.support.v7.util.DiffUtil
import com.jonasgerdes.stoppelmap.model.map.entity.Image
import com.jonasgerdes.stoppelmap.model.map.entity.Item
import com.jonasgerdes.stoppelmap.model.map.entity.Stall
import com.jonasgerdes.stoppelmap.model.map.entity.Type
import com.jonasgerdes.stoppelmap.model.map.entity.SubType

sealed class StallCard(val contentHash: String) {
    object DiffCallback : DiffUtil.ItemCallback<StallCard>() {
        override fun areItemsTheSame(old: StallCard, new: StallCard) = old.contentHash == new.contentHash

        override fun areContentsTheSame(old: StallCard, new: StallCard) = false //always update

    }
}

data class SingleStallCard(
        val stall: Stall,
        val images: List<Image>,
        val subTypes: List<SubType>,
        val items: List<Item>,
        val type: SubType
) : StallCard(stall.slug)

data class StallCollectionCard(val title: String, val type: Type, val subType: String? = null,
                               val stalls: List<Stall>, val areAdditional:Boolean = false)
    : StallCard(stalls.joinToString { it.slug })