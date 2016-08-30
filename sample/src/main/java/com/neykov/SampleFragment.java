package com.neykov;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.neykov.mvp.PresenterFactory;
import com.neykov.mvp.SupportPresenterLifecycleDelegate;

public class SampleFragment extends Fragment implements PresenterFactory<SamplePresenter> {

    private SupportPresenterLifecycleDelegate<SamplePresenter> presenterLifecycleDelegate = new SupportPresenterLifecycleDelegate<>(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterLifecycleDelegate.onCreate(savedInstanceState, getActivity().getSupportFragmentManager());
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
