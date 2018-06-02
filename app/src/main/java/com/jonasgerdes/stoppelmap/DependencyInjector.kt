@file:Suppress("UNCHECKED_CAST")

package com.jonasgerdes.stoppelmap

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jonasgerdes.stoppelmap.model.map.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.model.news.network.StoppelMapApi
import okhttp3.OkHttpClient
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

    StoppelMapDatabase::class.java -> instance {
        StoppelMapDatabase.database
    }

    Gson::class.java -> singleton {
        GsonBuilder().create()
    }

    StoppelMapApi::class.java -> singleton {
        val okhttp = OkHttpClient.Builder().build()
        Retrofit.Builder()
                .client(okhttp)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://app.stoppelmap.de/")
                .build()
                .create(StoppelMapApi::class.java)

    }


    else -> throw RuntimeException("Couldn't inject ${clazz.name}")
}
