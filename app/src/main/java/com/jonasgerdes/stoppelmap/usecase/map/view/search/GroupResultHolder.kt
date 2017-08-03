package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.view.View
import com.jonasgerdes.stoppelmap.model.entity.map.search.GroupSearchResult
import com.jonasgerdes.stoppelmap.util.asset.Assets
import kotlinx.android.synthetic.main.map_search_result_product.view.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 03.08.17
 */

class GroupResultHolder(itemView: View)
    : SearchResultHolder<GroupSearchResult>(itemView) {

    override fun onBind(result: GroupSearchResult) {
        with(itemView) {
            title.text = result.title
            if (result.icon != Assets.NONE) {
                icon.setImageResource(result.icon)
                icon.visibility = View.VISIBLE
            } else {
                icon.visibility = View.INVISIBLE
            }
        }
    }

}