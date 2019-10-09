package com.jonasgerdes.stoppelmap.data.dao

import com.jonasgerdes.stoppelmap.data.like
import com.jonasgerdes.stoppelmap.model.map.SubType

class StallTypeDaoStub(private val types: MutableList<SubType>) : StallTypeDao() {
    override suspend fun findTypeByName(name: String): List<SubType> = types.like(name) { it.name }

    override suspend fun getTypeBySlug(slug: String): SubType? = types.first { it.slug == slug }
}