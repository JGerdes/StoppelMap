package com.jonasgerdes.stoppelmap.news.data.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import org.threeten.bp.LocalDate

@Entity(tableName = "articles", primaryKeys = ["url"])
data class Article(
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "teaser") val teaser: String?,
    @ColumnInfo(name = "publishDate") val publishDate: LocalDate,
    @ColumnInfo(name = "content") val content: String?
)