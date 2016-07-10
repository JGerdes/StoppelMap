package com.jonasgerdes.stoppelmap.usecases.transportation.route_detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.jonasgerdes.stoppelmap.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonas on 10.07.2016.
 */
public abstract class AbstractStationHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.node_top)
    ImageView mTopNode;

    @BindView(R.id.node_bottom)
    ImageView mBottomNode;

    public AbstractStationHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void onBind(boolean showTopNode, boolean showBottomNode) {
        mTopNode.setVisibility(showTopNode ? View.VISIBLE : View.GONE);
        mBottomNode.setVisibility(showBottomNode ? View.VISIBLE : View.GONE);
    }
}
