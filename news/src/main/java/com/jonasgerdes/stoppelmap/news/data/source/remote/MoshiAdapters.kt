package com.jonasgerdes.stoppelmap.news.data.source.remote

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter


object MoshiAdapters {

    private val dateTimeFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE

    @FromJson
    fun offsetDateTimeFromJson(value: String): OffsetDateTime = OffsetDateTime.parse(value, dateTimeFormat)

    @ToJson
    fun offsetDateTimeToJson(value: OffsetDateTime): String = dateTimeFormat.format(value)

    @FromJson
    fun localDateFromJson(value: String): LocalDate = LocalDate.parse(value, dateFormat)

    @ToJson
    fun localDateToJson(value: LocalDate): String = dateFormat.format(value)
}