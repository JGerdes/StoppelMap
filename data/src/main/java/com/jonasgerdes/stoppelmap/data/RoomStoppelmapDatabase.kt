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
abstract class RoomStoppelmapDatabase() : RoomDatabase(), StoppelmapDatabase {

    abstract override fun stallsDao(): StallDao
    abstract override fun stallTypeDao(): StallTypeDao
    abstract override fun itemDao(): ItemDao
    abstract override fun eventDao(): EventDao
    abstract override fun routeDao(): RouteDao
    abstract override fun stationDao(): StationDao
    abstract override fun departureDao(): DepartureDao
    abstract override fun priceDao(): PriceDao

    companion object {

        val databaseFileName = "stoppelmap.db"

        fun init(context: Context) {
            val dir = File(context.filesDir.parentFile, "databases")
            context.removeDatabase("stoppelmap")
            context.assets.copyToFile(databaseFileName, File(dir, databaseFileName))
        }

        @Volatile
        private var instance: RoomStoppelmapDatabase? = null

        fun getInstance(context: Context): RoomStoppelmapDatabase =
            instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context,
                RoomStoppelmapDatabase::class.java,
                databaseFileName
            )
                .addMigrations()
                .build().also { instance = it }
        }
    }
}
