package com.jonasgerdes.stoppelmap.widget.options;



import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.jonasgerdes.stoppelmap.widget.AbstractWidgetSettingsActivity;
import com.jonasgerdes.stoppelmap.widget.WidgetPreview;

/**
 * Created by jonas on 23.02.2017.
 */
public abstract class OptionPage<T> extends Fragment {
    public WidgetPreview getWidgetPreview() {
        return  ((AbstractWidgetSettingsActivity) getActivity())
                .getWidgetPreview();
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public T getEditableWidgetPreview() {
        return (T) getWidgetPreview();
    }
}
