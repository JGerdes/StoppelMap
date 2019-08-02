package com.jonasgerdes.stoppelmap.data

import androidx.room.TypeConverter
import com.jonasgerdes.stoppelmap.model.map.Type
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

class RoomConverters {

    private val dateTimeFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun toOffsetDateTime(value: String): OffsetDateTime = OffsetDateTime.parse(value, dateTimeFormat)

    @TypeConverter
    fun fromOffsetDateTime(value: OffsetDateTime): String = dateTimeFormat.format(value)

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value, dateFormat)

    @TypeConverter
    fun fromLocalDate(value: LocalDate): String = dateFormat.format(value)


    @TypeConverter
    fun toStallType(value: String): Type = Type.values().find { it.type == value } ?: Type.MISC

    @TypeConverter
    fun fromStallType(value: Type): String = value.type
}