package com.jonasgerdes.stoppelmap.versioning;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.jonasgerdes.stoppelmap.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Jonas on 20.07.2016.
 */
public class RealmUpdateManager {


    private static final String STOPPELMAP_VERSION_CONFIG = "STOPPELMAP_VERSION_CONFIG";
    private static final String REALM_VERSION = "REALM_VERSION";
    private Context mContext;
    private SharedPreferences mPreferences;
    private Resources mResources;

    public RealmUpdateManager(Context context) {

        mContext = context;
        mPreferences = context.getSharedPreferences(STOPPELMAP_VERSION_CONFIG,
                Context.MODE_PRIVATE);
        mResources = context.getResources();

    }

    public boolean isUptodate() {
        int currentRealmVersion = mPreferences.getInt(REALM_VERSION, 0);
        int providedRealmVersion = mResources.getInteger(R.integer.versioning_realm_version);
        return currentRealmVersion >= providedRealmVersion;

    }

    public boolean update() {
        String providedRealmFileName =
                mResources.getString(R.string.versioning_provided_realm_file_path);
        String internalRealmFileName =
                mResources.getString(R.string.versioning_internal_realm_file_path);
        try {
            InputStream realmFileStream = mContext.getAssets().open(providedRealmFileName);
            File internalFile = new File(mContext.getFilesDir(), internalRealmFileName);

            File result = copyFile(realmFileStream, internalFile);
            if (result.exists()) {
                int providedRealmVersion = mResources.getInteger(R.integer.versioning_realm_version);
                SharedPreferences.Editor edit = mPreferences.edit();
                edit.putInt(REALM_VERSION, providedRealmVersion);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }


    private File copyFile(InputStream inputStream, File outputFile) throws IOException {

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, bytesRead);
        }
        outputStream.close();
        return outputFile;
    }
}
