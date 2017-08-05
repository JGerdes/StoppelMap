package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.License

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 05.08.17
 */
class LicenseAdapter : RecyclerView.Adapter<LicenseHolder>() {

    private var licenseList: List<License> = ArrayList()

    var licenses
        get() = this.licenseList
        set(value) {
            licenseList = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LicenseHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.information_license, parent, false)
        return LicenseHolder(view)
    }

    override fun onBindViewHolder(holder: LicenseHolder, position: Int) {
        holder.onBind(licenseList[position])
    }

    override fun getItemCount(): Int {
        return licenseList.size
    }
}