package com.jonasgerdes.stoppelmap.preparation

import com.jonasgerdes.stoppelmap.preparation.operations.FillDatabase
import com.jonasgerdes.stoppelmap.preparation.operations.PrepareDatabase
import org.koin.core.context.startKoin

fun main(vararg args: String) {

    startKoin {
        modules(preparationModule)
    }

    println("Starting preparation")
    
    val fillDatabase = FillDatabase()
    val prepareDatabase = PrepareDatabase()

    prepareDatabase()
    fillDatabase()
}
