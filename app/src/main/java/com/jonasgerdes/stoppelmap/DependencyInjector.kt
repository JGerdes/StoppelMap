@file:Suppress("UNCHECKED_CAST")

package com.jonasgerdes.stoppelmap

import com.jonasgerdes.stoppelmap.model.entity.StoppelMapDatabase
import kotlin.reflect.KProperty


inline fun <reified T> inject() = inject(T::class.java)

fun <T> inject(clazz: Class<T>): Injector<T> {
    return getInstanceFor(clazz) as Injector<T>
}

class Injector<T>(private val provide: () -> T) {
    operator fun getValue(any: Any, property: KProperty<*>) = provide()
}

private fun <T> getInstanceFor(clazz: Class<T>) = when (clazz) {

    StoppelMapDatabase::class.java -> Injector {
        StoppelMapDatabase.database
    }


    else -> throw RuntimeException("Couldn't inject ")
}

