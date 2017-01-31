package com.jonasgerdes.stoppelmap.widget.heart;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.jonasgerdes.stoppelmap.MainActivity;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.ViewUtil;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jonas on 07.06.2016.
 */
public class GingerbreadHeartWidgetProvider extends AppWidgetProvider {

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

    private final Rect textBounds = new Rect();

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
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout_gingerbread_heart);

        views.setInt(R.id.widget_gingerbread_heart_layer1, "setColorFilter", Color.BLUE);
        views.setInt(R.id.widget_gingerbread_heart_layer2, "setColorFilter", Color.GREEN);
        views.setInt(R.id.widget_gingerbread_heart_layer3, "setColorFilter", Color.CYAN);

        Point size = new Point(ViewUtil.dpToPx(context, 256), ViewUtil.dpToPx(context, 206));
        Bitmap countdownBitmap = createCountdownBitmap(context, getCountDownStrings(), size);
        views.setImageViewBitmap(R.id.widget_countdown, countdownBitmap);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_countdown, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private Bitmap createCountdownBitmap(Context context, String[] texts, Point size) {
        Bitmap bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        Typeface font = Typeface.createFromAsset(context.getAssets(), "font/Damion-Regular.ttf");
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(font);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(244, 244, 244));
        paint.setTextSize(size.y / 8f);
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

    private String[] getCountDownStrings() {
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
                timeLeft = getFormatedUnitString(days, UNIT_DESC_DAY)
                        + ", "
                        + getFormatedUnitString(hours, UNIT_DESC_HOUR);
            } else {
                timeLeft = getFormatedUnitString(hours, UNIT_DESC_HOUR);
            }

            return new String[]{timeLeft, "bis zum", year};
        }

    }

    private String getFormatedUnitString(long amount, String[] unitDescriptor) {
        String unit;
        if (amount == 1) {
            unit = unitDescriptor[0];
        } else {
            unit = unitDescriptor[1];
        }
        return String.format(Locale.GERMAN, "%d %s", amount, unit);
    }


}
