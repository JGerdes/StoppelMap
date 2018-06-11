package com.jonasgerdes.stoppelmap.model.map.entity

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Entity(tableName = "items")
data class Item(
        @PrimaryKey
        val slug: String,
        val name: String
)

@Entity(tableName = "stall_items",
        primaryKeys = ["stall", "item"],
        foreignKeys = [
            ForeignKey(entity = Stall::class,
                    parentColumns = ["slug"],
                    childColumns = ["stall"]),
            ForeignKey(entity = Item::class,
                    parentColumns = ["slug"],
                    childColumns = ["item"])
        ])
data class StallItem(
        val stall: String,
        val item: String
)

@Dao
interface ItemDao {
    @Query("SELECT * FROM items WHERE name LIKE :query")
    fun searchByName(query: String): Single<List<Item>>

}