package com.example.admin.GetCoordinates.main.upload_data_page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.GetCoordinates.R;
import com.example.admin.GetCoordinates.main.MainActivity;
import com.example.admin.GetCoordinates.main.start_page.StartPageFragment;
import com.example.admin.GetCoordinates.main.upload_data_page.clients_adapter.UploadDataAdapter;
import com.example.admin.GetCoordinates.mvp.classes.BaseFragment;
import com.example.admin.GetCoordinates.realm.realm_objects.RealmDeliverTarget;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.RealmResults;

public class UploadDataFragment extends BaseFragment<UploadDataPresenter> implements MainActivity.OnBackClickListener {

    @BindView(R.id.rv_data_for_upload)
    protected RecyclerView rvDataForUpload;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.upload_data_page, container, false);
    }

    @Override
    protected void initPresenter() {
        applyPresenter(new UploadDataPresenter<>((MainActivity) getActivity()));
        getPresenter().initComponents(new UploadDataModel(getResources()), this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setBackClickListener(this);
        }
        settingRvDataForUpload();
    }

    private void settingRvDataForUpload() {
        if (getContext() != null) {
            RealmResults<RealmDeliverTarget> dataForUpload = getPresenter().getDataForUpload();
            if (dataForUpload.size() != 0) {
                LinearLayoutManager llManager = new LinearLayoutManager(getContext());
                UploadDataAdapter adapter = new UploadDataAdapter(dataForUpload);
                rvDataForUpload.setLayoutManager(llManager);
                rvDataForUpload.setAdapter(adapter);
            } else {
                showDialogWithText("Информация для выгрузки отсутствует", true);
            }
        }
    }

    public void showDialogWithText(final String errorText, boolean isErrorDialog) {
        if (isProgressShow()){
            dismissProgressDialog();
        }
        AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        View errorDialogView = View.inflate(getContext(), R.layout.error_dialog, null);
        errorDialogBuilder.setView(errorDialogView);
        changeErrorVisibility(errorDialogView, isErrorDialog ? View.VISIBLE : View.GONE);
        final AlertDialog errorDialog = errorDialogBuilder.create();
        TextView tvErrorText = errorDialogView.findViewById(R.id.text_error);
        errorDialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
                onBackClick();
            }
        });
        tvErrorText.setText(errorText);
        errorDialog.setCancelable(false);
        errorDialog.show();
    }

    private void changeErrorVisibility(View errorDialogView, int visibility){
        TextView error = errorDialogView.findViewById(R.id.error);
        error.setVisibility(visibility);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onBackClick() {
        if (getActivity() != null){
            ((MainActivity) getActivity()).showFragment(new StartPageFragment());
        }
    }

    @OnClick(R.id.btn_upload)
    protected void uploadData(){
        showProgressDialog();
        getPresenter().uploadDataToServer();
    }

}
