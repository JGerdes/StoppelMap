package com.jonasgerdes.stoppelmap.widget.silhouette;

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
import android.widget.RemoteViews;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import com.jonasgerdes.stoppelmap.widget.R;
import com.jonasgerdes.stoppelmap.widget.ActionIntentFactory;
import com.jonasgerdes.stoppelmap.widget.Font;
import com.jonasgerdes.stoppelmap.widget.WidgetSettingsHelper;
import com.jonasgerdes.stoppelmap.widget.util.ViewUtil;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jonas on 07.06.2016.
 */
public class SilhouetteWidgetProvider extends AppWidgetProvider {

    public static final String SETTING_SHOW_HOUR = "setting_show_hour";
    public static final String SETTING_COLOR = "setting_color";
    public static final String SETTING_FONT_COLOR = "setting_font_color";
    public static final String SETTING_FONT = "setting_font";
    public static final String SETTING_ACTION = "setting_action";


    public static final int DEFAULT_FONT_COLOR = 0xfff4f4f4;

    // TODO: 28.02.2017 use string ressources
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
        int color = WidgetSettingsHelper.getInt(context, appWidgetId, SETTING_COLOR, Color.BLACK);
        int fontColor = WidgetSettingsHelper.getInt(context, appWidgetId, SETTING_FONT_COLOR,
                DEFAULT_FONT_COLOR);
        String fontFile = WidgetSettingsHelper.getString(context, appWidgetId,
                SETTING_FONT, Font.RobotoSlab.getFileName());
        int action = WidgetSettingsHelper.getInt(context, appWidgetId,
                SETTING_ACTION, R.id.action_open_map);

        RemoteViews views = initWidget(
                context,
                appWidgetId,
                WidgetSettingsHelper.getBoolean(context, appWidgetId, SETTING_SHOW_HOUR, true),
                color,
                Font.fromStringOrNull(fontFile),
                fontColor,
                action
        );

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @NonNull
    public RemoteViews initWidget(Context context, int appWidgetId, boolean showHours, int color,
                                  Font font, int fontColor, @IdRes int actionId) {
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout_silhouette);

        Point size = new Point(ViewUtil.dpToPx(context, 256), ViewUtil.dpToPx(context, 206));
        Bitmap countdownBitmap = createCountdownBitmap(context, getCountDownStrings(), size,
                mTextBounds, showHours, font, fontColor);
        views.setImageViewBitmap(R.id.widget_countdown, countdownBitmap);

        PendingIntent intent = ActionIntentFactory.createActionIntent(
                context, actionId, appWidgetId, SilhouetteWidgetSettingsActivity.class
        );
        views.setOnClickPendingIntent(R.id.widget_countdown, intent);

        views.setInt(R.id.widget_silhouette, "setColorFilter", color);
        return views;
    }


    public static Bitmap createCountdownBitmap(Context context, String[] texts, Point size,
                                               Rect textBounds, boolean showHours, Font font, int fontColor) {
        Bitmap bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        float centerOffset = 0.065f * size.y;
        float countDownSize = size.y / 10f;
        float daysYPosition = size.y * 0.7f;
        if (!showHours) {
            daysYPosition = size.y * 0.76f;
            countDownSize = size.y / 6.5f;
        }

        if (Font.Damion.equals(font)) {
            countDownSize *= 1.2f;
        }

        Paint paint = new Paint();
        paint.setTypeface(font.asTypeface(context));
        paint.setAntiAlias(true);
        paint.setSubpixelText(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(fontColor);

        paint.setTextSize(size.y / 15f);
        String remain = "noch";
        paint.getTextBounds(remain, 0, remain.length(), textBounds);
        canvas.drawText(remain, size.x / 2f - textBounds.exactCenterX() + centerOffset,
                size.y * 0.55f, paint);

        paint.setTextSize(countDownSize);
        paint.getTextBounds(texts[0], 0, texts[0].length(), textBounds);
        canvas.drawText(texts[0], size.x / 2f - textBounds.exactCenterX() + centerOffset,
                daysYPosition, paint);

        if (showHours) {
            paint.setTextSize(size.y / 12f);
            paint.getTextBounds(texts[1], 0, texts[1].length(), textBounds);
            canvas.drawText(texts[1], size.x / 2f - textBounds.exactCenterX() + centerOffset,
                    size.y * 0.8f, paint);
        }

        return bitmap;
    }

    // TODO: 28.02.2017 extract common code for countdown creation
    public static String[] getCountDownStrings() {
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
            return new String[]{"Stoppelmarkt", ""};
        }


        String year = String.valueOf(stomaStart.getYear() + 1900);

        if (delta <= TimeUnit.HOURS.toMillis(1) && delta >= 0) {
            // TODO: 28.02.2017 extract string ressources 
            return new String[]{"Ganz bald", ""};
        } else if (delta <= 0) {
            return new String[]{"Viel Spaß!", ""};
        } else {
            long days = delta / TimeUnit.DAYS.toMillis(1);
            delta %= TimeUnit.DAYS.toMillis(1);

            long hours = delta / TimeUnit.HOURS.toMillis(1);
            delta %= TimeUnit.HOURS.toMillis(1);

            long minutes = delta / TimeUnit.MINUTES.toMillis(1);
            delta %= TimeUnit.MINUTES.toMillis(1);


            String daysLeft;
            String hoursLeft = "";
            if (days > 0) {
                daysLeft = getFormatedUnitString(days, UNIT_DESC_DAY);
                hoursLeft = getFormatedUnitString(hours, UNIT_DESC_HOUR);
            } else {
                daysLeft = getFormatedUnitString(hours, UNIT_DESC_HOUR);
            }

            return new String[]{daysLeft, hoursLeft};
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
