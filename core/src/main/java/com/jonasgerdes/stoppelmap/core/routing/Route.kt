package com.jonasgerdes.stoppelmap.core.routing

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


abstract class Route : Parcelable {
    @Parcelize
    class Home : Route()

    @Parcelize
    class Map : Route()

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