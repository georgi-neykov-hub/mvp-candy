package com.neykov;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.neykov.mvp.PresenterFactory;
import com.neykov.mvp.PresenterLifecycleHelper;
import com.neykov.mvp.support.FragmentPresenterStorage;

public class SampleFragment extends Fragment implements PresenterFactory<SamplePresenter> {

    private PresenterLifecycleHelper<SamplePresenter> presenterLifecycleDelegate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterLifecycleDelegate = new PresenterLifecycleHelper<>(this, FragmentPresenterStorage.from(getFragmentManager()));
        presenterLifecycleDelegate.restoreState(savedInstanceState);
        Log.d("Presenter instance: ", presenterLifecycleDelegate.getPresenter().toString());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenterLifecycleDelegate.saveState(outState);
    }

    @Override
    public SamplePresenter createPresenter() {
        return new SamplePresenter();
    }
}
