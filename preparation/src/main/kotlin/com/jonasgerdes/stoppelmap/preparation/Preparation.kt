package com.jonasgerdes.stoppelmap.preparation

import com.jonasgerdes.stoppelmap.data.dataModule
import com.jonasgerdes.stoppelmap.preparation.operations.PrepareStoppelMapData
import com.jonasgerdes.stoppelmap.preparation.operations.VerifyStoppelMapDataConversion
import com.jonasgerdes.stoppelmap.preparation.operations.WriteStoppelMapData
import com.jonasgerdes.stoppelmap.preparation.util.Version
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main(vararg args: String) {

    startKoin {
        modules(
            dataModule,
            preparationModule,
            module {
                single {
                    Version(code = args
                        .first { it.startsWith("versionCode=") }
                        .removePrefix("versionCode=").toInt())
                }
            }
        )
    }

    println("Starting preparation")

    val generateData = PrepareStoppelMapData()
    val writeData = WriteStoppelMapData()
    val verify = VerifyStoppelMapDataConversion()

    val data = generateData()
    verify(data)
    writeData(data)
}
