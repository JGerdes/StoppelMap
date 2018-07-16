package com.jonasgerdes.stoppelmap.model.news

import android.arch.persistence.room.*
import android.content.Context
import com.jonasgerdes.stoppelmap.model.Converters
import com.jonasgerdes.stoppelmap.model.map.entity.Stall
import com.jonasgerdes.stoppelmap.model.map.entity.StallDao
import com.jonasgerdes.stoppelmap.util.copyToFile
import java.io.File

@Database(entities = [
    FeedItem::class,
    FeedImage::class,
    VersionMessage::class
], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DynamicDatabase : RoomDatabase() {
    abstract fun feedItems(): FeedItemDao
    abstract fun feedImages(): FeedImageDao
    abstract fun feedItemsWithImages(): FeedItemWithImagesDao
    abstract fun versionMessages(): VersionMessageDao

    companion object {
        lateinit var database: DynamicDatabase
            private set

        fun init(context: Context) {
            val databaseFileName = "dynamic.db"
            database = Room.databaseBuilder(context, DynamicDatabase::class.java, databaseFileName)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }
    }
}
