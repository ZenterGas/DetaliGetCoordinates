package com.example.admin.GetCoordinates.mvp.classes;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getFragmentContainer();

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(getFragmentContainer(), fragment).commit();
    }

    protected void showStartPageFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(getFragmentContainer(), fragment).commit();
    }

}
