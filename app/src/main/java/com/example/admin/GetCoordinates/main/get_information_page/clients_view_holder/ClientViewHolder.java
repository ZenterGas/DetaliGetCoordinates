package com.example.admin.GetCoordinates.main.get_information_page.clients_view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.admin.GetCoordinates.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.client_name)
    TextView tvClientName;
    @BindView(R.id.bottom_line)
    View bottomLine;

    public ClientViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setName(String name) {
        tvClientName.setText(name);
    }

    public void showBottomLine(){
        bottomLine.setVisibility(View.VISIBLE);
    }

}