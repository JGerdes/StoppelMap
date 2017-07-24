package com.jonasgerdes.stoppelmap.model.entity.map.search

import com.jonasgerdes.stoppelmap.model.entity.Product
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 24.07.17
 */

class ProductSearchResult(
        val product: Product,
        val icon: Int,
        val entities: List<MapEntity>
) : MapSearchResult(product.name)