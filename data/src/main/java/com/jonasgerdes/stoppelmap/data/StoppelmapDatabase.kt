package com.jonasgerdes.stoppelmap.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jonasgerdes.stoppelmap.data.dao.StallDao
import com.jonasgerdes.stoppelmap.model.common.Image
import com.jonasgerdes.stoppelmap.model.map.*
import com.jonasgerdes.stoppelmap.model.transportation.Departure
import com.jonasgerdes.stoppelmap.model.transportation.Route
import com.jonasgerdes.stoppelmap.model.transportation.Station
import com.jonasgerdes.stoppelmap.model.transportation.TransportPrice

@Database(
    entities = [
        Image::class,
        Alias::class,
        Item::class,
        Stall::class,
        StallItem::class,
        StallSubType::class,
        SubType::class,
        Departure::class,
        Route::class,
        Station::class,
        TransportPrice::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class StoppelmapDatabase() : RoomDatabase() {

    abstract fun stallsDao(): StallDao

    companion object {
        @Volatile
        private var instance: StoppelmapDatabase? = null

        fun getInstance(context: Context): StoppelmapDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context, StoppelmapDatabase::class.java, "stoppelmap.db")
                .createFromAsset("stoppelmap.db")
                .addMigrations()
                .build().also { instance = it }
        }
    }
}
