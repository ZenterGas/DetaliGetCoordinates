package com.example.admin.GetCoordinates.main.start_page.delivery_list_adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.GetCoordinates.R;
import com.example.admin.GetCoordinates.main.start_page.delivery_target_view_holder.DeliveryTargetHeader;
import com.example.admin.GetCoordinates.main.start_page.delivery_target_view_holder.DeliveryTargetViewHolder;
import com.example.admin.GetCoordinates.realm.realm_objects.RealmDeliverTarget;

import java.util.Objects;

import io.realm.RealmResults;

public class DeliveryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DeliveryTargetViewHolder.OnDeliveryTargetClickListener {

    private RealmResults<RealmDeliverTarget> clients;
    private OnDeliveryRecycleClickListener listener;
    private boolean hideFilled;
    private final int HEADER = 1001;
    private final int ITEM = 2002;

    public DeliveryListAdapter(RealmResults<RealmDeliverTarget> clients, OnDeliveryRecycleClickListener listener) {
        this.clients = clients;
        this.listener = listener;
    }

    public void setHideFilled(boolean hideFilled) {
        this.hideFilled = hideFilled;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else {
            return ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER:
                View headerTargetView = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_target_header, parent, false);
                return new DeliveryTargetHeader(headerTargetView);
            case ITEM:
                View deliveryTargetView = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_target_item, parent, false);
                return new DeliveryTargetViewHolder(deliveryTargetView, this);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            RealmDeliverTarget client = Objects.requireNonNull(clients.get(position-1));
            ((DeliveryTargetViewHolder) holder).setItemVisibility(client.getNewLatitude() != 0 && client.getNewLongitude() != 0 && !hideFilled ? 0 : 300);
            ((DeliveryTargetViewHolder) holder).setName(client.getName());
            ((DeliveryTargetViewHolder) holder).setPosition(position);
//            ((DeliveryTargetViewHolder) holder).setFillCoordinates();
            ((DeliveryTargetViewHolder) holder).setCheckBoxChecked(client.getNewLatitude() != 0 && client.getNewLongitude() != 0);
            if (position == clients.size() - 1) {
                ((DeliveryTargetViewHolder) holder).showBottomLine();
            }
        }
    }

    @Override
    public int getItemCount() {
        return clients.size() + 1;
    }

    @Override
    public void onDeliveryTargetClick(int position) {
        RealmDeliverTarget client = clients.get(position);
        if (client.getNewLongitude() != 0 && client.getNewLatitude() != 0) {
            listener.onFilledDeliveryTargetClick(client.getCode(), client.getNewLatitude(), client.getNewLongitude());
        } else {
            listener.onEmptyDeliveryTargetClick(client.getCode());
        }
    }

    public interface OnDeliveryRecycleClickListener {
        void onEmptyDeliveryTargetClick(String code);
        void onFilledDeliveryTargetClick(String code, double latitude, double longitude);
    }

}
