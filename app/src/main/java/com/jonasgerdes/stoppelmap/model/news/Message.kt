package com.jonasgerdes.stoppelmap.model.news

import android.arch.persistence.room.*

@Entity(tableName = "messages")
data class VersionMessage(
        @PrimaryKey
        var slug: String,
        var title: String? = null,
        var body: String? = null,
        var versions: List<Int>? = null,
        var image: String? = null,
        var url: String? = null,
        var color: Int? = null,
        @ColumnInfo(name = "show_always")
        var showAlways: Boolean,
        var seen: Boolean = false,
        var type:String = slug.substringBefore("#", TYPE_DEFAULT)
) {
    companion object {
        const val TYPE_DEFAULT = "default"
        const val TYPE_UPDATE = "update"
    }
}

@Dao
interface VersionMessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessage(vararg message: VersionMessage)

    @Query("SELECT * FROM messages WHERE seen = 0 OR show_always = 1")
    fun getUnseen(): List<VersionMessage>

    @Query("DELETE FROM messages WHERE seen = 0 OR show_always = 1")
    fun removeUnseen()

    @Query("UPDATE messages SET seen = 1 WHERE slug = :messageSlug")
    fun markAsSeen(messageSlug: String)
}

fun VersionMessageDao.getUnseenForVersion(version: Int) =
        getUnseen().filter { it.versions?.contains(version) ?: true }