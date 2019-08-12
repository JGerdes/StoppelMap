package com.jonasgerdes.stoppelmap.core.routing

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


abstract class Route : Parcelable {
    @Parcelize
    class Home : Route()

    @Parcelize
    data class Map(
        val state: State
    ) : Route() {
        sealed class State : Parcelable {
            @Parcelize
            class Idle() : State()

            @Parcelize
            class Search() : State()

            @Parcelize
            data class Carousel(val stallCollection: StallCollection) : State() {
                sealed class StallCollection : Parcelable {
                    @Parcelize
                    data class Single(val stallSlug: String) : StallCollection()

                    @Parcelize
                    data class TypeCollection(val type: String, val stallSlugs: List<String>) : StallCollection()

                    @Parcelize
                    data class ItemCollection(val item: String, val stallSlugs: List<String>) : StallCollection()
                }
            }
        }
    }

    @Parcelize
    class Schedule : Route()

    @Parcelize
    class Transport(
        val state: State
    ) : Route() {
        sealed class State : Parcelable {
            @Parcelize
            class OptionsList : State()

            @Parcelize
            data class RouteDetail(val route: String, val title: String? = null) : State()

            @Parcelize
            data class StationDetail(val station: String, val title: String? = null) : State()
        }
    }

    @Parcelize
    data class News(val forceReload: Boolean = false, val scrollToTop: Boolean = false) : Route()

    @Parcelize
    class About : Route()

}

sealed class HomeDetail : Parcelable {
    @Parcelize
    class About : HomeDetail()
}

infix fun Route.to(destination: Router.Destination) = Journey(this, destination)

data class Journey(
    val route: Route,
    val destination: Router.Destination
)