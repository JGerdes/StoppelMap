package com.jonasgerdes.stoppelmap.model.events

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.jonasgerdes.stoppelmap.model.map.Stall
import org.threeten.bp.OffsetDateTime

@Entity(
    tableName = "events",
    foreignKeys = [
        ForeignKey(
            entity = Stall::class,
            parentColumns = ["slug"],
            childColumns = ["location"]
        )
    ]
)
data class Event(
    @PrimaryKey
    val slug: String,
    val name: String,
    val location: String?,
    val description: String?,
    val start: OffsetDateTime,
    val end: OffsetDateTime?
)