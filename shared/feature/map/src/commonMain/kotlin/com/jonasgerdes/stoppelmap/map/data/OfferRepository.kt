package com.jonasgerdes.stoppelmap.map.data

import com.jonasgerdes.stoppelmap.data.shared.OfferQueries
import com.jonasgerdes.stoppelmap.data.shared.ProductQueries
import com.jonasgerdes.stoppelmap.map.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class OfferRepository(
    private val productQueries: ProductQueries,
    private val offerQueries: OfferQueries,
) {

    suspend fun searchProducts(query: String): List<Product> = withContext(Dispatchers.IO) {
        productQueries.searchByName(query, ::Product).executeAsList() +
                productQueries.searchByAlias(query, ::Product).executeAsList()
    }

    suspend fun getMapEntitiesOffering(productSlugs: Set<String>) = withContext(Dispatchers.IO) {
        offerQueries.getEntitiesOfferingProducts(productSlugs).executeAsList()
    }
}