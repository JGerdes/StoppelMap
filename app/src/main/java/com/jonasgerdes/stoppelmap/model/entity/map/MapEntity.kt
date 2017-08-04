package com.jonasgerdes.stoppelmap.model.entity.map

import com.jonasgerdes.stoppelmap.model.entity.Description
import com.jonasgerdes.stoppelmap.model.entity.GeoLocation
import com.jonasgerdes.stoppelmap.model.entity.PhoneNumber
import com.jonasgerdes.stoppelmap.model.entity.Picture
import com.jonasgerdes.stoppelmap.model.realm_wrapper.RealmString
import io.realm.RealmList
import io.realm.RealmObject


/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 13.06.2017
 */
open class MapEntity : RealmObject() {

    companion object{
        val TYPE_MISC = "misc"
    }

    var slug: String? = null
    var type: String? = null
    lateinit var center: GeoLocation
    var bounds: RealmList<GeoLocation> = RealmList()
    var zoomLevel = 20f

    var name: String? = null
    var alias: RealmList<RealmString> = RealmList()
    var operator: String? = null
    var description: Description? = null
    var website: String? = null
    var phoneNumbers: RealmList<PhoneNumber> = RealmList()
    var pictures: RealmList<Picture> = RealmList()

    var bar: Bar? = null
    var ride: Ride? = null
    var foodStall: FoodStall? = null
    var candyStall: CandyStall? = null
    var gameStall: GameStall? = null
    var restroom: Restroom? = null
    var sellerStall: SellerStall? = null
    var exhibition: Exhibition? = null

    class MapEntities : ArrayList<MapEntity>()
}