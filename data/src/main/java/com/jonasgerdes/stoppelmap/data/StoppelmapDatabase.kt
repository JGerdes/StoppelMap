package com.jonasgerdes.stoppelmap.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jonasgerdes.androidutil.copyToFile
import com.jonasgerdes.androidutil.removeDatabase
import com.jonasgerdes.stoppelmap.data.dao.*
import com.jonasgerdes.stoppelmap.model.common.Image
import com.jonasgerdes.stoppelmap.model.events.Event
import com.jonasgerdes.stoppelmap.model.map.*
import com.jonasgerdes.stoppelmap.model.transportation.Departure
import com.jonasgerdes.stoppelmap.model.transportation.Route
import com.jonasgerdes.stoppelmap.model.transportation.Station
import com.jonasgerdes.stoppelmap.model.transportation.TransportPrice
import java.io.File

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
        TransportPrice::class,
        Event::class
    ],
    version = 3,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class StoppelmapDatabase() : RoomDatabase() {

    abstract fun stallsDao(): StallDao
    abstract fun stallTypeDao(): StallTypeDao
    abstract fun itemDao(): ItemDao
    abstract fun eventDao(): EventDao
    abstract fun routeDao(): RouteDao
    abstract fun stationDao(): StationDao

    companion object {

        val databaseFileName = "stoppelmap.db"

        fun init(context: Context) {
            val dir = File(context.filesDir.parentFile, "databases")
            context.removeDatabase("stoppelmap")
            context.assets.copyToFile(databaseFileName, File(dir, databaseFileName))
        }

        @Volatile
        private var instance: StoppelmapDatabase? = null

        fun getInstance(context: Context): StoppelmapDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context, StoppelmapDatabase::class.java, databaseFileName)
                .addMigrations()
                .build().also { instance = it }
        }
    }
}
