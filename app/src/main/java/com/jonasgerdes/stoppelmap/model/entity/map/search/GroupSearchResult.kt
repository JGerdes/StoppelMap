package com.jonasgerdes.stoppelmap.model.entity.map.search

import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 03.08.17
 */

class GroupSearchResult(
        title: String,
        val icon: Int,
        val entities: List<MapEntity>
) : MapSearchResult(title)