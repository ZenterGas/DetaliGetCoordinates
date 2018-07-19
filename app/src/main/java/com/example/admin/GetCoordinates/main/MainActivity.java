package com.example.admin.GetCoordinates.main;

import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.example.admin.GetCoordinates.R;
import com.example.admin.GetCoordinates.main.start_page.StartPageFragment;
import com.example.admin.GetCoordinates.mvp.classes.BaseActivity;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity {

    private OnBackClickListener backClickListener;

    @Override
    protected int getFragmentContainer() {
        return R.id.container;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        showStartPageFragment(new StartPageFragment());
    }

    public void setBackClickListener(OnBackClickListener listener){
        backClickListener = listener;
    }

    @Override
    public void onBackPressed() {
        backClickListener.onBackClick();
    }

    public interface OnBackClickListener{
        void onBackClick();
    }

}
