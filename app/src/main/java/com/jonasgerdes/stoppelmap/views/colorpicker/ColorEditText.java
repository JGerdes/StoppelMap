package com.jonasgerdes.stoppelmap.views.colorpicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 30.03.2017
 */

public class ColorEditText extends FrameLayout {

    @BindView(R.id.preview)
    CardView mPreview;

    @BindView(R.id.hex_input)
    EditText mInput;
    private int mColor;

    public ColorEditText(Context context) {
        super(context);
        init();
    }

    public ColorEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_color_edit_text, this, true);
        ButterKnife.bind(this);
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int color = Color.parseColor("#" + s.toString());
                    mColor = color;
                    mPreview.setCardBackgroundColor(color);
                } catch (Exception e) {
                    //ignore
                }
            }
        });

    }

    public void setColor(@ColorInt int color) {
        mColor = color;
        mPreview.setCardBackgroundColor(color);
        mInput.setText(StringUtil.getHexFromColor(color));
    }

    @ColorInt
    public int getColor() {
        return mColor;
    }
}
