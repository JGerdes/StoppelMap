package com.jonasgerdes.stoppelmap.map.entity

import junit.framework.Assert.assertEquals
import org.junit.Test

class HighlightedTextTest {

    @Test
    fun `creation with no highlights works`() {
        // when
        val highlightedText = HighlightedText.withNoHighlights("Lorem Ipsum")
        // then
        assertEquals(0, highlightedText.highlights.size)
    }

    @Test
    fun `only first occurrence is found`() {
        //given
        val haystack = "Lorem ipsum dolor"
        // when
        val highlightedText = HighlightedText.from(haystack, "or")
        // then
        assertEquals(1, highlightedText.highlights.size)
        assertEquals(HighlightedText.Highlight(1, 2), highlightedText.highlights[0])
    }

    @Test
    fun `case is ignored`() {
        //given
        val haystack = "Lorem ipsum dolor"
        // when
        val highlightedText = HighlightedText.from(haystack, "lorem")
        // then
        assertEquals(1, highlightedText.highlights.size)
    }
}