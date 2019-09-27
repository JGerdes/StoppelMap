package com.jonasgerdes.stoppelmap.widget.options;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.Nullable;
import com.jonasgerdes.stoppelmap.widget.R;
import com.jonasgerdes.stoppelmap.widget.ChangeableFontWidgetPreview;
import com.jonasgerdes.stoppelmap.widget.Font;

/**
 * Created by jonas on 23.02.2017.
 */

public class TextOptionPage extends OptionPage<ChangeableFontWidgetPreview> {


    public static final String PARAM_DEFAULT_FONT = "PARAM_DEFAULT_FONT";
    public static final String PARAM_SHOW_HOURS = "PARAM_SHOW_HOURS";

    CheckBox mHourToggle;
    RadioGroup mFontSelection;
    RadioButton mFontSelectionRoboto;
    RadioButton mFontSelectionRobotoSlab;
    RadioButton mFontSelectionDamion;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.widget_settings_font_selection_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHourToggle = view.findViewById(R.id.toggle_hours);
        mFontSelection = view.findViewById(R.id.font_selection);
        mFontSelectionRoboto = view.findViewById(R.id.font_roboto);
        mFontSelectionRobotoSlab = view.findViewById(R.id.font_roboto_slab);
        mFontSelectionDamion = view.findViewById(R.id.font_damion);

        mHourToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getEditableWidgetPreview().setShowHours(isChecked);
                getWidgetPreview().update();
            }
        });

        mHourToggle.setChecked(
                getArguments() != null && getArguments().getBoolean(PARAM_SHOW_HOURS, false)
        );

        Font defaultFont = null;
        if (getArguments() != null && getArguments().containsKey(PARAM_DEFAULT_FONT)) {
            defaultFont = Font.fromStringOrNull(getArguments().getString(PARAM_DEFAULT_FONT));
        }

        setUpRadioButton(mFontSelectionRoboto, Font.Roboto, defaultFont);
        setUpRadioButton(mFontSelectionRobotoSlab, Font.RobotoSlab, defaultFont);
        setUpRadioButton(mFontSelectionDamion, Font.Damion, defaultFont);


    }

    private void setUpRadioButton(RadioButton button, final Font font, final Font
            defaultFont) {
        button.setTypeface(font.asTypeface(getContext()));
        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ChangeableFontWidgetPreview preview = getEditableWidgetPreview();
                if (isChecked && preview != null) {
                    preview.setFont(font);
                    getWidgetPreview().update();
                }
            }
        });

        if (font.equals(defaultFont)) {
            mFontSelection.check(button.getId());
        }
    }

    public static TextOptionPage newInstance(Font defaultFont, boolean showHours) {

        Bundle args = new Bundle();
        args.putString(PARAM_DEFAULT_FONT, defaultFont.getFileName());
        args.putBoolean(PARAM_SHOW_HOURS, showHours);
        TextOptionPage fragment = new TextOptionPage();
        fragment.setArguments(args);
        return fragment;
    }

}
