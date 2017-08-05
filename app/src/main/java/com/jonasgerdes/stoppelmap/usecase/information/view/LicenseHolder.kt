package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jonasgerdes.stoppelmap.model.entity.License
import kotlinx.android.synthetic.main.information_license.view.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 05.08.17
 */
class LicenseHolder(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    fun onBind(license: License) {
        itemView.title.text = license.title
        itemView.description.text = license.description
    }
}