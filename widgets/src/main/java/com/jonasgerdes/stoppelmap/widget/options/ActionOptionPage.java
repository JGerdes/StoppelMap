package com.jonasgerdes.stoppelmap.widget.options;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import com.jonasgerdes.stoppelmap.widget.R;
import com.jonasgerdes.stoppelmap.widget.ActionWidgetPreview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonas on 23.02.2017.
 */

public class ActionOptionPage extends OptionPage<ActionWidgetPreview> {

    public static final int ACTION_NONE = 0;
    public static final int ACTION_EDIT_WIDGET = 1;
    public static final int ACTION_OPEN_MAP = 2;

    public static final String PARAM_DEFAUT_ACTION = "PARAM_DEFAUT_ACTION";
    private List<RadioButton> mActionRadioButtons;
    private RadioGroup mActionSelection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.widget_settings_action_selection_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActionRadioButtons = new ArrayList<>();
        mActionRadioButtons.add((RadioButton) view.findViewById(R.id.action_none));
        mActionRadioButtons.add((RadioButton) view.findViewById(R.id.action_edit_widget));
        mActionRadioButtons.add((RadioButton) view.findViewById(R.id.action_open_map));
        mActionSelection = view.findViewById(R.id.action_selection);

        int defaultAction = ACTION_EDIT_WIDGET;
        if (getArguments() != null && getArguments().containsKey(PARAM_DEFAUT_ACTION)) {
            defaultAction = getArguments().getInt(PARAM_DEFAUT_ACTION);
        }
        for (final RadioButton actionRadioButton : mActionRadioButtons) {
            actionRadioButton.setOnCheckedChangeListener(new CompoundButton
                    .OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ActionWidgetPreview editablePreview = getEditableWidgetPreview();
                    if (isChecked && editablePreview != null) {
                        //don't use buttons view id as id for action because view ids can change
                        //in a new build and action is persistent in shared prefs
                        editablePreview.setAction(getAction(buttonView));
                        getWidgetPreview().update();
                    }
                }
            });
            if (getAction(actionRadioButton) == defaultAction) {
                mActionSelection.check(actionRadioButton.getId());
            }
        }
    }

    /**
     * Maps view (by its id) to action id
     *
     * @param buttonView
     * @return action id of virew
     */
    private int getAction(CompoundButton buttonView) {
        int id = buttonView.getId();
        if (id == R.id.action_none) {
            return ACTION_NONE;
        } else if (id == R.id.action_edit_widget) {
            return ACTION_EDIT_WIDGET;
        } else if (id == R.id.action_open_map) {
            return ACTION_OPEN_MAP;
        }
        return ACTION_EDIT_WIDGET;
    }

    public static ActionOptionPage newInstance(@IdRes int actionId) {

        Bundle args = new Bundle();
        args.putInt(PARAM_DEFAUT_ACTION, actionId);
        ActionOptionPage fragment = new ActionOptionPage();
        fragment.setArguments(args);
        return fragment;
    }

}
