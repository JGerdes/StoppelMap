package com.jonasgerdes.stoppelmap

import android.app.Application
import com.jonasgerdes.stoppelmap.admin.Administration

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Administration.init(this)
    }
}