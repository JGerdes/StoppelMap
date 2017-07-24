package com.jonasgerdes.stoppelmap.util.asset

import android.content.Context
import com.jonasgerdes.stoppelmap.model.entity.Product
import com.jonasgerdes.stoppelmap.util.asset.Assets.NONE

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.07.17
 */

class StringResourceHelper(val context: Context) {
    
    fun getNameFor(product: Product): String {
        val resId = context.resources.getIdentifier(product.name, "string", context.packageName)
        return if (resId != NONE) {
            context.getString(resId)
        } else {
            product.name
        }
    }
}
