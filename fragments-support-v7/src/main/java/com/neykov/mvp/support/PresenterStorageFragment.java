package com.neykov.mvp.support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.neykov.mvp.DefaultPresenterStorage;
import com.neykov.mvp.PresenterStorage;

public class PresenterStorageFragment extends Fragment{

    public static final String TAG = PresenterStorageFragment.class.getSimpleName();

    private final DefaultPresenterStorage presenterStorage = new DefaultPresenterStorage();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        presenterStorage.clear();
        super.onDestroy();
    }

    @Override
    public final void setRetainInstance(boolean retain) {
        super.setRetainInstance(true);
    }

    PresenterStorage getPresenterStorage() {
        return presenterStorage;
    }
}
