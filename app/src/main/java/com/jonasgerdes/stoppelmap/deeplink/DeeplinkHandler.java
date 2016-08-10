package com.jonasgerdes.stoppelmap.deeplink;

import android.content.Intent;
import android.net.Uri;

import com.jonasgerdes.stoppelmap.deeplink.action.Action;
import com.jonasgerdes.stoppelmap.deeplink.action.MapAction;

/**
 * Created by Jonas on 10.08.2016.
 */
public class DeeplinkHandler {
    private static final String TAG = "DeeplinkHandler";
    private static final String BASE_URL_SHEME = "https://share.stoppelmap.de/";


    public Action handleIntent(Intent intent) {
        if (intent == null) {
            return null;
        }
        if (intent.getAction() == null) {
            return null;
        }
        String action = intent.getAction();
        Uri data = intent.getData();
        if (data == null) {
            return null;
        }

        if (data.getPath() == null) {
            return null;
        }

        String[] parts = data.getPath().split("/");
        if (parts.length <= 2) {
            return null;
        }


        switch (parts[1]) {
            case MapAction.IDENTIFIER:
                return new MapAction(parts);
        }

        return null;

    }

    public static String createShareUrl(String identifier, String... data) {
        StringBuilder builder = new StringBuilder(BASE_URL_SHEME);
        builder.append(identifier);
        for (String part : data) {
            builder.append("/");
            builder.append(part);
        }
        return builder.toString();
    }

}
