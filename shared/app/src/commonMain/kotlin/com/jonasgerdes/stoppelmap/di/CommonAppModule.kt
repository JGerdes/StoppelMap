package com.jonasgerdes.stoppelmap.di

import com.jonasgerdes.stoppelmap.CommonBuildConfig
import com.jonasgerdes.stoppelmap.base.model.Secrets
import org.koin.dsl.module

val commonAppModule = module {
    single {
        Secrets(
            stoppelMapApiKey = CommonBuildConfig.STOPPELMAP_API_KEY
        )
    }
}