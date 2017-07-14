package com.jonasgerdes.stoppelmap.model.entity.map

import com.jonasgerdes.stoppelmap.model.entity.Picture

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 14.07.17
 */

fun MapEntity.getPictures(type: String): List<Picture> {
    return pictures.filter { it.type == type }
}