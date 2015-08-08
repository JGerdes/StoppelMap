package com.jonasgerdes.stoppelmap;

import android.content.res.AssetManager;
import android.util.Log;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomMapTileProvider implements TileProvider {
    private static final int TILE_WIDTH = R.integer.tile_size;
    private static final int TILE_HEIGHT = R.integer.tile_size;
    private static final int BUFFER_SIZE = 16 * 1024;

    private AssetManager mAssets;
    private Tile empty;
    private String path;

    public CustomMapTileProvider(AssetManager assets) {
        mAssets = assets;
        path = "map/"+MainActivity.getContext().getString(R.string.tile_path)+"/";
        Log.d("TP", "loading tiles for " + MainActivity.getContext().getString(R.string.tile_path));
        byte[] image = readImage(path+"none.png");
        empty = image == null ? null : new Tile(TILE_WIDTH, TILE_HEIGHT, image);
    }

    @Override
    public Tile getTile(int x, int y, int zoom) {
        y = fixYCoordinate(y, zoom);
        byte[] image = readTileImage(x, y, zoom);
        return image == null ? empty : new Tile(TILE_WIDTH, TILE_HEIGHT, image);
    }

    private byte[] readTileImage(int x, int y, int zoom) {
        return readImage(getTileFilename(x, y, zoom));
    }

    private byte[] readImage(String path){
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
            if (in != null) try { in.close(); } catch (Exception ignored) {}
            if (buffer != null) try { buffer.close(); } catch (Exception ignored) {}
        }
    }

    private String getTileFilename(int x, int y, int zoom) {
        return path + zoom + '/' + x + '_' + y + ".png";
    }

    private int fixYCoordinate(int y, int zoom) {
        int size = 1 << zoom; // size = 2^zoom
        return size - 1 - y;
    }
}