package com.jonasgerdes.stoppelmap.model

import android.arch.persistence.room.TypeConverter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

object Converters {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: String?): OffsetDateTime? {
        return value?.let {
            return formatter.parse(value, OffsetDateTime::from)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: OffsetDateTime?): String? {
        return date?.format(formatter)
    }


    @TypeConverter
    @JvmStatic
    fun toIntList(value: String?): List<Int>? {
        return value?.split(";")?.map { it.toInt() }
    }

    @TypeConverter
    @JvmStatic
    fun fromIntList(list: List<Int>?): String? {
        return list?.joinToString(";")
    }

}