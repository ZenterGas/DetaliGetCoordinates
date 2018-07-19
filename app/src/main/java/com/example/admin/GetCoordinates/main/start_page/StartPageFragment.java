package com.example.admin.GetCoordinates.main.start_page;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.example.admin.GetCoordinates.R;
import com.example.admin.GetCoordinates.main.MainActivity;
import com.example.admin.GetCoordinates.main.get_information_page.GetInformationFragment;
import com.example.admin.GetCoordinates.main.start_page.delivery_list_adapter.DeliveryListAdapter;
import com.example.admin.GetCoordinates.main.upload_data_page.UploadDataFragment;
import com.example.admin.GetCoordinates.mvp.classes.BaseFragment;
import com.example.admin.GetCoordinates.realm.realm_objects.RealmDeliverTarget;
import com.example.admin.GetCoordinates.utils.LogUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.realm.RealmResults;

import static com.example.admin.GetCoordinates.MainApplication.logs;

public class StartPageFragment extends BaseFragment<StartPagePresenter> implements DeliveryListAdapter.OnDeliveryRecycleClickListener, MainActivity.OnBackClickListener {

    @BindView(R.id.driver_name)
    protected TextView tvDriver;
    @BindView(R.id.rv_delivery_list)
    protected RecyclerView rvDeliveryList;
    @BindView(R.id.hide_filled)
    protected Switch hideFilledField;
    @BindView(R.id.tv_data)
    protected TextView tvData;

    private LocationManager locationManager;
    private boolean isNeedReserve;
    private boolean isSecondCheck;
    private AlertDialog errorDialog;
    private String clientCode;
    private DeliveryListAdapter adapter;
    private AlertDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_page_fragment, container, false);
    }

    @Override
    protected void initPresenter() {
        applyPresenter(new StartPagePresenter<>((MainActivity) getActivity()));
        getPresenter().initComponents(new StartPageModel(getResources()), this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setBackClickListener(this);
        }
        if (getContext() != null) {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            getPresenter().getDeliveryList();
        }
    }

    public void hideSwitch() {
        hideFilledField.setVisibility(View.GONE);
    }

    public void setDeliveryList(String data, String driver, RealmResults<RealmDeliverTarget> clients) {
        tvData.setText(data);
        tvDriver.setText(driver);
        settingsDeliveryList(clients);
    }

    private void settingsDeliveryList(RealmResults<RealmDeliverTarget> clients) {
        if (clients.size() != 0) {
            LinearLayoutManager llManager = new LinearLayoutManager(getContext());
            adapter = new DeliveryListAdapter(clients, this);
            hideFilledField.setVisibility(View.VISIBLE);
            adapter.setHideFilled(hideFilledField.isChecked());
            rvDeliveryList.setLayoutManager(llManager);
            rvDeliveryList.setAdapter(adapter);
        }
    }

    @OnClick(R.id.btn_load_data)
    protected void loadNewInformation() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showFragment(new GetInformationFragment());
        }
    }

    @OnClick(R.id.btn_upload_data)
    protected void goToUploadPage(){
        if (getActivity() != null){
            ((MainActivity) getActivity()).showFragment(new UploadDataFragment());
        }
    }

    @OnCheckedChanged(R.id.hide_filled)
    protected void changeHideMode(boolean checked){
        adapter.setHideFilled(checked);
        adapter.notifyDataSetChanged();
    }

    public void showDialogWithText(final String errorText, boolean isErrorDialog) {
        AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        View errorDialogView = View.inflate(getContext(), R.layout.error_dialog, null);
        errorDialogBuilder.setView(errorDialogView);
        changeErrorVisibility(errorDialogView, isErrorDialog ? View.VISIBLE : View.GONE);
        errorDialog = errorDialogBuilder.create();
        TextView tvErrorText = errorDialogView.findViewById(R.id.text_error);
        errorDialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
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
    public void onFilledDeliveryTargetClick(final String code, final double latitude, final double longitude) {
        AlertDialog.Builder selectDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        View selectDialogView = View.inflate(getContext(), R.layout.select_dialog, null);
        selectDialogBuilder.setView(selectDialogView);
        final AlertDialog selectDialog = selectDialogBuilder.create();
        selectDialogView.findViewById(R.id.rewrite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();
                onEmptyDeliveryTargetClick(code);
            }
        });
        selectDialogView.findViewById(R.id.show_on_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        selectDialogView.findViewById(R.id.cancel_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().cancelChange(code);
                adapter.notifyDataSetChanged();
                selectDialog.dismiss();
            }
        });
        selectDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDialog.dismiss();
            }
        });
        selectDialog.setCancelable(false);
        selectDialog.show();
    }

    @Override
    public void onEmptyDeliveryTargetClick(String code) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            clientCode = code;
            isNeedReserve = true;
            isSecondCheck = false;
            showProgressDialog();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            startReserveCheck();
        } else {
            adapter.notifyDataSetChanged();
            showDialogWithText("Пожалуйста включите GPS", true);
        }
    }

    @Override
    protected void showProgressDialog() {
        AlertDialog.Builder progressDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        View progressDialogView = View.inflate(getContext(), R.layout.waiting_dialog, null);
        progressDialogBuilder.setView(progressDialogView);

        progressDialog = progressDialogBuilder.create();
        progressDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    locationManager.removeUpdates(locationListener);
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
                return true;
            }
        });
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void startReserveCheck() {
        Thread checkReserve = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30000);
                    if (isNeedReserve) {
                        reserveHandler.sendEmptyMessage(0);
                    }
                    if (isSecondCheck) {
                        reserveHandler.sendEmptyMessage(1);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        checkReserve.start();
    }

    private Handler reserveHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                isSecondCheck = true;
                isNeedReserve = false;
                if (checkInternetConnection()) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                }
                startReserveCheck();
            } else if (msg.what == 1) {
                progressDialog.dismiss();
                locationManager.removeUpdates(locationListener);
                logs.appendLog(LogUtil.LogsType.e, "StartPageFragment", "SecondCheck: can not get location coordinates");
                showDialogWithText("Не удалось получить координаты!", true);
                adapter.notifyDataSetChanged();
            }
            return false;
        }
    });

    public boolean checkInternetConnection() {
        if (getContext() != null) {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                return netInfo != null && netInfo.isConnectedOrConnecting();
            }
        }
        return false;
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            isNeedReserve = false;
            isSecondCheck = false;
            locationManager.removeUpdates(locationListener);
            getPresenter().setCoordinates(location.getLatitude(), location.getLongitude(), clientCode);
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
            showDialogWithText("Координаты успешно записаны", false);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    @Override
    public void showMessage(String message) {

    }


    @Override
    public void onBackClick() {
        showExitDialog();
    }

    public void showExitDialog() {
        AlertDialog.Builder exitDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        View exitDialogView = View.inflate(getContext(), R.layout.confirm_dialog, null);
        TextView tvConfirmText = exitDialogView.findViewById(R.id.text_error);
        tvConfirmText.setText("Вы действительно хотите выйти?");
        exitDialogBuilder.setView(exitDialogView);
        final AlertDialog exitDialog = exitDialogBuilder.create();
        exitDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });
        exitDialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    if (locationListener != null ){
                        locationManager.removeUpdates(locationListener);
                    }
                    logs.appendLog(LogUtil.LogsType.i, "StartPageFragment: showExitDialog", "Application close");
                    System.exit(0);
                }
            }
        });
        exitDialog.setCancelable(false);
        exitDialog.show();
    }

    @OnClick(R.id.btn_exit)
    protected void closeApp(){
        showExitDialog();
    }

}
