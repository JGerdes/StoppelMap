package com.jonasgerdes.stoppelmap.util

import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 12.06.17
 */
object Assets {

    val NONE = -1;

    fun getIconFor(entity: MapEntity): Int {
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
}