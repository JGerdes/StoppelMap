package com.jonasgerdes.stoppelmap.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.IdRes;
import com.jonasgerdes.stoppelmap.core.routing.Route;
import com.jonasgerdes.stoppelmap.core.routing.Router;
import com.jonasgerdes.stoppelmap.widget.options.ActionOptionPage;

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
            case ActionOptionPage.ACTION_NONE:
                return null;


            case ActionOptionPage.ACTION_OPEN_MAP:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.stoppelmap.de/map"));
                break;

            /*case R.id.action_open_bus:
                intent = new Intent(context, MainActivity.class);
                break;*/

            case ActionOptionPage.ACTION_EDIT_WIDGET:
                intent = new Intent(context, settingActivityClass);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                intent.setData(Uri.withAppendedPath(Uri.parse("abc" + "://widget/id/"), String.valueOf(appWidgetId)));
                break;

            default:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://open.stoppelmap.de"));
        }

        return PendingIntent.getActivity(context, 0, intent, 0);
    }
}
