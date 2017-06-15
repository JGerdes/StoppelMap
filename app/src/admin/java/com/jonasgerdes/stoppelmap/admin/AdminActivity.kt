package com.jonasgerdes.stoppelmap.admin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.util.readAsString
import io.realm.Realm

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */
class AdminActivity : AppCompatActivity() {

    companion object {
        val PATH_ENTITIES = "json/entities/"
    }

    val gson = GsonBuilder().create()
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        realm = Realm.getDefaultInstance()
        loadData()
    }

    fun loadData() {
        val json = assets.readAsString(PATH_ENTITIES + "rides.json")
        val rides = gson.fromJson(json, MapEntity.MapEntities::class.java)
        realm.executeTransaction {
            realm.deleteAll()
            realm.copyToRealm(rides)
        }
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }
}
