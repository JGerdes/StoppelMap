package com.jonasgerdes.stoppelmap.news.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.jonasgerdes.stoppelmap.news.data.source.local.model.Article

@Dao
abstract class ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(articles: List<Article>)


}