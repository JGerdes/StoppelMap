package com.jonasgerdes.stoppelmap.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;

import com.jonasgerdes.stoppelmap.MainActivity;
import com.jonasgerdes.stoppelmap.R;

/**
 * Created by jonas on 09.03.2017.
 */

public class ActionIntentFactory {

    public static PendingIntent createActionIntent(Context context,
                                                   @IdRes int actionId,
                                                   int appWidgetId,
                                                   Class settingActivityClass) {

        Intent intent;
        switch (actionId) {
            case R.id.action_none:
                return null;

            case R.id.action_open_map:
                intent = new Intent(context, MainActivity.class);
                break;

            /*case R.id.action_open_bus:
                intent = new Intent(context, MainActivity.class);
                break;*/

            case R.id.action_edit_widget:
                intent = new Intent(context, settingActivityClass);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                intent.setData(Uri.withAppendedPath(Uri.parse("abc" + "://widget/id/"), String.valueOf(appWidgetId)));
                break;

            default:
                intent = new Intent(context, MainActivity.class);
        }

        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}
