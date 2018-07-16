@file:Suppress("UNCHECKED_CAST")

package com.jonasgerdes.stoppelmap

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jonasgerdes.stoppelmap.model.map.InMemoryDatabase
import com.jonasgerdes.stoppelmap.model.map.InMemoryDatabaseImpl
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.news.DynamicDatabase
import com.jonasgerdes.stoppelmap.model.news.network.StoppelMapApi
import com.jonasgerdes.stoppelmap.util.DateTimeProvider
import com.jonasgerdes.stoppelmap.util.UserAgentInterceptor
import com.jonasgerdes.stoppelmap.util.versioning.VersionProvider
import com.jonasgerdes.stoppelmap.util.versioning.VersionProviderImpl
import okhttp3.OkHttpClient
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KProperty


inline fun <reified T> inject() = inject(T::class.java)

fun <T> inject(clazz: Class<T>): Injector<T> {
    return getInstanceFor(clazz) as Injector<T>
}

class Injector<T>(private val provide: () -> T) {
    operator fun getValue(any: Any, property: KProperty<*>) = provide()
}

private fun <T> instance(provide: () -> T): Injector<T> = Injector(provide)

private val singletons = mutableMapOf<String, Any>()

private inline fun <reified T : Any> singleton(crossinline provide: () -> T): Injector<T> = Injector {
    val className = T::class.java.name
    if (!singletons.contains(className)) {
        singletons[className] = provide()
    }
    singletons[className]!! as T
}


private fun <T> getInstanceFor(clazz: Class<T>) = when (clazz) {

    VersionProvider::class.java -> singleton {
        VersionProviderImpl.instance
    }

    StoppelMapDatabase::class.java -> instance {
        StoppelMapDatabase.database
    }

    DynamicDatabase::class.java -> instance {
        DynamicDatabase.database
    }

    InMemoryDatabase::class.java -> singleton {
        InMemoryDatabaseImpl()
    }

    Gson::class.java -> singleton {
        GsonBuilder().create()
    }

    DateTimeProvider::class.java -> singleton {
        DateTimeProvider
    }

    StoppelMapApi::class.java -> singleton {
        val okhttp = OkHttpClient.Builder()
                .addNetworkInterceptor(UserAgentInterceptor(VersionProviderImpl.instance.getUserAgent()))
                .build()
        Retrofit.Builder()
                .client(okhttp)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://app.stoppelmap.de/")
                .build()
                .create(StoppelMapApi::class.java)

    }


    else -> throw RuntimeException("Couldn't inject ${clazz.name}")
}
