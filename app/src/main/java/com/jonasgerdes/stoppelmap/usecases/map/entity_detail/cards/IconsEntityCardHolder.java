package com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.Icon;
import com.jonasgerdes.stoppelmap.model.shared.RealmString;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCard;
import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.EntityCardHolder;
import com.jonasgerdes.stoppelmap.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 19.07.2016.
 */
public class IconsEntityCardHolder extends EntityCardHolder {

    public static final int LAYOUT = R.layout.map_entity_card_icons;

    @BindView(R.id.container)
    ViewGroup mContainer;

    public IconsEntityCardHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(EntityCard entityCard) {
        Icon icon;
        Context context = itemView.getContext();
        for (RealmString realmString : entityCard.getMapEntity().getIcons()) {
            icon = Icon.ICONS.get(realmString.getVal());
            if (icon == null) {
                continue;
            }

            TextView textView = new TextView(itemView.getContext());
            textView.setText(icon.title);
            Drawable iconDrawable = ContextCompat.getDrawable(context, icon.drawable);
            textView.setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null);
            int padding = ViewUtil.dpToPx(context, 4);
            textView.setPadding(padding, padding, padding, padding);
            textView.setCompoundDrawablePadding(padding * 4);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            mContainer.addView(textView);
        }
    }
}
