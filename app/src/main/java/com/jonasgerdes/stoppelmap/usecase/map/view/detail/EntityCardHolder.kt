package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jonasgerdes.stoppelmap.model.entity.map.detail.EntityDetailCard

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */
abstract class EntityCardHolder<in E : EntityDetailCard>(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(card: E)
}