package com.jonasgerdes.stoppelmap.admin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
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
    }

    val gson = GsonBuilder()
            .registerTypeAdapter(RealmString::class.java, RealmString.TypeAdapter())
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
        realm.executeTransaction {
            realm.deleteAll()
            realm.copyToRealm(entities)
        }
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }
}
