package com.neykov;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.neykov.mvp.support.ViewFragment;

public class SampleFragment extends ViewFragment<SamplePresenter> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Presenter instance: ", getPresenter().toString());
    }

    @Override
    public SamplePresenter createPresenter() {
        return new SamplePresenter();
    }
}
