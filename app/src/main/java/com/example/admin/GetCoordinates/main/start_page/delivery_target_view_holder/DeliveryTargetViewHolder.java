package com.example.admin.GetCoordinates.main.start_page.delivery_target_view_holder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.admin.GetCoordinates.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeliveryTargetViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.delivery_target)
    TextView tvClientName;
    @BindView(R.id.bottom_line)
    View bottomLine;
    @BindView(R.id.cb_coordinates_not_empty)
    CheckBox coordinatesNotEmpty;
    @BindView(R.id.delivery_target_root)
    ConstraintLayout rootLayout;
    private OnDeliveryTargetClickListener listener;
    private int position;

    public DeliveryTargetViewHolder(View itemView, OnDeliveryTargetClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    public void setName(String name) {
        tvClientName.setText(name);
    }

    public void showBottomLine(){
        bottomLine.setVisibility(View.VISIBLE);
    }

    public void setCheckBoxChecked(boolean checked){
        coordinatesNotEmpty.setChecked(checked);
    }

    public void setItemVisibility(int visibility) {
        rootLayout.setMaxHeight(visibility);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @OnClick(R.id.cb_coordinates_not_empty)
    protected void onCbClick(){
        coordinatesNotEmpty.setChecked(true);
        listener.onDeliveryTargetClick(position);
    }

    public interface OnDeliveryTargetClickListener{
        void onDeliveryTargetClick(int position);
    }

}
