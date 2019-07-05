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
        }
    }

    @Parcelize
    class Schedule : Route()

    @Parcelize
    class Transport : Route()

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