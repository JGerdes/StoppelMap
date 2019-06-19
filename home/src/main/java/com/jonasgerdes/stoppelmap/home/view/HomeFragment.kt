package com.jonasgerdes.stoppelmap.home.view

import android.os.Bundle
import android.view.View
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.home.R
import com.jonasgerdes.stoppelmap.home.view.item.CountdownItem
import com.jonasgerdes.stoppelmap.home.view.item.SimpleTextItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month

class HomeFragment : BaseFragment<Route.News>(R.layout.fragment_home) {


    private val cardAdapter = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeCards.adapter = cardAdapter

        toolbar.consumeWindowInsetsTop()

        cardAdapter.update(
            listOf(
                SimpleTextItem(R.string.home_card_to_few_cards_info),
                CountdownItem(
                    Duration.between(LocalDateTime.now(), LocalDateTime.of(2019, Month.AUGUST, 15, 18, 0, 0))
                )
            )
        )


    }

}