package com.jonasgerdes.stoppelmap.widget.heart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 23.02.2017.
 */

public class GingerbreadHeartPreview extends FrameLayout implements ColorableWidgetPreview {


    @BindView(R.id.widget_countdown)
    ImageView mPreviewFontLayer;

    @BindView(R.id.widget_gingerbread_heart_layer1)
    ImageView mPreviewLayer1;

    @BindView(R.id.widget_gingerbread_heart_layer2)
    ImageView mPreviewLayer2;

    @BindView(R.id.widget_gingerbread_heart_layer3)
    ImageView mPreviewLayer3;

    private int[] mCurrentColors = new int[3];
    private Rect mTextBounds = new Rect();

    public GingerbreadHeartPreview(Context context) {
        super(context);
        init();
    }

    public GingerbreadHeartPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GingerbreadHeartPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GingerbreadHeartPreview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_layout_gingerbread_heart, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    public void update() {
        mPreviewLayer1.setColorFilter(mCurrentColors[0]);
        mPreviewLayer2.setColorFilter(mCurrentColors[1]);
        mPreviewLayer3.setColorFilter(mCurrentColors[2]);

        Point size = new Point(
                ViewUtil.dpToPx(getContext(), 256),
                ViewUtil.dpToPx(getContext(), 206)
        );
        Bitmap countdownBitmap = GingerbreadHeartWidgetProvider.createCountdownBitmap(
                getContext(),
                GingerbreadHeartWidgetProvider.getCountDownStrings(),
                size,
                mTextBounds
        );
        mPreviewFontLayer.setImageBitmap(countdownBitmap);
    }

    @Override
    public void setColorsBy(int color) {
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

    @Override
    public void setColors(int[] colors) {
        mCurrentColors = colors;
    }

    public void applyToWidget(RemoteViews views) {
        views.setInt(R.id.widget_gingerbread_heart_layer1, "setColorFilter", mCurrentColors[0]);
        views.setInt(R.id.widget_gingerbread_heart_layer2, "setColorFilter", mCurrentColors[1]);
        views.setInt(R.id.widget_gingerbread_heart_layer3, "setColorFilter", mCurrentColors[2]);
    }
}
