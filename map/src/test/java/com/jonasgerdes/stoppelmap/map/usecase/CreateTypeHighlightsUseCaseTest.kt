package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.data.DEFAULT_STALL
import com.jonasgerdes.stoppelmap.data.StoppelmapDatabaseStub
import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.Highlight
import com.jonasgerdes.stoppelmap.model.map.Item
import com.jonasgerdes.stoppelmap.model.map.SubType
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class CreateTypeHighlightsUseCaseTest {

    val database = StoppelmapDatabaseStub()
    val stallRepository by lazy { StallRepository(database) }
    val getFullStalls = GetFullStallsBySlugUseCase(stallRepository)

    @Test
    fun `own highlight is created for stalls with name`() = runBlocking {
        val createTypeHighlights = CreateTypeHighlightsUseCase(stallRepository, getFullStalls)

        // given
        val type = SubType("candy-stall", "Süßwaren")
        database.empty()
            .with(DEFAULT_STALL.copy(slug = "with_name_1", name = "Eis wie Sahne"), type)
            .with(DEFAULT_STALL.copy(slug = "with_name_2", name = "Eispalast"), type)
            .with(DEFAULT_STALL.copy(slug = "without_name_1"), type)
            .with(DEFAULT_STALL.copy(slug = "without_name_2"), type)
            .with(DEFAULT_STALL.copy(slug = "without_name_3"), type)

        // when
        val result = createTypeHighlights(
            listOf(
                "with_name_1",
                "with_name_2",
                "without_name_1",
                "without_name_2",
                "without_name_3"
            ), "candy-stall"
        )

        // then
        assertEquals(3, result.size)
        assertEquals(1, result.filterIsInstance<Highlight.TypeCollection>().size)
        assertTrue("Stall with name in type collection.",
            result.filterIsInstance<Highlight.TypeCollection>()
                .first().stalls.all {
                it.basicInfo.name == null
            })
        assertTrue("Stall without name has own highlight.",
            result.filterIsInstance<Highlight.SingleStall>()
                .all {
                    it.stall.basicInfo.name != null
                })
    }
}