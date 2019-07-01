package com.jonasgerdes.stoppelmap.news.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jonasgerdes.stoppelmap.news.data.source.local.model.ArticleWithImages
import com.jonasgerdes.stoppelmap.news.data.source.local.model.Image

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(images: List<Image>)

}