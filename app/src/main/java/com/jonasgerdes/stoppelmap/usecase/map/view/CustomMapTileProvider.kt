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
    }

    override fun getTile(x: Int, y: Int, zoom: Int): Tile? {
        val assetFile = getTileFilename(x, y, zoom)
        val image = assets.read(assetFile)
        return if (image != null) Tile(TILE_WIDTH, TILE_HEIGHT, image) else null
    }

    private fun getTileFilename(x: Int, y: Int, zoom: Int): String {
        return "${TILE_PATH}/$zoom/$x/$y.${TILE_FORMAT}"
    }

}