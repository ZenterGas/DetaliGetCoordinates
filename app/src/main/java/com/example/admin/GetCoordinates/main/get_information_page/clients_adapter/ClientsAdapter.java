package com.example.admin.GetCoordinates.main.get_information_page.clients_adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.GetCoordinates.R;
import com.example.admin.GetCoordinates.main.get_information_page.clients_view_holder.ClientViewHolder;
import com.example.admin.GetCoordinates.retrofit_api.response.Client;

import java.util.ArrayList;

public class ClientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Client> clients;

    public ClientsAdapter(ArrayList<Client> clients){
        this.clients = clients;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View clientView = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        return new ClientViewHolder(clientView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ClientViewHolder) holder).setName(clients.get(position).name);
        if (position == clients.size()-1){
            ((ClientViewHolder) holder).showBottomLine();
        }
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

}
