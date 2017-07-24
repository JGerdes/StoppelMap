package com.jonasgerdes.stoppelmap.util.asset

import android.content.Context
import com.jonasgerdes.stoppelmap.model.entity.Product
import com.jonasgerdes.stoppelmap.util.asset.Assets.NONE

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.07.17
 */

class StringResourceHelper(val context: Context) {

    val cache = HashMap<String, String>()

    fun getNameFor(product: Product): String {
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
}
