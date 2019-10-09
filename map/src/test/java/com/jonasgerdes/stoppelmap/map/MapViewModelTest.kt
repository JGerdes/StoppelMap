package com.jonasgerdes.stoppelmap.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jonasgerdes.stoppelmap.core.domain.GlobalInfoProvider
import com.jonasgerdes.stoppelmap.core.domain.LocationProvider
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.data.DEFAULT_STALL
import com.jonasgerdes.stoppelmap.data.StoppelmapDatabaseStub
import com.jonasgerdes.stoppelmap.data.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.entity.*
import com.jonasgerdes.stoppelmap.map.usecase.*
import com.jonasgerdes.stoppelmap.map.view.MapViewModel
import com.jonasgerdes.stoppelmap.model.map.Item
import com.jonasgerdes.stoppelmap.model.map.SubType
import com.jonasgerdes.stoppelmap.testutil.CoroutinesTestRule
import com.jonasgerdes.stoppelmap.testutil.test
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.stub
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test

private val AREA = GlobalInfoProvider.AreaBounds(
    northLatitude = 52.7494011815008,
    southLatitude = 52.7429499584193,
    westLongitude = 8.28653654801576,
    eastLongitude = 8.30059127365977
)

private val LOCATION_OUTSIDE = LocationProvider.Location(52.75, 8.31)
private val LOCATION_INSIDE = LocationProvider.Location(52.744, 8.29)

