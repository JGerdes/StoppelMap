package com.jonasgerdes.stoppelmap.util

import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 12.06.17
 */
object Assets {

    val NONE = -1

    fun getTypeIconFor(entity: MapEntity): Int {
        return when (entity.type) {
            Bar.TYPE -> R.drawable.ic_entity_bar_black_16dp
            CandyStall.TYPE -> NONE
            Exhibition.TYPE -> NONE
            FoodStall.TYPE -> R.drawable.ic_entity_food_black_16dp
            GameStall.TYPE -> NONE
            Restroom.TYPE -> R.drawable.ic_entity_restroom_black_16dp
            Ride.TYPE -> NONE
            SellerStall.TYPE -> NONE

            else -> NONE
        }
    }

    fun getIconsFor(entity: MapEntity): List<Int> {
        val icons = ArrayList<Int>()
        if (getTypeIconFor(entity) != NONE) {
            icons.add(getTypeIconFor(entity))
        }

        icons.addAll(when (entity.type) {
            Bar.TYPE -> getIconsFor(entity.bar!!)
            CandyStall.TYPE -> emptyList<Int>()
            Exhibition.TYPE -> emptyList<Int>()
            FoodStall.TYPE -> getIconsFor(entity.foodStall!!)
            GameStall.TYPE -> emptyList<Int>()
            Restroom.TYPE -> emptyList<Int>()
            Ride.TYPE -> emptyList<Int>()
            SellerStall.TYPE -> emptyList<Int>()
            else -> emptyList<Int>()
        });

        return icons
    }

    private fun getIconsFor(foodStall: FoodStall): List<Int> {
        val icons = ArrayList<Int>()
        if (foodStall.isTent) {
            icons.add(R.drawable.ic_entity_tent_black_16dp)
        }
        if (foodStall.isBar) {
            icons.add(R.drawable.ic_entity_bar_black_16dp)
        }
        return icons
    }

    private fun getIconsFor(bar: Bar): List<Int> {
        val icons = ArrayList<Int>()
        if (bar.isTent) {
            icons.add(R.drawable.ic_entity_tent_black_16dp)
        }
        return icons
    }


}