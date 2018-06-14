package com.jonasgerdes.stoppelmap.model.map.entity

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Entity(tableName = "sub_types",
        primaryKeys = ["slug", "name"])
data class SubType(
        val slug: String,
        val name: String
)

@Entity(tableName = "stall_sub_types",
        primaryKeys = ["stall", "sub_type"])
data class StallSubType(
        val stall: String,
        @ColumnInfo(name = "sub_type")
        val subType: String
)

@Dao
interface SubTypeDao {
    @Query("SELECT * FROM sub_types WHERE name LIKE :query")
    fun searchByName(query: String): Single<List<Item>>

}