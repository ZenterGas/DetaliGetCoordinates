package com.example.admin.GetCoordinates.main.get_information_page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.GetCoordinates.R;
import com.example.admin.GetCoordinates.main.MainActivity;
import com.example.admin.GetCoordinates.main.get_information_page.clients_adapter.ClientsAdapter;
import com.example.admin.GetCoordinates.main.start_page.StartPageFragment;
import com.example.admin.GetCoordinates.mvp.classes.BaseFragment;
import com.example.admin.GetCoordinates.retrofit_api.response.Client;
import com.example.admin.GetCoordinates.retrofit_api.response.Driver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import butterknife.OnClick;

public class GetInformationFragment extends BaseFragment<GetInformationPresenter> implements MainActivity.OnBackClickListener {

    private long initialDate;

    @BindView(R.id.data)
    protected MaskedEditText data;
    @BindView(R.id.calendar)
    protected CalendarView calendarView;
    @BindView(R.id.btn_get_current_date)
    protected TextView btnGetCurrentDate;
    @BindView(R.id.btn_close_calendar)
    protected TextView btnCloseCalendar;
    @BindView(R.id.driver_list)
    protected Spinner driverList;
    @BindView(R.id.clients_recycle)
    protected RecyclerView rvClients;
    @BindView(R.id.btn_save_clients)
    protected TextView btnSaveClients;

    private ArrayList<Client> selectedClients;
    private AlertDialog errorDialog;
    private String dataForRequest;

    private long currentDateInMillis;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.get_information_fragment, container, false);
    }

    @Override
    protected void initPresenter() {
        applyPresenter(new GetInformationPresenter<>((MainActivity) getActivity()));
        getPresenter().initComponents(new GetInformationModel(getResources()), this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setBackClickListener(this);
        }
        if (getContext() != null) {
            initialDate = calendarView.getDate();
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    String selectedDate = getSelectedDate(year, month, dayOfMonth);
                    if (initialDate != view.getDate() && !selectedDate.equals(data.getRawText())) {
                        getDrivers(selectedDate);
                    }
                }
            });
            setStartSettings();
        }
    }

    private void setStartSettings() {
        Calendar calendar = Calendar.getInstance();
        currentDateInMillis = calendar.getTimeInMillis();
        String selectedDate = getSelectedDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        getDrivers(selectedDate);
    }

    private String getSelectedDate(int selectedYear, int selectedMonth, int selectedDay) {
        String year = String.valueOf(selectedYear);
        String month = convertToDateFormat(selectedMonth + 1);
        String day = convertToDateFormat(selectedDay);
        dataForRequest = String.valueOf(year) + month + day;
        return day + month + String.valueOf(year);
    }

    private void getDrivers(String selectedDate) {
        showProgressDialog();
        data.setText(selectedDate);
        getPresenter().getDrivers(dataForRequest);
        closeOrOpenCalendar(View.GONE);
    }

    @OnClick(R.id.data)
    protected void showCalendar() {
        closeOrOpenCalendar(View.VISIBLE);
    }

    @OnClick(R.id.btn_close_calendar)
    protected void closeCalendar() {
        closeOrOpenCalendar(View.GONE);
    }

    private void closeOrOpenCalendar(int visibility) {
        btnGetCurrentDate.setVisibility(visibility);
        calendarView.setVisibility(visibility);
        btnCloseCalendar.setVisibility(visibility);
    }

    private String convertToDateFormat(int monthOrDay) {
        String required;
        if (monthOrDay < 10) {
            required = "0" + String.valueOf(monthOrDay);
        } else {
            required = String.valueOf(monthOrDay);
        }
        return required;
    }

    @OnClick(R.id.btn_get_current_date)
    protected void getCurrentDate() {
        calendarView.setDate(currentDateInMillis);
    }

    public void showDriverList(final ArrayList<Driver> drivers) {
        if (drivers.size() != 0) {
            showOrHideElements(View.VISIBLE, true);
            if (getContext() != null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getDriversName(drivers));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                driverList.setAdapter(adapter);
                driverList.setPrompt("Водители");
                driverList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!isProgressDialogShowing()) {
                            showProgressDialog();
                        }
                        getPresenter().getClients(dataForRequest, drivers.get(position).code);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        } else {
            dismissProgressDialog();
            showOrHideElements(View.GONE, true);
            showErrorDialog("Нет водителей на этот день");
        }
    }

    private List<String> getDriversName(ArrayList<Driver> drivers) {
        List<String> driversName = new ArrayList<>();
        for (int i = 0; i < drivers.size(); i++) {
            driversName.add(drivers.get(i).name);
        }
        return driversName;
    }

    public void settingRecycle(ArrayList<Client> clients) {
        dismissProgressDialog();
        selectedClients = clients;
        if (clients.size() != 0) {
            showOrHideElements(View.VISIBLE, false);
            LinearLayoutManager llManager = new LinearLayoutManager(getContext());
            ClientsAdapter adapter = new ClientsAdapter(clients);
            rvClients.setLayoutManager(llManager);
            rvClients.setAdapter(adapter);
        } else {
            showOrHideElements(View.GONE, false);
            showErrorDialog("У этого водителя не назначено ни одного клиента");
        }
    }

    public void showErrorDialog(final String errorText) {
        AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        View errorDialogView = View.inflate(getContext(), R.layout.error_dialog, null);
        errorDialogBuilder.setView(errorDialogView);
        errorDialog = errorDialogBuilder.create();
        TextView tvErrorText = errorDialogView.findViewById(R.id.text_error);
        errorDialogView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
                if (errorText.equals(getString(R.string.connect_to_local))) {
                    dismissProgressDialog();
                    onBackClick();
                }
            }
        });
        tvErrorText.setText(errorText);
        errorDialog.setCancelable(false);
        errorDialog.show();
    }

    private void showOrHideElements(int visibility, boolean isDriverList) {
        if (isDriverList) {
            driverList.setVisibility(visibility);
        }
        rvClients.setVisibility(visibility);
        btnSaveClients.setVisibility(visibility);
    }

    @OnClick(R.id.btn_save_clients)
    protected void saveClientToDatabase() {
        getPresenter().saveClients(data.getText().toString(), driverList.getSelectedItem().toString(), selectedClients);
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showFragment(new StartPageFragment());
        }
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onBackClick() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).showFragment(new StartPageFragment());
        }
    }

}
