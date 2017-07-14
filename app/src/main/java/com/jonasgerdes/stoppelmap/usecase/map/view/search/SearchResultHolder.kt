package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult
import com.jonasgerdes.stoppelmap.util.Assets
import com.jonasgerdes.stoppelmap.util.setTint
import kotlinx.android.synthetic.main.map_search_result_single.view.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.imageView

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
            with(itemView) {
                title.text = result.title
                if (result.fromAlias != null) {
                    alias.visibility = View.VISIBLE
                    alias.text = result.fromAlias
                } else {
                    alias.visibility = View.GONE
                }
                setIcons(Assets.getIconsFor(result.entity)
                        .filter { i -> i != Assets.NONE })

                Glide.with(context)
                        .load(Assets.getHeadersFor(result.entity)[0])
                        .centerCrop()
                        .into(thumbnail)
            }
        }

        fun setIcons(icons: List<Int>) {
            itemView.iconList.removeAllViews()
            with(itemView.iconList) {
                icons.forEach {
                    imageView(it) {
                        setTint(R.color.tint_icon_active)
                    }.layoutParams = LinearLayout.LayoutParams(dip(16), dip(16))
                }
                visibility = if (icons.isEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }

    }
}