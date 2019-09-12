package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.DEFAULT_STALL
import com.jonasgerdes.stoppelmap.data.StoppelmapDatabaseStub
import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.model.map.SubType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetFullStallsBySlugUseCaseTest {

    val database = StoppelmapDatabaseStub()
    val stallRepository by lazy { StallRepository(database) }

    @Test
    fun `returned stalls have correct slugs`() = runBlocking {
        val getFullStalls = GetFullStallsBySlugUseCase(stallRepository)
        val slug = "test-slug"

        // given
        database.empty().with(DEFAULT_STALL.copy(slug = slug))
        // when
        val result = getFullStalls(listOf(slug))
        //then
        assertEquals(slug, result.first().basicInfo.slug)
    }

    @Test
    fun `returned stalls have correct subtypes`() = runBlocking {
        val getFullStalls = GetFullStallsBySlugUseCase(stallRepository)
        val slug = "test-slug"

        // given
        database.empty()
            .with(
                DEFAULT_STALL.copy(slug = slug),
                SubType("type1", "type1"),
                SubType("type2", "type2")
            )
            .with(
                DEFAULT_STALL.copy(slug = "stall2"),
                SubType("type3", "type3"),
                SubType("type4", "type4")
            )
        // when
        val result = getFullStalls(listOf(slug))
        //then
        assertEquals(2, result.first().subTypes.size)
        assertEquals("type1", result.first().subTypes[0].slug)
        assertEquals("type2", result.first().subTypes[1].slug)
    }
}
