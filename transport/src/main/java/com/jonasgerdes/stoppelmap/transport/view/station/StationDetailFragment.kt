package com.jonasgerdes.stoppelmap.transport.view.station

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.transport.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_option_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class StationDetailFragment : Fragment(R.layout.fragment_station_detail) {

    val departureAdapter = GroupAdapter<ViewHolder>()

    val title: String? by lazy { arguments!!.getString(ARGUMENT_TITLE) }
    val slug: String by lazy { arguments!!.getString(ARGUMENT_SLUG) }

    val viewModel: StationDetailViewModel by viewModel { parametersOf(slug) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.consumeWindowInsetsTop()
        if (title != null) toolbar.title = title
        toolbar.setNavigationOnClickListener {
            Router.navigateBack()
        }

        observe(viewModel.station) { station ->
            toolbar.title = station.basicInfo.name
        }

    }

    companion object {
        private const val ARGUMENT_SLUG = "argument_slug"
        private const val ARGUMENT_TITLE = "argument_title"
        fun newInstance(slug: String, title: String? = null) = StationDetailFragment().apply {
            arguments = bundleOf(
                ARGUMENT_SLUG to slug,
                ARGUMENT_TITLE to title
            )
        }
    }
}