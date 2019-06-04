package com.jonasgerdes.stoppelmap.news.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jonasgerdes.stoppelmap.news.data.source.local.model.Article

/*
@Database(
    entities = [
        Article::class
    ], version = 1, exportSchema = true
)
abstract class NewsDatabase() : RoomDatabase() {

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
}*/
