package com.jonasgerdes.stoppelmap.news.data.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "images", primaryKeys = ["url"],
    indices = [
        Index("article_url")
    ],
    foreignKeys = [
        ForeignKey(
            entity = Article::class,
            parentColumns = ["url"],
            childColumns = ["article_url"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Image(
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "article_url") val articleUrl: String,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "caption") val caption: String?
)