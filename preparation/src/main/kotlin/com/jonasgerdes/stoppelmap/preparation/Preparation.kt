package com.jonasgerdes.stoppelmap.preparation

import com.jonasgerdes.stoppelmap.data.dataModule
import com.jonasgerdes.stoppelmap.preparation.operations.GenerateStoppelMapData
import com.jonasgerdes.stoppelmap.preparation.operations.VerifyStoppelMapDataConversion
import com.jonasgerdes.stoppelmap.preparation.operations.WriteStoppelMapData
import org.koin.core.context.startKoin

fun main(vararg args: String) {

    startKoin {
        modules(
            dataModule,
            preparationModule,
        )
    }

    println("Starting preparation")

    val generateData = GenerateStoppelMapData()
    val writeData = WriteStoppelMapData()
    val verify = VerifyStoppelMapDataConversion()

    val data = generateData()
    verify(data)
    writeData(data)
}
