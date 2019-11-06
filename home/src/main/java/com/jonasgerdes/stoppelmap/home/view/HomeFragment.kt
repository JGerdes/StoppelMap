package com.jonasgerdes.stoppelmap.home.view

import android.os.Bundle
import android.view.View
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.core.di.viewModelFactory
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.home.R
import com.jonasgerdes.stoppelmap.home.view.item.CountdownItem
import com.jonasgerdes.stoppelmap.home.view.item.SimpleTextItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<Route.Home>(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModelFactory()

    private val cardAdapter = GroupAdapter<ViewHolder>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeCards.adapter = cardAdapter

        toolbar.consumeWindowInsetsTop()


        observe(viewModel.cards) { cards ->
            cardAdapter.update(
                mapCards(cards)
            )
        }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settings_about -> Router.navigateToRoute(
                    Route.About(),
                    Router.Destination.HOME
                )
            }
            true
        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.startUpdates()
    }

    override fun onPause() {
        viewModel.stopUpdates()
        super.onPause()
    }

    private fun mapCards(cards: List<HomeCard>) = cards.map { card ->
        when (card) {
            MoreCardsInfoCard -> SimpleTextItem(R.string.home_card_to_few_cards_info)
            is CountdownCard -> CountdownItem(card.duration)
        }
    }

}