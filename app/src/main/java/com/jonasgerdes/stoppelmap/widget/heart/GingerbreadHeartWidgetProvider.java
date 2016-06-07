package com.jonasgerdes.stoppelmap.widget.heart;

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
import android.widget.RemoteViews;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.ViewUtil;

/**
 * Created by Jonas on 07.06.2016.
 */
public class GingerbreadHeartWidgetProvider extends AppWidgetProvider {

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
        views.setImageViewBitmap(R.id.widget_countdown, createCountdownBitmap(context, "64 Tage, 23 Stunden", size));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public Bitmap createCountdownBitmap(Context context, String text, Point size) {
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
}
