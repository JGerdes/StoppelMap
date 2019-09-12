package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.DEFAULT_STALL
import com.jonasgerdes.stoppelmap.data.StoppelmapDatabaseStub
import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.model.map.Alias
import com.jonasgerdes.stoppelmap.model.map.Item
import com.jonasgerdes.stoppelmap.model.map.SubType
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchForStallsUseCaseTest {

    val database = StoppelmapDatabaseStub()
    val stallRepository by lazy { StallRepository(database) }

    @Test
    fun `stall is found by name`() = runBlocking {
        val searchForStalls = SearchForStallsUseCase(stallRepository)
        val slug = "kuelings-niedersachsenhalle"

        // given
        database.empty().with(DEFAULT_STALL.copy(slug = slug, name = "Kühlings Niedersachsenhalle"))
        // when
        val result = searchForStalls("Niedersachsen")
        // then
        assertTrue("Result doesn't contain $slug", result.any { it.stallSlugs.contains(slug) })
    }

    @Test
    fun `stall is not found by wrong name`() = runBlocking {
        val searchForStalls = SearchForStallsUseCase(stallRepository)
        val slug = "kuelings-niedersachsenhalle"

        // given
        database.empty().with(DEFAULT_STALL.copy(slug = slug, name = "Kühlings Niedersachsenhalle"))
        // when
        val result = searchForStalls("Brackmann")
        // then
        assertTrue("Result does contain $slug", result.none { it.stallSlugs.contains(slug) })
    }

    @Test
    fun `stall is found by type`() = runBlocking {
        val searchForStalls = SearchForStallsUseCase(stallRepository)
        val slug = "kuelings-niedersachsenhalle"

        // given
        database.empty().with(
            DEFAULT_STALL.copy(
                slug = slug,
                name = "Kühlings Niedersachsenhalle"
            ),
            SubType("tent", "Festzelt")
        )
        // when
        val result = searchForStalls("zelt")
        // then
        assertTrue("Result doesn't contain $slug", result.any { it.stallSlugs.contains(slug) })
    }

    @Test
    fun `stall is not found by wrong type`() = runBlocking {
        val searchForStalls = SearchForStallsUseCase(stallRepository)
        val slug = "kuelings-niedersachsenhalle"

        // given
        database.empty().with(
            DEFAULT_STALL.copy(
                slug = slug,
                name = "Kühlings Niedersachsenhalle"
            ),
            SubType("tent", "Festzelt")
        )
        // when
        val result = searchForStalls("achterbahn")
        // then
        assertTrue("Result does contain $slug", result.none { it.stallSlugs.contains(slug) })
    }

    @Test
    fun `stall is found by item`() = runBlocking {
        val searchForStalls = SearchForStallsUseCase(stallRepository)
        val slug = "kuelings-niedersachsenhalle"

        // given
        database.empty().with(
            DEFAULT_STALL.copy(
                slug = slug,
                name = "Kühlings Niedersachsenhalle"
            ),
            Item("beer", "Bier")
        )
        // when
        val result = searchForStalls("bier")
        // then
        assertTrue("Result doesn't contain $slug", result.any { it.stallSlugs.contains(slug) })
    }

    @Test
    fun `stall is not found by wrong item`() = runBlocking {
        val searchForStalls = SearchForStallsUseCase(stallRepository)
        val slug = "kuelings-niedersachsenhalle"

        // given
        database.empty().with(
            DEFAULT_STALL.copy(
                slug = slug,
                name = "Kühlings Niedersachsenhalle"
            ),
            Item("beer", "Bier")
        )
        // when
        val result = searchForStalls("zuckerwatte")
        // then
        assertTrue("Result does contain $slug", result.none { it.stallSlugs.contains(slug) })
    }

    @Test
    fun `stall is found by alias`() = runBlocking {
        val searchForStalls = SearchForStallsUseCase(stallRepository)
        val slug = "pickers"

        // given
        database.empty().with(
            DEFAULT_STALL.copy(
                slug = slug,
                name = "Pickers"
            )
        ).with(Alias(slug, "Luttener Zelt"))
        // when
        val result = searchForStalls("lutten")
        // then
        assertTrue("Result doesn't contain $slug", result.any { it.stallSlugs.contains(slug) })
    }

    @Test
    fun `stall is not found by wrong alias`() = runBlocking {
        val searchForStalls = SearchForStallsUseCase(stallRepository)
        val slug = "pickers"

        // given
        database.empty().with(
            DEFAULT_STALL.copy(
                slug = slug,
                name = "Pickers"
            )
        ).with(Alias(slug, "Luttener Zelt"))
        // when
        val result = searchForStalls("lohne")
        // then
        assertTrue("Result does contain $slug", result.none { it.stallSlugs.contains(slug) })
    }
}
