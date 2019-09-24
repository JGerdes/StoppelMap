package com.jonasgerdes.stoppelmap.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

fun <T> LiveData<T>.test() = TestObserver<T>().apply { observeForever(this) }

class TestObserver<T> : Observer<T> {
    private var elements = emptyList<T>()

    @Suppress("SuspiciousCollectionReassignment")
    override fun onChanged(element: T) {
        elements += element
    }

    fun assertHasElements() =
        assertTrue("LiveData has no elements", elements.isNotEmpty())

    fun assertHasElements(expectedCount: Int) {
        val actual = elements.size
        assertEquals(
            "Expected  $expectedCount, but has $actual elements",
            expectedCount,
            actual
        )
    }

    fun assertHasAtLeastElements(count: Int) {
        val actual = elements.size
        assertTrue(
            "Expected at least $count, but has $actual elements",
            actual >= count
        )
    }

    fun assertElementEquals(index: Int, element: T) {
        val elements = this.elements
        val count = index + 1
        assertTrue(
            "Expected at least $count, but has ${elements.size} elements",
            elements.size >= count
        )
        assertEquals(elements[index], element)
    }


}