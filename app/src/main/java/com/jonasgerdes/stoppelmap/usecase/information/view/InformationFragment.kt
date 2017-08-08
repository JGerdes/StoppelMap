package com.jonasgerdes.stoppelmap.usecase.information.view

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.License
import com.jonasgerdes.stoppelmap.usecase.map.view.search.LicenseAdapter
import com.jonasgerdes.stoppelmap.util.versioning.VersionHelper
import kotlinx.android.synthetic.main.information_fragment.*
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
class InformationFragment : LifecycleFragment() {

    @Inject
    protected lateinit var versionHelper: VersionHelper

    init {
        App.graph.inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.information_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LicenseAdapter()
        licenses.adapter = adapter
        versionName.text = "v${versionHelper.versionName}"
        with(context) {
            adapter.licenses = listOf(
                    License(getString(R.string.license_support_title),
                            getString(R.string.license_support_body)
                    ),
                    License(getString(R.string.license_kotlin_title),
                            getString(R.string.license_kotlin_body)
                    ),
                    License(getString(R.string.license_anko_title),
                            getString(R.string.license_anko_body)
                    ),
                    License(getString(R.string.license_dagger_title),
                            getString(R.string.license_dagger_body)
                    ),
                    License(getString(R.string.license_glide_title),
                            getString(R.string.license_glide_body)
                    ),
                    License(getString(R.string.license_gson_title),
                            getString(R.string.license_gson_body)
                    ),
                    License(getString(R.string.license_okhttp_title),
                            getString(R.string.license_okhttp_body)
                    ),
                    License(getString(R.string.license_realm_core_title),
                            getString(R.string.license_realm_core_body)
                    ),
                    License(getString(R.string.license_realm_java_title),
                            getString(R.string.license_realm_java_body)
                    ),
                    License(getString(R.string.license_realmbrowser_title),
                            getString(R.string.license_realmbrowser_body)
                    ),
                    License(getString(R.string.license_rxjava_title),
                            getString(R.string.license_rxjava_body)
                    ),
                    License(getString(R.string.license_rxandroid_title),
                            getString(R.string.license_rxandroid_body)
                    ),
                    License(getString(R.string.license_rxbindings_title),
                            getString(R.string.license_rxbindings_body)
                    ),
                    License(getString(R.string.license_rxpermissions_title),
                            getString(R.string.license_rxpermissions_body)
                    ),
                    License(getString(R.string.license_material_icons_title),
                            getString(R.string.license_material_icons_body)
                    ))
        }
    }
}