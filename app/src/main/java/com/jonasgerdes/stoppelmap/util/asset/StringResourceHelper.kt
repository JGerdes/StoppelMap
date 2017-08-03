package com.jonasgerdes.stoppelmap.util.asset

import android.content.Context
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.Product
import com.jonasgerdes.stoppelmap.model.entity.map.*
import com.jonasgerdes.stoppelmap.util.asset.Assets.NONE

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.07.17
 */

class StringResourceHelper(val context: Context) {

    val cache = HashMap<String, String>()

    fun getNameFor(product: Product?): String {
        if (product == null) {
            return ""
        }
        if (cache.containsKey(product.name)) {
            return cache[product.name]!!
        }
        val resId = context.resources.getIdentifier(product.name, "string", context.packageName)
        val name = if (resId != NONE) {
            context.getString(resId)
        } else {
            product.name
        }
        cache[product.name] = name
        return name
    }

    fun resetCache() {
        cache.clear()
    }


    fun getTitleFor(entity: MapEntity): String {
        return entity.name ?: when (entity.type) {
            Bar.TYPE -> entity.operator ?: ""
            FoodStall.TYPE -> getNameFor(entity.foodStall!!.dishes.firstOrNull())
            CandyStall.TYPE -> getNameFor(entity.candyStall!!.products.firstOrNull())
            GameStall.TYPE -> getNameFor(entity.gameStall!!.games.firstOrNull())
            Restroom.TYPE -> {
                /*if (entity.restroom!!.forMen && !entity.restroom!!.forWomen) {
                    context.getString(R.string.restroom_men_only)
                } else if (!entity.restroom!!.forMen && entity.restroom!!.forWomen) {
                    context.getString(R.string.restroom_women_only)
                } else if (entity.restroom!!.forDisabled) {
                    context.getString(R.string.restroom_for_disabled)
                } else { */
                context.getString(R.string.restroom_generic)
                //}
            }
            else -> ""
        }
    }
}
