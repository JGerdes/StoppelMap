package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.core.domain.GlobalInfoProvider
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.assertEquals
import org.junit.Test

class IsUserInAreaUserCaseTest {

    private val testBounds = GlobalInfoProvider.AreaBounds(
        northLatitude = 2.0,
        southLatitude = 1.0,
        westLongitude = 3.0,
        eastLongitude = 4.0
    )

    private val infoProvider = mock<GlobalInfoProvider> {
        on {getAreaBounds()} doReturn testBounds
    }

    @Test
    fun `location in area is detected correctly as such`() {
        // given
        val isUserInArea = IsUserInAreaUseCase(infoProvider)
        //when
        val result = isUserInArea(GetUserLocationUseCase.Location(1.5, 3.5))
        //then
        assertEquals(IsUserInAreaUseCase.UserState.IN_AREA, result)
    }

    @Test
    fun `location north of area is detected as out of area`() {
        // given
        val isUserInArea = IsUserInAreaUseCase(infoProvider)
        //when
        val result = isUserInArea(GetUserLocationUseCase.Location(3.5, 3.5))
        //then
        assertEquals(IsUserInAreaUseCase.UserState.OUTSIDE_AREA, result)
    }

    @Test
    fun `location west of area is detected as outside of area`() {
        // given
        val isUserInArea = IsUserInAreaUseCase(infoProvider)
        //when
        val result = isUserInArea(GetUserLocationUseCase.Location(1.5, 1.5))
        //then
        assertEquals(IsUserInAreaUseCase.UserState.OUTSIDE_AREA, result)
    }

    @Test
    fun `no location will lead to no result`() {
        // given
        val isUserInArea = IsUserInAreaUseCase(infoProvider)
        //when
        val result = isUserInArea(null)
        //then
        assertEquals(IsUserInAreaUseCase.UserState.UNDEFINED, result)
    }
}