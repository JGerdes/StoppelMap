package com.jonasgerdes.stoppelmap.news.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jonasgerdes.stoppelmap.news.data.source.local.model.Article
import com.jonasgerdes.stoppelmap.news.data.source.local.model.ArticleWithImages

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: List<Article>)

    @Query("SELECT * FROM articles ORDER BY publishDate DESC")
    fun getAllArticlesWithImages(): List<ArticleWithImages>

    @Query("DELETE FROM articles")
    fun clearAll()

}