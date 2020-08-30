package com.jonasgerdes.stoppelmap.news.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jonasgerdes.stoppelmap.news.data.source.local.dao.ArticleDao
import com.jonasgerdes.stoppelmap.news.data.source.local.dao.ImageDao
import com.jonasgerdes.stoppelmap.news.data.source.local.model.Article
import com.jonasgerdes.stoppelmap.news.data.source.local.model.Image


@Database(
    entities = [
        Article::class,
        Image::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class NewsDatabase() : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun imageDao(): ImageDao

    companion object {
        @Volatile
        private var instance: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context, NewsDatabase::class.java, "news")
                .addMigrations()
                .fallbackToDestructiveMigration()
                .build().also { instance = it }
        }
    }
}


