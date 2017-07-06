package com.jonasgerdes.stoppelmap.model.entity.map.search

import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 06.07.17
 */

class SingleEntitySearchResult(
        title: String,
        val entity: MapEntity
) : MapSearchResult(title)