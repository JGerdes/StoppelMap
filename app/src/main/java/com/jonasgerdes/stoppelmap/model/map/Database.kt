package com.jonasgerdes.stoppelmap.model.map

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.jonasgerdes.stoppelmap.model.map.entity.*
import com.jonasgerdes.stoppelmap.util.copyToFile
import java.io.File

@Database(entities = [Stall::class, Alias::class, Item::class, StallItem::class], version = 1, exportSchema = false)
abstract class StoppelMapDatabase : RoomDatabase() {
    abstract fun stalls(): StallDao
    abstract fun items(): ItemDao

    companion object {
        lateinit var database: StoppelMapDatabase
            private set

        fun init(context: Context) {
            val databaseFileName = "stoppelmap.db"
            val dir = File(context.filesDir.parentFile, "databases")
            context.assets.copyToFile(databaseFileName, File(dir, databaseFileName))
            database = Room.databaseBuilder(context, StoppelMapDatabase::class.java, databaseFileName)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }
    }
}
