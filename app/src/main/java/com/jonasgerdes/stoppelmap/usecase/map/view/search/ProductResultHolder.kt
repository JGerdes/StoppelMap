package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.view.View
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.model.entity.map.search.ProductSearchResult
import com.jonasgerdes.stoppelmap.util.asset.Assets
import com.jonasgerdes.stoppelmap.util.asset.StringResourceHelper
import kotlinx.android.synthetic.main.map_search_result_product.view.*
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.07.17
 */

class ProductResultHolder(itemView: View)
    : SearchResultHolder<ProductSearchResult>(itemView) {

    @Inject
    lateinit var stringHelper: StringResourceHelper

    init {
        App.graph.inject(this)
    }

    override fun onBind(result: ProductSearchResult) {
        with(itemView) {
            title.text = stringHelper.getNameFor(result.product)
            if (result.icon != Assets.NONE) {
                icon.setImageResource(result.icon)
                icon.visibility = View.VISIBLE
            } else {
                icon.visibility = View.INVISIBLE
            }
        }
    }

}