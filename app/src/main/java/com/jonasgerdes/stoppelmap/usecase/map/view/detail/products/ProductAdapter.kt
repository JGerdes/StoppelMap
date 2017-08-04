package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.Product
import com.jonasgerdes.stoppelmap.util.asset.StringResourceHelper
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */
class ProductAdapter : RecyclerView.Adapter<ProductHolder>() {

    private var productList: List<Product> = ArrayList()
    @Inject lateinit var stringHelper: StringResourceHelper

    init {
        App.graph.inject(this)
    }

    var products
        get() = productList
        set(value) {
            productList = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.map_entity_card_products_product, parent, false)
        return ProductHolder(view)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        with(productList[position]) {
            holder.onBind(stringHelper.getNameFor(this), prices)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}