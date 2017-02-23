package com.jonasgerdes.stoppelmap.widget.options;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.widget.HourTogglableWidgetPreview;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jonas on 23.02.2017.
 */

public class HourOptionPage extends OptionPage<HourTogglableWidgetPreview> {


    @BindView(R.id.toggle_hours)
    CheckBox mHourToggle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.widget_settings_hour_position_page, container, false);
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

    }

}
