package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.view.View
import com.bumptech.glide.Glide
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult
import com.jonasgerdes.stoppelmap.util.asset.Assets
import kotlinx.android.synthetic.main.map_search_result_single.view.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.07.17
 */

class SingleResultHolder(itemView: View)
    : SearchResultHolder<SingleEntitySearchResult>(itemView) {
    override fun onBind(result: SingleEntitySearchResult) {
        with(itemView) {
            title.text = result.title
            if (result.fromAlias != null) {
                alias.visibility = View.VISIBLE
                alias.text = result.fromAlias
            } else {
                alias.visibility = View.GONE
            }
            iconList.iconTint = R.color.tint_icon_active_dark
            iconList.setIcons(Assets.getIconsFor(result.entity)
                    .filter { i -> i != Assets.NONE })

            Glide.with(context)
                    .load(Assets.getHeadersFor(result.entity)[0])
                    .centerCrop()
                    .into(thumbnail)
        }
    }

}