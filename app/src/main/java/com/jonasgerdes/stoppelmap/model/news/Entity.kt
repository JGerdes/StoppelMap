package com.jonasgerdes.stoppelmap.model.news

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import io.reactivex.Observable
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Dao
import io.reactivex.Flowable


data class NewsResponse(val versionName: String = "",
                        val version: Int = 0,
                        val updated: String = "",
                        val items: List<NewsItem>)


data class NewsItem(val url: String,
                    val images: List<String>?,
                    val subTitle: String? = "",
                    val publishDate: String = "",
                    val title: String = "",
                    val content: String = "",
                    val teaser: String = "")

@Entity(tableName = "feed_items")
data class FeedItem(
        @PrimaryKey
        var url: String,
        var type: String?,
        @ColumnInfo(name = "sub_title")
        var subTitle: String? = "",
        @ColumnInfo(name = "publish_date")
        var publishDate: String? = "",
        var title: String? = "",
        var content: String? = "",
        var author: String? = null
) {
    companion object {
        val TYPE_NEWS = "NEWS"
    }
}

@Entity(tableName = "feed_images",
        foreignKeys = [ForeignKey(entity = FeedItem::class,
                parentColumns = ["url"],
                childColumns = ["feed_item"],
                onDelete = CASCADE)])

@Dao
interface FeedItemDao {

        @Query("SELECT * FROM feed_items")
        fun getAll(): Flowable<List<FeedItem>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertItem(item: FeedItem)
}
@Entity(tableName = "feed_images")
data class FeedImage(
        @PrimaryKey
        var url: String,
        @ColumnInfo(name = "feed_item")
        var feedItem: String?
)

@Dao
interface FeedImageDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertItem(item: FeedImage)
}

class FeedItemWithImages {
        @Embedded
        var feedItem: FeedItem? = null

        @Relation(parentColumn = "url", entityColumn = "feed_item")
        var images: List<FeedImage>? = null
}

@Dao
interface FeedItemWithImagesDao {
        @Transaction
        @Query("SELECT * FROM feed_items")
        fun getAll(): Flowable<List<FeedItemWithImages>>
}