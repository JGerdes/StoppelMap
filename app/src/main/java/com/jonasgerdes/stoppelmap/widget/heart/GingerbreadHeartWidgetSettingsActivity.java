package com.jonasgerdes.stoppelmap.widget.heart;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.jonasgerdes.stoppelmap.R;

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

    @BindView(R.id.widget_gingerbread_heart_layer1)
    ImageView mPreviewLayer1;

    @BindView(R.id.widget_gingerbread_heart_layer2)
    ImageView mPreviewLayer2;

    @BindView(R.id.widget_gingerbread_heart_layer3)
    ImageView mPreviewLayer3;


    private int mCurrentColor;
    private int mAppWidgetId;


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

        for (int i = 0; i < mColorList.getChildCount(); i++) {
            mColorList.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof CardView) {
            CardView colorCard = (CardView) view;
            int color = colorCard.getCardBackgroundColor().getDefaultColor();
            Log.d(TAG, "onClick: " + color);
            mCurrentColor = color;

            mPreviewLayer1.setColorFilter(mCurrentColor);
            mPreviewLayer2.setColorFilter(mCurrentColor);
            mPreviewLayer3.setColorFilter(mCurrentColor);
        }
    }

    @OnClick(R.id.fab)
    void saveSettings() {

        // Getting an instance of WidgetManager
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
        RemoteViews views = new RemoteViews(getPackageName(),
                R.layout.widget_layout_gingerbread_heart);

        views.setInt(R.id.widget_gingerbread_heart_layer1, "setColorFilter", mCurrentColor);
        views.setInt(R.id.widget_gingerbread_heart_layer2, "setColorFilter", mCurrentColor);
        views.setInt(R.id.widget_gingerbread_heart_layer3, "setColorFilter", mCurrentColor);

        // Tell the AppWidgetManager to perform an update on the app widget
        appWidgetManager.updateAppWidget(mAppWidgetId, views);

        // Return RESULT_OK from this activity
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}
