package com.jonasgerdes.stoppelmap.widget.heart;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.RemoteViews;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.widget.ActionIntentFactory;
import com.jonasgerdes.stoppelmap.widget.WidgetSettingsHelper;
import com.jonasgerdes.stoppelmap.widget.util.ViewUtil;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jonas on 07.06.2016.
 */
public class GingerbreadHeartWidgetProvider extends AppWidgetProvider {

    public static final String SETTING_SHOW_HOUR = "setting_show_hour";
    public static final String SETTING_COLOR_1 = "setting_color_1";
    public static final String SETTING_COLOR_2 = "setting_color_2";
    public static final String SETTING_COLOR_3 = "setting_color_3";
    public static final String SETTING_ACTION = "setting_action";

    private static final String[] UNIT_DESC_DAY = {"Tag", "Tage"};
    private static final String[] UNIT_DESC_HOUR = {"Stunde", "Stunden"};
    private static final Date[] NEXT_DATES = {
            new Date(116, 7, 11, 18, 0),
            new Date(117, 7, 10, 18, 0),
            new Date(118, 7, 16, 18, 0),
            new Date(119, 7, 15, 18, 0),
            new Date(120, 7, 13, 18, 0)
    };
    private static final long STOMA_DURATION = TimeUnit.DAYS.toMillis(5) + TimeUnit.HOURS.toMillis(4);

    private final Rect mTextBounds = new Rect();

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int widgetCount = appWidgetIds.length;

        for (int i = 0; i < widgetCount; i++) {
            updateWidget(context, appWidgetManager, appWidgetIds[i]);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateWidget(context, appWidgetManager, appWidgetId);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }


    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        int[] colors = new int[]{
                WidgetSettingsHelper.getInt(context, appWidgetId, SETTING_COLOR_1, 0xD1C4E9),
                WidgetSettingsHelper.getInt(context, appWidgetId, SETTING_COLOR_2, 0x7E57C2),
                WidgetSettingsHelper.getInt(context, appWidgetId, SETTING_COLOR_3, 0x311B92)
        };
        int action = WidgetSettingsHelper.getInt(context, appWidgetId,
                SETTING_ACTION, R.id.action_open_map);

        RemoteViews views = initWidget(
                context,
                appWidgetId,
                WidgetSettingsHelper.getBoolean(context, appWidgetId, SETTING_SHOW_HOUR, true),
                colors,
                action
        );

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @NonNull
    public RemoteViews initWidget(Context context, int appWidgetId, boolean showHours, int[] colors,
                                  @IdRes int actionId) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout_gingerbread_heart);

        Point size = new Point(ViewUtil.dpToPx(context, 256), ViewUtil.dpToPx(context, 206));
        Bitmap countdownBitmap = createCountdownBitmap(context, getCountDownStrings(showHours), size, mTextBounds, showHours);
        views.setImageViewBitmap(R.id.widget_countdown, countdownBitmap);

        PendingIntent intent = ActionIntentFactory.createActionIntent(
                context, actionId, appWidgetId, GingerbreadHeartWidgetSettingsActivity.class
        );
        views.setOnClickPendingIntent(R.id.widget_countdown, intent);

        views.setInt(R.id.widget_gingerbread_heart_layer1, "setColorFilter", colors[0]);
        views.setInt(R.id.widget_gingerbread_heart_layer2, "setColorFilter", colors[1]);
        views.setInt(R.id.widget_gingerbread_heart_layer3, "setColorFilter", colors[2]);
        return views;
    }

    public static Bitmap createCountdownBitmap(Context context, String[] texts, Point size, Rect textBounds, boolean showHours) {
        Bitmap bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        float countDownSize = size.y / 9f;
        if (!showHours) {
            countDownSize = size.y / 5f;
        }

        Paint paint = new Paint();
        Typeface font = ResourcesCompat.getFont(context, R.font.damion);
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(font);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(244, 244, 244));
        paint.setTextSize(countDownSize);
        paint.getTextBounds(texts[0], 0, texts[0].length(), textBounds);
        canvas.drawText(texts[0], size.x / 2f - textBounds.exactCenterX(), size.y * 0.4f, paint);

        paint.setTextSize(size.y / 14f);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(texts[1], size.x / 2f, size.y * 0.55f, paint);

        paint.setTextSize(size.y / 14f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(texts[2], size.x * 0.53f, size.y * 0.7f, paint);

        return bitmap;
    }

    public static String[] getCountDownStrings(boolean withHours) {
        Date now = new Date();
        Date stomaStart = NEXT_DATES[0];
        long delta = stomaStart.getTime() - now.getTime();
        //find next stoma date
        for (int i = 1; i < NEXT_DATES.length && delta < -1 * STOMA_DURATION; i++) {
            stomaStart = NEXT_DATES[i];
            delta = stomaStart.getTime() - now.getTime();
        }

        //past the list
        if (delta < -1 * STOMA_DURATION) {
            String year = String.valueOf(now.getYear() + 1900);
            //after august, take next year
            if (now.getMonth() > 7) {
                year = String.valueOf(stomaStart.getYear() + 1901);
            }
            return new String[]{"Wir sehn' uns", "auf dem", year};
        }


        String year = String.valueOf(stomaStart.getYear() + 1900);

        if (delta <= TimeUnit.HOURS.toMillis(1) && delta >= 0) {
            return new String[]{"Wenige Augenblicke", "bis zum", year};
        } else if (delta <= 0) {
            return new String[]{"Viel Spaß", "auf dem", year};
        } else {
            long days = delta / TimeUnit.DAYS.toMillis(1);
            delta %= TimeUnit.DAYS.toMillis(1);

            long hours = delta / TimeUnit.HOURS.toMillis(1);
            delta %= TimeUnit.HOURS.toMillis(1);

            long minutes = delta / TimeUnit.MINUTES.toMillis(1);
            delta %= TimeUnit.MINUTES.toMillis(1);


            String timeLeft;
            if (days > 0) {
                timeLeft = getFormatedUnitString(days, UNIT_DESC_DAY);
                if (withHours) {
                    timeLeft += ", " + getFormatedUnitString(hours, UNIT_DESC_HOUR);
                }
            } else {
                timeLeft = getFormatedUnitString(hours, UNIT_DESC_HOUR);
            }

            return new String[]{timeLeft, "bis zum", year};
        }

    }

    private static String getFormatedUnitString(long amount, String[] unitDescriptor) {
        String unit;
        if (amount == 1) {
            unit = unitDescriptor[0];
        } else {
            unit = unitDescriptor[1];
        }
        return String.format(Locale.GERMAN, "%d %s", amount, unit);
    }


}
