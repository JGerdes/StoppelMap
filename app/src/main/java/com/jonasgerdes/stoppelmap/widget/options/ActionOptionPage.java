package com.jonasgerdes.stoppelmap.widget.options;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.widget.ActionWidgetPreview;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by jonas on 23.02.2017.
 */

public class ActionOptionPage extends OptionPage<ActionWidgetPreview> {

    @BindViews({
            R.id.action_none,
            R.id.action_edit_widget,
            //R.id.action_open_bus,
            R.id.action_open_map
    })
    List<RadioButton> mActionRadioButtons;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.widget_settings_action_selection_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        for (final RadioButton actionRadioButton : mActionRadioButtons) {
            actionRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        getEditableWidgetPreview().setAction(actionRadioButton.getId());
                        getWidgetPreview().update();
                    }
                }
            });
        }

    }

}
