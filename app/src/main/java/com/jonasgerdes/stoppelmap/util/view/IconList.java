package com.jonasgerdes.stoppelmap.util.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.widget.util.ViewUtil;

import java.util.List;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09-Aug-17
 */

public class IconList extends LinearLayout {

    int iconTint = R.color.text_dark;
    int iconSize = 16;
    int iconMargin = 2;

    public IconList(Context context) {
        super(context);
    }

    public IconList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IconList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IconList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setIcons(List<Integer> icons) {
        removeAllViews();
        int iconSizePx = ViewUtil.dpToPx(getContext(), iconSize);
        int iconMarginPx = ViewUtil.dpToPx(getContext(), iconMargin);
        int color = ContextCompat.getColor(getContext(), iconTint);
        for (Integer icon : icons) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(iconSizePx, iconSizePx);
            params.setMargins(iconMarginPx, iconMarginPx, iconMarginPx, iconMarginPx);
            AppCompatImageView image = new AppCompatImageView(getContext());
            image.setLayoutParams(params);
            Drawable iconDrawable = VectorDrawableCompat.create(getResources(), icon, null);
            image.setImageDrawable(iconDrawable);
            image.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            addView(image);
        }
        if (icons.isEmpty()) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
    }

    public int getIconTint() {
        return iconTint;
    }

    public void setIconTint(int iconTint) {
        this.iconTint = iconTint;
    }

    public int getIconSize() {
        return iconSize;
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
    }

    public int getIconMargin() {
        return iconMargin;
    }

    public void setIconMargin(int iconMargin) {
        this.iconMargin = iconMargin;
    }
}
