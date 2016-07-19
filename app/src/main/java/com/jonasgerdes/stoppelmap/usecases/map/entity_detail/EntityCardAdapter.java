package com.jonasgerdes.stoppelmap.usecases.map.entity_detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.usecases.map.entity_detail.cards.IconsEntityCardHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jonas on 19.07.2016.
 */
public class EntityCardAdapter extends RecyclerView.Adapter<EntityCardHolder> {

    private List<EntityCard> mEntityCards;

    @Override
    public EntityCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        switch (viewType) {
            case IconsEntityCardHolder.LAYOUT:
                return new IconsEntityCardHolder(view);
        }

        return new EntityCardHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return mEntityCards.get(position).getType();
    }

    @Override
    public void onBindViewHolder(EntityCardHolder holder, int position) {
        holder.onBind(mEntityCards.get(position));
    }

    @Override
    public int getItemCount() {
        if (mEntityCards == null) {
            return 0;
        }
        return mEntityCards.size();
    }

    public void setEntityCards(List<EntityCard> entityCards) {
        mEntityCards = entityCards;
        notifyDataSetChanged();
    }

    public void setEntityCards(EntityCard... entityCards) {
        setEntityCards(Arrays.asList(entityCards));
    }

    public void addEntityCard(EntityCard entityCard) {
        if (mEntityCards == null) {
            mEntityCards = new ArrayList<>();
        }
        mEntityCards.add(entityCard);
        notifyItemInserted(mEntityCards.size() - 1);
    }
}
