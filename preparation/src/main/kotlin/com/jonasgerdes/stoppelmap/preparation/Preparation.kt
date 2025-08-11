package com.jonasgerdes.stoppelmap.preparation

import com.jonasgerdes.stoppelmap.data.dataModule
import com.jonasgerdes.stoppelmap.preparation.operations.GenerateDeeplinkConfig
import com.jonasgerdes.stoppelmap.preparation.operations.PrepareStoppelMapData
import com.jonasgerdes.stoppelmap.preparation.operations.VerifyStoppelMapData
import com.jonasgerdes.stoppelmap.preparation.operations.VerifyStoppelMapDataConversion
import com.jonasgerdes.stoppelmap.preparation.operations.WriteStoppelMapData
import com.jonasgerdes.stoppelmap.preparation.operations.ZipData
import com.jonasgerdes.stoppelmap.preparation.util.Version
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun main(vararg args: String) {

    val koin = startKoin {
        modules(
            dataModule,
            preparationModule,
            module {
                single {
                    Version(
                        code = args
                            .first { it.startsWith("versionCode=") }
                            .removePrefix("versionCode=").toInt())
                }
            }
        )
    }

    println("Starting preparation")

    val settings = koin.koin.get<Settings>()
    val generateData = PrepareStoppelMapData()
    val writeData = WriteStoppelMapData()
    val verifyData = VerifyStoppelMapData()
    val verifyConversion = VerifyStoppelMapDataConversion()
    val zipData = ZipData()
    val generateDeeplinkConfig = GenerateDeeplinkConfig()

    val data = generateData()
    verifyData(data)
    verifyConversion(data)
    writeData(data)
    zipData()
    generateDeeplinkConfig(data)


    settings.tempDir.deleteRecursively()

}
