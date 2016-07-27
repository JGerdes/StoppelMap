package com.jonasgerdes.stoppelmap.usecases.map.search;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jonasgerdes.stoppelmap.R;
import com.jonasgerdes.stoppelmap.model.map.Icon;
import com.jonasgerdes.stoppelmap.model.map.search.EntitySearchResult;
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult;
import com.jonasgerdes.stoppelmap.model.shared.RealmString;
import com.jonasgerdes.stoppelmap.usecases.map.EntityHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 22.07.2016.
 */
public class EntityResultHolder extends SearchResultHolder {

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.image)
    ImageView mImageView;

    @BindView(R.id.icon_container)
    LinearLayout mIconContainer;

    public EntityResultHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(SearchResult result) {
        EntitySearchResult entityResult = (EntitySearchResult) result;
        mTitle.setText(entityResult.getMapEntity().getName());
        Context context = itemView.getContext();
        String headerFile = EntityHelper.getHeaderFile(context, entityResult.getMapEntity());
        String headerPath = context.getString(R.string.asset_map_entity_header_dir, headerFile);
        Glide.with(context)
                .load(Uri.parse(headerPath))
                .centerCrop()
                .into(mImageView);

        List<RealmString> icons = entityResult.getMapEntity().getIcons();
        showIcons(icons);
    }

    private void showIcons(List<RealmString> icons) {
        if (icons != null) {
            ImageView view;
            for (int i = 0; i < mIconContainer.getChildCount(); i++) {
                view = ((ImageView) mIconContainer.getChildAt(i));
                if (icons.size() > i) {
                    Icon icon = Icon.ICONS.get(icons.get(i).getVal());
                    if (icon != null) {
                        view.setImageResource(icon.drawable);
                        view.setVisibility(View.VISIBLE);
                        continue;
                    }
                }
                view.setVisibility(View.GONE);
            }
        }
    }
}
