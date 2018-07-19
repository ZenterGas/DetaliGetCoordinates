package com.example.admin.GetCoordinates.main.upload_data_page.upload_data_view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.admin.GetCoordinates.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UploadDataViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.client_name)
    TextView tvClientName;
    @BindView(R.id.bottom_line)
    View bottomLine;

    public UploadDataViewHolder(View itemView) {
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