package com.jonasgerdes.stoppelmap.data.di

import android.content.Context
import com.jonasgerdes.stoppelmap.data.RoomStoppelmapDatabase
import com.jonasgerdes.stoppelmap.data.StoppelmapDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun database(context: Context): StoppelmapDatabase
            = RoomStoppelmapDatabase.getInstance(context)
    
}