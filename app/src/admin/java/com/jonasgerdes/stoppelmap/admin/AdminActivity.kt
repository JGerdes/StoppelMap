package com.jonasgerdes.stoppelmap.admin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.Route
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.model.events.Event
import com.jonasgerdes.stoppelmap.model.events.Events
import com.jonasgerdes.stoppelmap.model.realm_wrapper.RealmString
import com.jonasgerdes.stoppelmap.util.readAsString
import io.realm.Realm

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */
class AdminActivity : AppCompatActivity() {

    companion object {
        val PATH_ENTITIES = "json/entities"
        val PATH_BUS_ROUTES = "json/transportation/bus"
        val PATH_EVENTS = "json/events"
    }

    val gson = GsonBuilder()
            .registerTypeAdapter(RealmString::class.java, RealmString.TypeAdapter())
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        realm = Realm.getDefaultInstance()
        loadData()
    }

    fun loadData() {
        val entities = ArrayList<MapEntity>()
        assets.list(PATH_ENTITIES).map {
            val json = assets.readAsString(PATH_ENTITIES + "/" + it)
            gson.fromJson(json, MapEntity.MapEntities::class.java)
        }.forEach {
            entities.addAll(it)
        }

        val routes = ArrayList<Route>()
        assets.list(PATH_BUS_ROUTES).map {
            val json = assets.readAsString(PATH_BUS_ROUTES + "/" + it)
            gson.fromJson(json, Route.Routes::class.java)
        }.forEach {
            routes.addAll(it)
        }

        val events = ArrayList<Event>()
        assets.list(PATH_EVENTS).map {
            val json = assets.readAsString(PATH_EVENTS + "/" + it)
            gson.fromJson(json, Events::class.java)
        }.forEach {
            events.addAll(it.events!!)
        }

        events.forEach {
            val locationUuid = it.locationUuid
            if (locationUuid != null) {
                it.mapEntity = entities.filter { it.slug == locationUuid }.firstOrNull()
            }
        }

        realm.executeTransaction {
            realm.deleteAll()
            realm.copyToRealm(entities)
            realm.copyToRealm(routes)
            realm.copyToRealm(events)
        }
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }
}
