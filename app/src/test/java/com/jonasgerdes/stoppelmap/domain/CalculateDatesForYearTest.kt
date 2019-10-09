package com.jonasgerdes.stoppelmap.domain

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.threeten.bp.LocalDate
import org.threeten.bp.Month


class CalculateDatesForYearTest {
    @Test // Earliest possible date.
    fun `calculated dates are correct for years where 15th is tuesday`() {
        // given
        val year = 2017

        // when
        val result = calculateDatesForYear(year)

        // then
        assertEquals(
            listOf(
                LocalDate.of(year, Month.AUGUST, 10),
                LocalDate.of(year, Month.AUGUST, 11),
                LocalDate.of(year, Month.AUGUST, 12),
                LocalDate.of(year, Month.AUGUST, 13),
                LocalDate.of(year, Month.AUGUST, 14),
                LocalDate.of(year, Month.AUGUST, 15)
            ), result
        )
    }

    @Test // Latest possible date.
    fun `calculated dates are correct for years where 15th is wednesday`() {
        // given
        val year = 2018

        // when
        val result = calculateDatesForYear(year)

        // then
        assertEquals(
            listOf(
                LocalDate.of(year, Month.AUGUST, 16),
                LocalDate.of(year, Month.AUGUST, 17),
                LocalDate.of(year, Month.AUGUST, 18),
                LocalDate.of(year, Month.AUGUST, 19),
                LocalDate.of(year, Month.AUGUST, 20),
                LocalDate.of(year, Month.AUGUST, 21)
            ), result
        )
    }

    @Test
    fun `calculated dates are correct for years where 15th is thursday`() {
        // given
        val year = 2019

        // when
        val result = calculateDatesForYear(year)

        // then
        assertEquals(
            listOf(
                LocalDate.of(year, Month.AUGUST, 15),
                LocalDate.of(year, Month.AUGUST, 16),
                LocalDate.of(year, Month.AUGUST, 17),
                LocalDate.of(year, Month.AUGUST, 18),
                LocalDate.of(year, Month.AUGUST, 19),
                LocalDate.of(year, Month.AUGUST, 20)
            ), result
        )
    }

    @Test
    fun `calculated dates are correct for years where 15th is some random day`() {
        // given
        val year = 2020

        // when
        val result = calculateDatesForYear(year)

        // then
        assertEquals(
            listOf(
                LocalDate.of(year, Month.AUGUST, 13),
                LocalDate.of(year, Month.AUGUST, 14),
                LocalDate.of(year, Month.AUGUST, 15),
                LocalDate.of(year, Month.AUGUST, 16),
                LocalDate.of(year, Month.AUGUST, 17),
                LocalDate.of(year, Month.AUGUST, 18)
            ), result
        )
    }
}