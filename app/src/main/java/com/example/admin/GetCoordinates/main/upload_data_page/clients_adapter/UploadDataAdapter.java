package com.example.admin.GetCoordinates.main.upload_data_page.clients_adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.GetCoordinates.R;
import com.example.admin.GetCoordinates.main.upload_data_page.upload_data_view_holder.UploadDataViewHolder;
import com.example.admin.GetCoordinates.realm.realm_objects.RealmDeliverTarget;

import io.realm.RealmResults;

public class UploadDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RealmResults<RealmDeliverTarget> clients;

    public UploadDataAdapter(RealmResults<RealmDeliverTarget> clients){
        this.clients = clients;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View uploadDataView = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        return new UploadDataViewHolder(uploadDataView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((UploadDataViewHolder) holder).setName(clients.get(position).getName());
        if (position == clients.size()-1){
            ((UploadDataViewHolder) holder).showBottomLine();
        }
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

}
