package com.jonasgerdes.stoppelmap.usecases.map;

import android.content.res.AssetManager;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomMapTileProvider implements TileProvider {
    private static final int TILE_WIDTH = 512;
    private static final int TILE_HEIGHT = 512;
    private static final int BUFFER_SIZE = 16 * 1024;
    private static final String TILE_PATH = "map/";

    private AssetManager mAssets;
    private Tile mEmptyTile;
    private String mTilePath;

    public CustomMapTileProvider(AssetManager assets) {
        mAssets = assets;
        mTilePath = TILE_PATH;
        byte[] image = readImage(mTilePath + "none.png");
        mEmptyTile = image == null ? null : new Tile(TILE_WIDTH, TILE_HEIGHT, image);
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        y = fixYCoordinate(y, zoom);
        byte[] image = readTileImage(x, y, zoom);
        return image == null ? mEmptyTile : new Tile(TILE_WIDTH, TILE_HEIGHT, image);
    }

    private byte[] readTileImage(int x, int y, int zoom) {
        return readImage(getTileFilename(x, y, zoom));
    }

    private byte[] readImage(String path) {
        InputStream in = null;
        ByteArrayOutputStream buffer = null;

        try {
            in = mAssets.open(path);
            buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[BUFFER_SIZE];

            while ((nRead = in.read(data, 0, BUFFER_SIZE)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();

            return buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) try {
                in.close();
            } catch (Exception ignored) {
            }
            if (buffer != null) try {
                buffer.close();
            } catch (Exception ignored) {
            }
        }
    }

    private String getTileFilename(int x, int y, int zoom) {
        return mTilePath + zoom + '/' + x + '_' + y + ".png";
    }

    private int fixYCoordinate(int y, int zoom) {
        int size = 1 << zoom; // size = 2^zoom
        return size - 1 - y;
    }
}