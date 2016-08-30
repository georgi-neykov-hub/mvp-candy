package com.neykov;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.neykov.mvp.Presenter;
import com.neykov.mvp.PresenterFactory;
import com.neykov.mvp.SupportPresenterLifecycleDelegate;
import com.neykov.mvp.sample.R;

public class SampleActivity extends FragmentActivity implements PresenterFactory<SamplePresenter>{

    private SupportPresenterLifecycleDelegate<SamplePresenter> presenterLifecycleDelegate = new SupportPresenterLifecycleDelegate<>(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenterLifecycleDelegate.onCreate(savedInstanceState, getSupportFragmentManager());
        Log.d("Presenter instance: ", presenterLifecycleDelegate.getPresenter().toString());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterLifecycleDelegate.onSaveInstanceState(outState);
    }

    @Override
    public SamplePresenter createPresenter() {
        return new SamplePresenter();
    }
}
