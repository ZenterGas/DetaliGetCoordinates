package com.example.admin.GetCoordinates.mvp.classes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.example.admin.GetCoordinates.R;
import com.example.admin.GetCoordinates.mvp.interfaces.PresenterInterface;
import com.example.admin.GetCoordinates.mvp.interfaces.ViewInterface;

import java.util.Objects;

import butterknife.ButterKnife;

public abstract class BaseFragment <P extends PresenterInterface> extends Fragment implements ViewInterface<P> {

    private P presenter;
    private AlertDialog progressDialog;

    protected abstract void initPresenter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    protected final void applyPresenter(P presenter){
        this.presenter = presenter;
    }

    protected final P getPresenter(){
        return this.presenter;
    }

    protected void showProgressDialog() {
        AlertDialog.Builder progressDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        View progressDialogView = View.inflate(getContext(), R.layout.waiting_dialog, null);
        progressDialogBuilder.setView(progressDialogView);

        progressDialog = progressDialogBuilder.create();
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected boolean isProgressShow() {
        return progressDialog != null && progressDialog.isShowing();
    }

    protected void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    protected boolean isProgressDialogShowing(){
        return progressDialog.isShowing();
    }

}
