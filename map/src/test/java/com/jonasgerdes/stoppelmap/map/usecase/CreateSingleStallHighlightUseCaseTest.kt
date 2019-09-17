package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.DEFAULT_STALL
import com.jonasgerdes.stoppelmap.data.StoppelmapDatabaseStub
import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.Highlight
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreateSingleStallHighlightUseCaseTest {

    val database = StoppelmapDatabaseStub()
    val stallRepository by lazy { StallRepository(database) }
    val getFullStalls = GetFullStallsBySlugUseCase(stallRepository)

    @Test
    fun `highlight with name is created for stall with name`() = runBlocking {
        val createSingleStall = CreateSingleStallHighlightUseCase(stallRepository, getFullStalls)
        val slug = "niedersachsen-halle"
        // given
        database.empty().with(DEFAULT_STALL.copy(slug = slug, name = "Kühlings Niedersachsenhalle"))

        // when
        val result = createSingleStall(slug)

        // then
        assertTrue("Created highlight isn't single stall", result is Highlight.SingleStall)
    }

    @Test
    fun `highlight with no name is created for stall without name`() = runBlocking {
        val createSingleStall = CreateSingleStallHighlightUseCase(stallRepository, getFullStalls)
        val slug = "slush-ice-12"
        // given
        database.empty().with(DEFAULT_STALL.copy(slug = slug))

        // when
        val result = createSingleStall(slug)

        // then
        assertTrue(
            "Created highlight isn't nameless stall",
            result is Highlight.NamelessStall
        )
    }
}