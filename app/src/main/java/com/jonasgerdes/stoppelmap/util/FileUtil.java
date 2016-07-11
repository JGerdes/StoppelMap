package com.jonasgerdes.stoppelmap.util;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jonas on 11.07.2016.
 */
public class FileUtil {

    public static String readAssetAsString(AssetManager assets, String assetPath) {
        try {
            InputStream is = assets.open(assetPath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
