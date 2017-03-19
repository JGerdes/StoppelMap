package com.jonasgerdes.stoppelmap.versioning;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.version.AppData;
import com.jonasgerdes.stoppelmap.model.version.Message;
import com.jonasgerdes.stoppelmap.model.version.Version;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jonas on 20.07.2016.
 */
public class VersionHelper {

    public interface OnVersionAvailableListener {
        void onVersionAvailable(Version version, List<Message> messages);
    }

    public static void requestVersionInfo(final Context context, final OnVersionAvailableListener listener) {
        String url = context.getString(R.string.versioning_check_url);
        String appName = context.getString(R.string.app_name);
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
            versionCode = info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String userAgent = appName;
        if (!versionName.isEmpty() || versionCode != -1) {
            userAgent = String.format("%s v%s (%d)", appName, versionName, versionCode);
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", userAgent)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onVersionAvailable(null, new ArrayList<Message>());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Gson gson = new GsonBuilder().create();
                    String jsonData = response.body().string();
                    AppData appData = gson.fromJson(jsonData, AppData.class);
                    boolean isBeta = context.getResources().getBoolean(R.bool.is_beta);
                    Version version = isBeta ? appData.version.beta : appData.version.release;
                    if (appData.messages == null) {
                        appData.messages = new ArrayList<>();
                    }
                    listener.onVersionAvailable(version, appData.messages);
                } catch (Exception e) {
                    listener.onVersionAvailable(null, new ArrayList<Message>());
                }
            }
        });

    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean getHasMessageBeShown(Context context, Message message) {
        SharedPreferences pref =
                context.getSharedPreferences("STOPPELMAP_SHOW_MESSAGES", Context.MODE_PRIVATE);
        return pref.getBoolean(message.slug, false);
    }

    public static void setHasMessageBeShown(Context context, Message message) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences("STOPPELMAP_SHOW_MESSAGES", Context.MODE_PRIVATE).edit();
        editor.putBoolean(message.slug, true).apply();
    }

}
