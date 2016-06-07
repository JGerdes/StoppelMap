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

        Point size = new Point(ViewUtil.dpToPx(context, 256), ViewUtil.dpToPx(context, 206));
        Bitmap countdownBitmap = createCountdownBitmap(context, getCountDownString(), size);
        views.setImageViewBitmap(R.id.widget_countdown, countdownBitmap);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_countdown, pendingIntent);
        
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private Bitmap createCountdownBitmap(Context context, String text, Point size) {
        Bitmap bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        Typeface clock = Typeface.createFromAsset(context.getAssets(), "font/Damion-Regular.ttf");
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setTypeface(clock);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(size.y / 8f);
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text, size.x / 2f - textBounds.exactCenterX(), size.y * 0.4f, paint);

        return bitmap;
    }

    private String getCountDownString() {
        Date now = new Date();
        Date stomaStart = new Date(116, 7, 11, 18, 30);

        long delta = stomaStart.getTime() - now.getTime();
        if (delta <= TimeUnit.HOURS.toMillis(1) && delta >= 0) {
            return "Wenige Augenblicke";
        } else if (delta <= 0) {
            return "Viel Spaß!";
        } else {
            long days = delta / TimeUnit.DAYS.toMillis(1);
            delta %= TimeUnit.DAYS.toMillis(1);

            long hours = delta / TimeUnit.HOURS.toMillis(1);
            delta %= TimeUnit.HOURS.toMillis(1);

            long minutes = delta / TimeUnit.MINUTES.toMillis(1);
            delta %= TimeUnit.MINUTES.toMillis(1);

            return getFormatedUnitString(days, UNIT_DESC_DAY)
                    + ", "
                    + getFormatedUnitString(hours, UNIT_DESC_HOUR);
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
