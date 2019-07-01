package com.jonasgerdes.stoppelmap.about.view

import android.os.Bundle
import android.view.View
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.about.*
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.androidutil.versionName
import com.jonasgerdes.stoppelmap.about.data.contributors
import com.jonasgerdes.stoppelmap.about.data.illustrators
import com.jonasgerdes.stoppelmap.about.data.libraries
import com.jonasgerdes.stoppelmap.about.data.sources
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : BaseFragment<Route.Home>(R.layout.fragment_about) {

    private val cardAdapter by lazy {
        GroupAdapter<ViewHolder>().apply {
            add(Section().apply {
                add(VersionItem(context?.versionName ?: ""))
                add(TextItem(text = getString(R.string.about_open_source)))
            })
            add(Section().apply {
                setHeader(HeaderItem(getString(R.string.about_disclaimer_title)))
                add(
                    TextItem(
                        text = getString(R.string.about_disclaimer_text)
                    )
                )
            })
            add(Section().apply {
                setHeader(HeaderItem(getString(R.string.about_contributors_title)))
                addAll(contributors)
            })
            add(Section().apply {
                setHeader(HeaderItem(getString(R.string.about_graphics_title)))
                addAll(illustrators)
            })
            add(Section().apply {
                setHeader(HeaderItem(getString(R.string.about_sources_title)))
                addAll(sources)
            })
            add(Section().apply {
                setHeader(HeaderItem(getString(R.string.about_libraries_title)))
                addAll(libraries.sortedBy { it.name })
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.consumeWindowInsetsTop()


        toolbar.setNavigationOnClickListener {
            Router.navigateBack()
        }

        aboutCards.adapter = cardAdapter
    }
}