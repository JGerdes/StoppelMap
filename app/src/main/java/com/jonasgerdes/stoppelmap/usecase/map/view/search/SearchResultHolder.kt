package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult
import com.jonasgerdes.stoppelmap.util.Assets
import com.jonasgerdes.stoppelmap.util.getDrawableCompat
import com.jonasgerdes.stoppelmap.util.setTint
import kotlinx.android.synthetic.main.map_search_result_single.view.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 06.07.17
 */
abstract class SearchResultHolder<in E : MapSearchResult>(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(result: E)

    class SingleResult(itemView: View)
        : SearchResultHolder<SingleEntitySearchResult>(itemView) {
        override fun onBind(result: SingleEntitySearchResult) {
            itemView.title.text = result.title
            if (result.fromAlias != null) {
                itemView.alias.visibility = View.VISIBLE
                itemView.alias.text = result.fromAlias
            } else {
                itemView.alias.visibility = View.GONE
            }
            setIcons(listOf(Assets.getIconFor(result.entity))
                    .filter { i -> i != Assets.NONE }
                    .map {
                        itemView.context.getDrawableCompat(it)
                    })
        }

        fun setIcons(icons: List<Drawable>) {
            itemView.iconList.removeAllViews()
            icons.map {
                val view = ImageView(itemView.context)
                view.setImageDrawable(it)
                view.setTint(R.color.tint_icon_active)
                view
            }.forEach {
                itemView.iconList.addView(it)
            }
            itemView.iconList.visibility = if (icons.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

    }
}