package com.jonasgerdes.stoppelmap.data.dao

import com.jonasgerdes.stoppelmap.data.like
import com.jonasgerdes.stoppelmap.model.map.Alias
import com.jonasgerdes.stoppelmap.model.map.Item
import com.jonasgerdes.stoppelmap.model.map.Stall
import com.jonasgerdes.stoppelmap.model.map.SubType

class StallDaoStub(
    private val stalls: MutableList<Stall>,
    private val aliases: MutableList<Alias>,
    private val stallItems: MutableList<Pair<Stall, Item>>,
    private val stallTypes: MutableList<Pair<Stall, SubType>>
) : StallDao() {
    override suspend fun findStallByName(name: String): List<Stall> = stalls.like(name) { it.name }
    override suspend fun findStallBySlug(slug: String): Stall? = stalls.first { it.slug == slug }
    override suspend fun findAlias(alias: String): List<Alias> = aliases.like(alias) { it.alias }
    override suspend fun getStallsByType(type: String): List<Stall> =
        stalls.filter { it.type.type == type }

    override suspend fun getStallsBySubType(subType: String): List<Stall> =
        stallTypes.filter { it.second.slug == subType }.map { it.first }

    override suspend fun getStallsByItem(item: String): List<Stall> =
        stallItems.filter { it.second.slug == item }.map { it.first }

    override suspend fun getSubTypesByStall(stall: String): List<SubType> =
        stallTypes.filter { it.first.slug == stall }.map { it.second }
}