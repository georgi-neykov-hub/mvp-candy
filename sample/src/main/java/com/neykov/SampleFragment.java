package com.neykov;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.neykov.mvp.PresenterFactory;
import com.neykov.mvp.PresenterLifecycleDelegate;
import com.neykov.mvp.SupportFragmentPresenterStorage;

public class SampleFragment extends Fragment implements PresenterFactory<SamplePresenter> {

    private PresenterLifecycleDelegate<SamplePresenter> presenterLifecycleDelegate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterLifecycleDelegate = new PresenterLifecycleDelegate<>(this, SupportFragmentPresenterStorage.from(getFragmentManager()));
        presenterLifecycleDelegate.onCreate(savedInstanceState);
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
