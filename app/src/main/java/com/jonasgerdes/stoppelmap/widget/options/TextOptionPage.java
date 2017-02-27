package com.jonasgerdes.stoppelmap.widget.options;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.widget.ChangeableFontWidgetPreview;
import com.jonasgerdes.stoppelmap.widget.silhouette.SilhouetteWidgetProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 23.02.2017.
 */

public class TextOptionPage extends OptionPage<ChangeableFontWidgetPreview> {


    @BindView(R.id.toggle_hours)
    CheckBox mHourToggle;

    @BindView(R.id.font_roboto)
    RadioButton mFontSelectionRoboto;

    @BindView(R.id.font_roboto_slab)
    RadioButton mFontSelectionRobotoSlab;

    @BindView(R.id.font_damion)
    RadioButton mFontSelectionDamion;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.widget_settings_font_selection_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mHourToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getEditableWidgetPreview().setShowHours(isChecked);
                getWidgetPreview().update();
            }
        });

        setUpRadioButton(mFontSelectionRoboto, SilhouetteWidgetProvider.FONT_ROBOTO);
        setUpRadioButton(mFontSelectionRobotoSlab, SilhouetteWidgetProvider.FONT_ROBOTO_SLAB);
        setUpRadioButton(mFontSelectionDamion, SilhouetteWidgetProvider.FONT_DAMION);

    }

    private void setUpRadioButton(RadioButton button, final String fontFile) {
        button.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                "font/" + fontFile)
        );
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getEditableWidgetPreview().setFont(fontFile);
                    getWidgetPreview().update();
                }
            }
        });
    }

}
