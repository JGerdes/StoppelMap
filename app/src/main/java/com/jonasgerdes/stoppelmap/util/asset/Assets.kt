package com.jonasgerdes.stoppelmap.util.asset

import android.net.Uri
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.Picture
import com.jonasgerdes.stoppelmap.model.entity.Product
import com.jonasgerdes.stoppelmap.model.entity.map.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 12.06.17
 */
object Assets {

    val NONE = 0
    val PATH_PICTURES = "file:///android_asset/pictures"
    val DIRECTORY_DEFAULT = "default"

    fun getTypeIconFor(entity: MapEntity): Int {
        return when (entity.type) {
            Bar.TYPE -> R.drawable.ic_entity_bar_black_24dp
            CandyStall.TYPE -> R.drawable.ic_entity_candy_black_24dp
            Exhibition.TYPE -> NONE
            FoodStall.TYPE -> R.drawable.ic_entity_food_black_24dp
            GameStall.TYPE -> R.drawable.ic_entity_game_black_24dp
            Restroom.TYPE -> R.drawable.ic_entity_restroom_black_24dp
            Ride.TYPE -> R.drawable.ic_entity_ride_black_24dp
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

    private fun getIconsFor(bar: Bar): List<Int> {
        val icons = ArrayList<Int>()
        if (bar.isTent) {
            icons.add(R.drawable.ic_entity_tent_black_24dp)
        }
        return icons
    }

    private fun getIconsFor(foodStall: FoodStall): List<Int> {
        val icons = ArrayList<Int>()
        if (foodStall.isTent) {
            icons.add(R.drawable.ic_entity_tent_black_24dp)
        }
        if (foodStall.isBar) {
            icons.add(R.drawable.ic_entity_bar_black_24dp)
        }
        return icons
    }

    fun getHeadersFor(entity: MapEntity): List<Uri> {
        val headers = entity.getPictures(Picture.TYPE_HEADER)
        return if (!headers.isEmpty()) {
            headers.map {
                PATH_PICTURES + "/${entity.type}/${it.filePath}"
            }.map { Uri.parse(it) }
        } else {
            listOf(getDefaultHeaderFor(entity))
        }
    }

    private fun getDefaultHeaderFor(entity: MapEntity): Uri {
        return Uri.parse(
                when (entity.type) {
                    FoodStall.TYPE -> {
                        if (entity.foodStall!!.isBar) {
                            PATH_PICTURES + "/$DIRECTORY_DEFAULT/hybrid.png"
                        } else {
                            PATH_PICTURES + "/$DIRECTORY_DEFAULT/food-stall.png"
                        }
                    }
                    Bar.TYPE, Ride.TYPE, CandyStall.TYPE, GameStall.TYPE,
                    Restroom.TYPE -> PATH_PICTURES + "/$DIRECTORY_DEFAULT/${entity.type}.png"
                    else -> PATH_PICTURES + "/$DIRECTORY_DEFAULT/default.png"
                })
    }

    fun getIconsFor(product: Product): Int {
        return when {
            product.name.startsWith("dish_type") -> R.drawable.ic_entity_food_black_24dp
            product.name.startsWith("candy_type") -> R.drawable.ic_entity_candy_black_24dp
            product.name.startsWith("drink_type") -> R.drawable.ic_entity_bar_black_24dp
            product.name.startsWith("game_type") -> R.drawable.ic_entity_game_black_24dp
            else -> NONE
        }
    }


}