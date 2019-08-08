package com.jonasgerdes.stoppelmap.events.view

import androidx.core.view.isVisible
import com.jonasgerdes.androidutil.htmlToSpanned
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.events.R
import com.jonasgerdes.stoppelmap.events.entity.Event
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_event_card.view.*
import org.threeten.bp.format.DateTimeFormatter

val TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm")

data class EventItem(val event: Event) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            itemView.title.text = event.title
            itemView.time.text = event.start.format(TIME_FORMAT)

            if (event.location != null) {
                itemView.toLocationButtom.isVisible = true
                itemView.location.isVisible = true
                itemView.location.text = event.location.name
            } else {
                itemView.toLocationButtom.isVisible = false
                itemView.location.isVisible = false
            }

            val description = event.description?.let {
                val htmlSpanned = htmlToSpanned(event.description)
                if (htmlSpanned.toString().isBlank()) null else htmlSpanned
            }

            if (description != null) {
                itemView.description.isVisible = true
                itemView.description.text = description
            } else {
                itemView.description.isVisible = false
            }

            itemView.location.setOnClickListener {
                openLocation()
            }

            itemView.toLocationButtom.setOnClickListener {
                openLocation()
            }
        }
    }

    private fun openLocation() = event.location?.let { stall ->
        //TODO: investigate why this is needed (otherwise first opening of map fragments is overwritten by idle)
        Router.switchToDestination(Router.Destination.MAP)
        Router.navigateToRoute(
            Route.Map(Route.Map.State.Carousel(Route.Map.State.Carousel.StallCollection.Single(stall.slug))),
            Router.Destination.MAP
        )
    }

    override fun getLayout() = R.layout.item_event_card

}