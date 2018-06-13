package com.jonasgerdes.stoppelmap.model.map.entity

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import io.reactivex.Single

@Entity(tableName = "images")
data class Image(
        @PrimaryKey
        val file: String,
        val stall: String,
        val type: String,
        val author: String,
        val license: String?
)

fun List<Image>.headers() = filter { it.type == "header" }

@Dao
interface ImageDao {
    @Query("SELECT * FROM images WHERE images.stall IN (:stalls)")
    fun getAllForStalls(stalls: Array<String>): Single<List<Image>>

}