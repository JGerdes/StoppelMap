package com.jonasgerdes.stoppelmap.widget.silhouette;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.ViewUtil;
import com.jonasgerdes.stoppelmap.widget.ActionWidgetPreview;
import com.jonasgerdes.stoppelmap.widget.ChangeableFontWidgetPreview;
import com.jonasgerdes.stoppelmap.widget.ColorableFontWidgetPreview;
import com.jonasgerdes.stoppelmap.widget.ColorableWidgetPreview;
import com.jonasgerdes.stoppelmap.widget.WidgetPreview;
import com.jonasgerdes.stoppelmap.widget.WidgetSettingsHelper;

import butterknife.BindView;

/**
 * Created by jonas on 23.02.2017.
 */

public class SilhouettePreview extends WidgetPreview
        implements ColorableWidgetPreview, ChangeableFontWidgetPreview,
        ColorableFontWidgetPreview, ActionWidgetPreview {


    @BindView(R.id.widget_countdown)
    ImageView mPreviewFontLayer;

    @BindView(R.id.widget_silhouette)
    ImageView mPreviewLayer;

    private int mCurrentColor;
    private Rect mTextBounds = new Rect();
    private boolean mShowHours = false;
    private String mFontFile = SilhouetteWidgetProvider.FONT_ROBOTO_SLAB;
    private int mFontColor = SilhouetteWidgetProvider.DEFAULT_FONT_COLOR;
    @IdRes
    private int mAction;

    public SilhouettePreview(Context context) {
        super(context);
    }

    public SilhouettePreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SilhouettePreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SilhouettePreview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    @LayoutRes
    protected int getLayout() {
        return R.layout.widget_layout_silhouette;
    }


    @Override
    public void update() {
        mPreviewLayer.setColorFilter(mCurrentColor);

        Point size = new Point(
                ViewUtil.dpToPx(getContext(), 256),
                ViewUtil.dpToPx(getContext(), 206)
        );
        Bitmap countdownBitmap = SilhouetteWidgetProvider.createCountdownBitmap(
                getContext(),
                SilhouetteWidgetProvider.getCountDownStrings(),
                size,
                mTextBounds,
                mShowHours,
                mFontFile,
                mFontColor
        );
        mPreviewFontLayer.setImageBitmap(countdownBitmap);
    }

    @Override
    public void setColorsBy(int color) {
        mCurrentColor = color;
    }

    @Override
    public void setColors(int[] colors) {
        mCurrentColor = colors[0];
    }

    @Override
    public void saveSettings(WidgetSettingsHelper settingsHelper) {
        settingsHelper.putBoolean(SilhouetteWidgetProvider.SETTING_SHOW_HOUR, mShowHours);
        settingsHelper.putInt(SilhouetteWidgetProvider.SETTING_COLOR, mCurrentColor);
        settingsHelper.putString(SilhouetteWidgetProvider.SETTING_FONT, mFontFile);
        settingsHelper.putInt(SilhouetteWidgetProvider.SETTING_FONT_COLOR, mFontColor);
        settingsHelper.putInt(SilhouetteWidgetProvider.SETTING_ACTION, mAction);
    }

    @Override
    public RemoteViews createWidget() {
        return new SilhouetteWidgetProvider().initWidget(
                getContext(),
                mWidgetId,
                mShowHours,
                mCurrentColor,
                mFontFile,
                mFontColor,
                mAction
        );
    }

    @Override
    public void setShowHours(boolean showHours) {
        mShowHours = showHours;
    }

    @Override
    public void setFont(String fontFile) {
        mFontFile = fontFile;
    }

    @Override
    public void setFontColor(int color) {
        mFontColor = color;
    }

    @Override
    public void setAction(@IdRes int actionId) {
        mAction = actionId;
    }
}
