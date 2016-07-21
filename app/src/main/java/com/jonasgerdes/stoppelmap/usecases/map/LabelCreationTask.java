package com.jonasgerdes.stoppelmap.usecases.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.Icon;
import com.jonasgerdes.stoppelmap.model.map.MapEntity;
import com.jonasgerdes.stoppelmap.model.shared.RealmString;

import java.util.List;

/**
 * Created by Jonas on 07.08.2015.
 */
public class LabelCreationTask extends AsyncTask<List<MapEntity>, Void, Void> {

    public interface OnReadyListener {
        void onReady();
    }

    private Context mContext;
    private OnReadyListener mOnReadyListener;

    public LabelCreationTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(List<MapEntity>... params) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        Bitmap icon;
        for (MapEntity entity : params[0]) {
            icon = createLabel(inflater, entity.getName(), entity.getIcons());
            int w = icon.getWidth(),
                    h = icon.getHeight();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(entity.getOrigin().toLatLng())
                    .icon(BitmapDescriptorFactory.fromBitmap(icon));
            markerOptions.anchor(0.5f, 0.5f);
            entity.setMarkerOptions(markerOptions);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mOnReadyListener != null) {
            mOnReadyListener.onReady();
        }
    }


    private Bitmap createLabel(LayoutInflater inflater, String name, List<RealmString> entityIcons) {
        LinearLayout tv = (LinearLayout) inflater.inflate(R.layout.marker_default, null, false);
        ((TextView) tv.findViewById(R.id.title)).setText(Html.fromHtml(name));

        LinearLayout icons = (LinearLayout) tv.findViewById(R.id.icons);
        String iconName;
        Icon icon;
        for (RealmString iconString : entityIcons) {
            iconName = iconString.getVal();
            ImageView view = new ImageView(inflater.getContext());
            icon = Icon.ICONS.get(iconName);
            if (icon != null) {
                view.setImageResource(icon.drawable);
                icons.addView(view);
            }
        }

        tv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        tv.setDrawingCacheEnabled(true);
        tv.buildDrawingCache();
        return tv.getDrawingCache();
    }

    public LabelCreationTask onReady(OnReadyListener onReadyListener) {
        mOnReadyListener = onReadyListener;
        return this;
    }
}
