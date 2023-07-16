package com.jonasgerdes.stoppelmap.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jonasgerdes.stoppelmap.settings.data.ImageSources
import com.jonasgerdes.stoppelmap.settings.data.Libraries
import com.jonasgerdes.stoppelmap.settings.data.SettingsRepository
import com.jonasgerdes.stoppelmap.settings.ui.SettingsViewModel
import com.jonasgerdes.stoppelmap.settings.usecase.GetSettingsUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val settingsModule = module {

    single {
        SettingsRepository(dataStore = get<Context>().dataStore)
    }

    factory {
        GetSettingsUseCase(settingsRepository = get())
    }

    viewModel {
        SettingsViewModel(
            settingsRepository = get(),
            appInfo = get(),
            libraries = get<Libraries>().libraries,
            imageSources = get<ImageSources>().imageSources,
        )
    }
}
