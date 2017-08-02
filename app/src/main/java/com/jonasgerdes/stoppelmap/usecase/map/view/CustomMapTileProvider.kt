package com.jonasgerdes.stoppelmap.usecase.map.view

import android.content.res.AssetManager
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import com.jonasgerdes.stoppelmap.util.read

class CustomMapTileProvider(private val assets: AssetManager) : TileProvider {

    companion object {
        private val TILE_WIDTH = 512
        private val TILE_HEIGHT = 512
        private val TILE_PATH = "map"
        private val TILE_FORMAT = "webp"
        private lateinit var TILE_NONE: ByteArray
    }

    init {
        TILE_NONE = assets.read("${TILE_PATH}/none.${TILE_FORMAT}")!!
    }

    override fun getTile(x: Int, y: Int, zoom: Int): Tile? {
        val assetFile = getTileFilename(x, y, zoom)
        val image = assets.read(assetFile) ?: TILE_NONE
        return Tile(TILE_WIDTH, TILE_HEIGHT, image)
    }

    private fun getTileFilename(x: Int, y: Int, zoom: Int): String {
        return "${TILE_PATH}/$zoom/$x/$y.${TILE_FORMAT}"
    }

}