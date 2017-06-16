package com.jonasgerdes.stoppelmap.model.realm_wrapper

import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.realm.RealmObject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 15.06.2017
 */
open class RealmString(var value: String? = null) : RealmObject() {

    class TypeAdapter : com.google.gson.TypeAdapter<RealmString>() {
        override fun write(out: JsonWriter?, value: RealmString?) {
            //ignore
        }

        override fun read(reader: JsonReader?): RealmString {
            return RealmString(reader?.nextString())
        }

    }
}