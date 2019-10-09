package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.core.domain.LocationProvider
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import junit.framework.Assert.*
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetUserLocationUseCaseTest {

    @Test
    fun `location is correctly mapped`() = runBlocking {
        // given
        val provider = mock<LocationProvider> {
            onBlocking { invoke() } doReturn LocationProvider.Location(
                latitude = 1.0,
                longitude = 2.0
            )
        }
        val getUserLocation = GetUserLocationUseCase(provider)
        // when
        val result = getUserLocation()
        // then
        assertNotNull(result)
        assertEquals(1.0, result!!.latitude)
        assertEquals(2.0, result.longitude)
    }

    @Test
    fun `disabled location returns null`() = runBlocking {
        // given
        val provider = mock<LocationProvider> {
            onBlocking { invoke() } doReturn null
        }
        val getUserLocation = GetUserLocationUseCase(provider)
        // when
        val result = getUserLocation()
        // then
        assertNull(result)
    }
}