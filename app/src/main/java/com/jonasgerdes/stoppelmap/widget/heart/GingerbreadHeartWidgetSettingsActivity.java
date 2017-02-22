package com.jonasgerdes.stoppelmap.widget.heart;

import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.BitmapUtil;
import com.jonasgerdes.stoppelmap.util.ViewUtil;
import com.jonasgerdes.stoppelmap.views.colorpicker.ColorPicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jonas on 31.01.2017.
 */

public class GingerbreadHeartWidgetSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GingerbreadHeartWidgetS";


    @BindView(R.id.widget_color_selection_list)
    ViewGroup mColorList;

    @BindView(R.id.widget_countdown)
    ImageView mPreviewFontLayer;

    @BindView(R.id.widget_gingerbread_heart_layer1)
    ImageView mPreviewLayer1;

    @BindView(R.id.widget_gingerbread_heart_layer2)
    ImageView mPreviewLayer2;

    @BindView(R.id.widget_gingerbread_heart_layer3)
    ImageView mPreviewLayer3;

    @BindView(R.id.previewBackground)
    ImageView mPreviewBackground;

    @BindView(R.id.color_secection_dominant)
    CardView mColorSelectionDominant;

    @BindView(R.id.color_secection_vibrant)
    CardView mColorSelectionVibrant;

    @BindView(R.id.color_secection_muted)
    CardView mColorSelectionMuted;

    @BindView(R.id.color_picker)
    ColorPicker mColorPicker;


    private int[] mCurrentColors = new int[3];
    private int mAppWidgetId;
    private Rect mTextBounds = new Rect();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.widget_settings_gingerbread_heart);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("StoppelMap");
        getSupportActionBar().setSubtitle("Countdown-Widget konfigurieren");

        for (int i = 0; i < mColorList.getChildCount(); i++) {
            mColorList.getChildAt(i).setOnClickListener(this);
        }

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        mPreviewBackground.setImageDrawable(wallpaperDrawable);

        Bitmap wallpaperBitmap = BitmapUtil.drawableToBitmap(wallpaperDrawable);
        Palette.from(wallpaperBitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(final Palette palette) {
                mColorSelectionDominant
                        .setCardBackgroundColor(palette.getDominantColor(Color.BLACK));
                mColorSelectionVibrant
                        .setCardBackgroundColor(palette.getVibrantColor(Color.BLACK));
                mColorSelectionMuted
                        .setCardBackgroundColor(palette.getMutedColor(Color.BLACK));

                mColorSelectionDominant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setColorsBy(palette.getDominantColor(Color.BLACK));
                        mColorPicker.setColor(mCurrentColors[1]);
                        updatePreview();
                    }
                });
                mColorSelectionVibrant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentColors[0] = palette.getLightVibrantColor(Color.BLACK);
                        mCurrentColors[1] = palette.getVibrantColor(Color.BLACK);
                        mCurrentColors[2] = palette.getDarkVibrantColor(Color.BLACK);
                        mColorPicker.setColor(mCurrentColors[1]);
                        updatePreview();
                    }
                });
                mColorSelectionMuted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentColors[0] = palette.getLightMutedColor(Color.BLACK);
                        mCurrentColors[1] = palette.getMutedColor(Color.BLACK);
                        mCurrentColors[2] = palette.getDarkMutedColor(Color.BLACK);
                        mColorPicker.setColor(mCurrentColors[1]);
                        updatePreview();
                    }
                });
            }
        });

        setColorsBy(Color.parseColor("#7d56c2"));
        updatePreview();

        mColorPicker.setChangeListener(new ColorPicker.ColorChangeListener() {
            @Override
            public void onColorChanged(int newColor) {
                setColorsBy(newColor);
                updatePreview();
            }
        }).setColor(mCurrentColors[1]);


    }

    @Override
    public void onClick(View view) {
        if (view instanceof CardView) {
            CardView colorCard = (CardView) view;
            int color = colorCard.getCardBackgroundColor().getDefaultColor();
            Log.d(TAG, "onClick: " + color);
            setColorsBy(color);
            mColorPicker.setColor(color);
            updatePreview();
        }
    }

    private void setColorsBy(int color) {
        float[] hsv1 = new float[3];
        Color.colorToHSV(color, hsv1);
        float[] hsv2 = new float[]{hsv1[0], hsv1[1], hsv1[2]};

        hsv1[1] = Math.max(0.1f, hsv1[1] - 0.4f);
        hsv1[2] = 0.75f;
        mCurrentColors[0] = Color.HSVToColor(hsv1);

        mCurrentColors[1] = color;

        hsv2[1] += 0.3f;
        hsv2[2] = Math.max(0.4f, hsv2[2] - 0.3f);
        mCurrentColors[2] = Color.HSVToColor(hsv2);
    }

    private void updatePreview() {
        mPreviewLayer1.setColorFilter(mCurrentColors[0]);
        mPreviewLayer2.setColorFilter(mCurrentColors[1]);
        mPreviewLayer3.setColorFilter(mCurrentColors[2]);

        Point size = new Point(ViewUtil.dpToPx(this, 256), ViewUtil.dpToPx(this, 206));
        Bitmap countdownBitmap = GingerbreadHeartWidgetProvider.createCountdownBitmap(
                this,
                GingerbreadHeartWidgetProvider.getCountDownStrings(),
                size,
                mTextBounds
        );
        mPreviewFontLayer.setImageBitmap(countdownBitmap);
    }

    @OnClick(R.id.fab)
    void saveSettings() {

        // Getting an instance of WidgetManager
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.widget_layout_gingerbread_heart);

        views.setInt(R.id.widget_gingerbread_heart_layer1, "setColorFilter", mCurrentColors[0]);
        views.setInt(R.id.widget_gingerbread_heart_layer2, "setColorFilter", mCurrentColors[1]);
        views.setInt(R.id.widget_gingerbread_heart_layer3, "setColorFilter", mCurrentColors[2]);

        // Tell the AppWidgetManager to perform an update on the app widget
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        // Return RESULT_OK from this activity
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
