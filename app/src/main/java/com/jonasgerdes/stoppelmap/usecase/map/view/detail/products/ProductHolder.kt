package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.Price
import com.jonasgerdes.stoppelmap.model.entity.Product
import kotlinx.android.synthetic.main.map_entity_card_products_product.view.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */
class ProductHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(name: String, prices: List<Price>) {
        itemView.product.text = name
        if (prices.isEmpty()) {
            itemView.price.visibility = View.GONE
        } else {
            itemView.price.visibility = View.VISIBLE
            itemView.price.text = itemView.context.getString(
                    R.string.entity_detail_card_products_price,
                    prices[0].price
            )
        }
    }

}