@ExperimentalCoroutinesApi
class MapViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val coroutineTestRule = CoroutinesTestRule(testDispatcher)

    val database = StoppelmapDatabaseStub()
    val locationProvider = mock<LocationProvider> { }
    val globalInfoProvider = mock<GlobalInfoProvider> {
        on { this.getAreaBounds() } doReturn AREA
    }
    val stallRepository by lazy { StallRepository(database) }

    val searchForStallsUseCase = SearchForStallsUseCase(stallRepository)
    val getStallsBySlugUseCase = GetFullStallsBySlugUseCase(stallRepository)
    val createSingleStallHighlightUseCase =
        CreateSingleStallHighlightUseCase(stallRepository, getStallsBySlugUseCase)
    val createTypeHighlightsUseCase =
        CreateTypeHighlightsUseCase(stallRepository, getStallsBySlugUseCase)
    val createItemHighlightsUseCase =
        CreateItemHighlightsUseCase(stallRepository, getStallsBySlugUseCase)
    val getUserLocationUseCase = GetUserLocationUseCase(locationProvider)
    val isUserInAreaUseCase = IsUserInAreaUseCase(globalInfoProvider)


    val viewModel by lazy {
        MapViewModel(
            searchForStalls = searchForStallsUseCase,
            getStallsBySlug = getStallsBySlugUseCase,
            createSingleStallHighlight = createSingleStallHighlightUseCase,
            createTypeHighlights = createTypeHighlightsUseCase,
            createItemHighlights = createItemHighlightsUseCase,
            getUserLocation = getUserLocationUseCase,
            isUserInArea = isUserInAreaUseCase
        )
    }


    @Test
    fun `center on user request leads to error message if user not in area`() {
        // given
        locationProvider.stub {
            onBlocking { invoke() } doReturn LOCATION_OUTSIDE
        }
        // when
        viewModel.onCenterOnUserTriggered()

        // then
        assertEquals(viewModel.message.value, Message.NotInArea)
    }

    @Test
    fun `map is centered to user location if on center user is requested`() {
        // given
        locationProvider.stub {
            onBlocking { invoke() } doReturn LOCATION_INSIDE
        }
        // when
        viewModel.onCenterOnUserTriggered()

        // then
        assertEquals(
            MapFocus.One(
                Location(
                    longitude = LOCATION_INSIDE.longitude,
                    latitude = LOCATION_INSIDE.latitude
                )
            ),
            viewModel.mapFocus.value
        )
    }

    @Test
    fun `empty search finds nothing`() = runBlockingTest {
        // given
        database.with(
            DEFAULT_STALL.copy(
                name = ""
            ),
            DEFAULT_STALL.copy(
                name = " "
            )
        )
        // when
        viewModel.onSearchEntered("")
        testDispatcher.advanceUntilIdle()

        // then
        val results = viewModel.searchResults.value!!
        assertEquals(0, results.size)
    }

    @Test
    fun `search queries are debounced`() = runBlockingTest {
        val testObserver = viewModel.searchResults.test()

        // given
        database.with(
            DEFAULT_STALL.copy(
                name = "Amtmannsbult"
            )
        )
        // when
        viewModel.onSearchEntered("A")
        viewModel.onSearchEntered("Am")
        viewModel.onSearchEntered("Amt")
        viewModel.onSearchEntered("Amtm")
        testDispatcher.advanceUntilIdle()

        // then
        testObserver.assertHasElements(1)
    }

    @Test
    fun `stall is found by part of name`() = runBlockingTest {
        val slug = "riesenrad"
        // given
        database.with(
            DEFAULT_STALL.copy(
                slug = slug,
                name = "Wilhlems Riesenrad"
            )
        )
        // when
        viewModel.onSearchEntered("ries")
        testDispatcher.advanceUntilIdle()

        // then
        val results = viewModel.searchResults.value!!
        assertEquals(1, results.size)
        val result = viewModel.searchResults.value!!.first()
        assertTrue("Result is not StallResult", result is SearchResult.StallSearchResult)
        assertEquals(slug, result.stallSlugs.first())
    }

    @Test
    fun `selecting single stall highlights it as such`() = runBlockingTest {
        val slug = "amtmannsbult"
        // given
        database.with(
            DEFAULT_STALL.copy(
                slug = slug,
                name = "Amtmannsbult"
            )
        )
        // when
        viewModel.onStallsSelected(Route.Map.State.Carousel.StallCollection.Single(slug))

        // then
        val result = viewModel.highlightedStalls.value!!.first()
        assertTrue(result is Highlight.SingleStall)
        val correctStalls = result.getStalls().count { it.basicInfo.slug == slug }
        assertEquals(1, correctStalls)
    }

    @Test
    fun `selecting single stall without name highlights it as such`() = runBlockingTest {
        val slug = "generic-slush"
        // given
        database.with(DEFAULT_STALL.copy(slug = slug))

        // when
        viewModel.onStallsSelected(Route.Map.State.Carousel.StallCollection.Single(slug))

        // then
        val result = viewModel.highlightedStalls.value!!.first()
        assertTrue(result is Highlight.NamelessStall)
        val correctStalls = result.getStalls().count { it.basicInfo.slug == slug }
        assertEquals(1, correctStalls)
    }

    @Test
    fun `opening item highlight works`() = runBlockingTest {
        val genericSlug1 = "generic-icecream"
        val genericSlug2 = "generic-icecream2"
        val namedSlug1 = "eis-wie-sahne"
        val namedSlug2 = "eis-palast"

        // given
        val item = Item("icecream", "Eis")
        database.with(DEFAULT_STALL.copy(slug = genericSlug1), item)
        database.with(DEFAULT_STALL.copy(slug = genericSlug2), item)
        database.with(DEFAULT_STALL.copy(slug = namedSlug1, name = "Eis wie Sahne"), item)
        database.with(DEFAULT_STALL.copy(slug = namedSlug2, name = "Eispalast"), item)

        // when
        viewModel.onStallsSelected(
            Route.Map.State.Carousel.StallCollection.ItemCollection(
                "icecream",
                listOf(genericSlug1, genericSlug2, namedSlug1, namedSlug2)
            )
        )

        // then
        val result = viewModel.highlightedStalls.value!!
        assertEquals(3, result.size)

        assertTrue(result[0] is Highlight.SingleStall)
        assertEquals(namedSlug1, result[0].getStalls().first().basicInfo.slug)
        assertTrue(result[1] is Highlight.SingleStall)
        assertEquals(namedSlug2, result[1].getStalls().first().basicInfo.slug)
        // Nameless stalls are collected and put to the end.
        assertTrue(result[2] is Highlight.ItemCollection)
        assertEquals(2, result[2].getStalls().size)
        assertEquals(genericSlug1, result[2].getStalls()[0].basicInfo.slug)
        assertEquals(genericSlug2, result[2].getStalls()[1].basicInfo.slug)
    }

    @Test
    fun `opening type highlight works`() = runBlockingTest {
        val genericSlug1 = "generic-ride"
        val genericSlug2 = "generic-ride"
        val namedSlug1 = "wilde-maus"
        val namedSlug2 = "wellenflug"

        // given
        val type = SubType("ride", "Fahrgeschäft")
        database.with(DEFAULT_STALL.copy(slug = genericSlug1), type)
        database.with(DEFAULT_STALL.copy(slug = genericSlug2), type)
        database.with(DEFAULT_STALL.copy(slug = namedSlug1, name = "Wilde Maus"), type)
        database.with(DEFAULT_STALL.copy(slug = namedSlug2, name = "Wellenflug"), type)

        // when
        viewModel.onStallsSelected(
            Route.Map.State.Carousel.StallCollection.TypeCollection(
                "ride",
                listOf(genericSlug1, genericSlug2, namedSlug1, namedSlug2)
            )
        )

        // then
        val result = viewModel.highlightedStalls.value!!
        assertEquals(3, result.size)

        assertTrue(result[0] is Highlight.SingleStall)
        assertEquals(namedSlug1, result[0].getStalls().first().basicInfo.slug)
        assertTrue(result[1] is Highlight.SingleStall)
        assertEquals(namedSlug2, result[1].getStalls().first().basicInfo.slug)
        // Nameless stalls are collected and put to the end.
        assertTrue(result[2] is Highlight.TypeCollection)
        assertEquals(2, result[2].getStalls().size)
        assertEquals(genericSlug1, result[2].getStalls()[0].basicInfo.slug)
        assertEquals(genericSlug2, result[2].getStalls()[1].basicInfo.slug)
    }


    @Test
    fun `single stall highlights works`() {
        // given
        val stall = DEFAULT_STALL.copy(
            slug = "amtmannsbult",
            centerLng = 8.29536052129,
            centerLat = 52.748749353112,
            minLng = 8.29529464178,
            minLat = 52.74867357606,
            maxLng = 8.29545934056,
            maxLat = 52.74880375829
        )
        database.with(stall)

        // when
        viewModel.onStallHighlightedSelected(
            Highlight.SingleStall(FullStall(basicInfo = stall, subTypes = emptyList()))
        )

        // then
        val result = viewModel.mapFocus.value!!
        assertTrue(result is MapFocus.One)
        val focus = (result as MapFocus.One)
        assertEquals(focus.coordinate.longitude, 8.29536052129)
        assertEquals(focus.coordinate.latitude, 52.748749353112)
    }

    @Test
    fun `type highlights works`() {
        // given
        val type = SubType("ride", "Fahrgeschäft")
        val stall1 = DEFAULT_STALL.copy(
            slug = "spinning-racer",
            centerLng = 8.297406518132,
            centerLat = 52.747007695534,
            minLng = 8.29721617346,
            minLat = 52.74684088902,
            maxLng = 8.29765450807,
            maxLat = 52.74725790519
        )
        val stall2 = DEFAULT_STALL.copy(
            slug = "piraten-rutsche",
            centerLng = 8.29737657478,
            centerLat = 52.7467986333862,
            minLng = 8.29723358979,
            minLat = 52.74674888786,
            maxLng = 8.29776582232,
            maxLat = 52.74683296003
        )
        database.with(stall1, type)
        database.with(stall2, type)

        // when
        viewModel.onStallHighlightedSelected(
            Highlight.TypeCollection(
                stalls = listOf(
                    FullStall(basicInfo = stall1, subTypes = listOf(type)),
                    FullStall(basicInfo = stall2, subTypes = listOf(type))
                ),
                type = type
            )
        )

        // then
        val result = viewModel.mapFocus.value!!
        assertTrue(result is MapFocus.All)
        val focus = (result as MapFocus.All)
        assertTrue(
            "MapFocus.All doesn't include all necessary coordinates",
            focus.coordinates.containsAll(
                listOf(
                    Location(longitude = 8.29721617346, latitude = 52.74684088902),
                    Location(longitude = 8.29765450807, latitude = 52.74725790519),
                    Location(longitude = 8.29723358979, latitude = 52.74674888786),
                    Location(longitude = 8.29776582232, latitude = 52.74683296003)
                )
            )
        )
    }
}