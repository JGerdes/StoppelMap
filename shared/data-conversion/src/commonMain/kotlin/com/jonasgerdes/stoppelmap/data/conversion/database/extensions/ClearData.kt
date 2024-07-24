package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase

internal fun StoppelMapDatabase.clearData() {
    aliasQueries.clear()
    departureQueries.clear()
    departure_dayQueries.clear()
    eventQueries.clear()
    event_personQueries.clear()
    feeQueries.clear()
    imageQueries.clear()
    localized_stringQueries.clear()
    locationQueries.clear()
    map_entityQueries.clear()
    map_entity_imageQueries.clear()
    map_entity_serviceQueries.clear()
    map_entity_tagQueries.clear()
    metadataQueries.clear()
    offerQueries.clear()
    operatorQueries.clear()
    personQueries.clear()
    person_imageQueries.clear()
    phone_numberQueries.clear()
    productQueries.clear()
    routeQueries.clear()
    serviceQueries.clear()
    stationQueries.clear()
    sub_typeQueries.clear()
    tagQueries.clear()
    websiteQueries.clear()
}