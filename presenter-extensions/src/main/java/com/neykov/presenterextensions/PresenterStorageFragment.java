package com.neykov.presenterextensions;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class PresenterStorageFragment extends Fragment{

    static final String TAG = PresenterStorageFragment.class.getSimpleName();

    private DefaultPresenterStorage presenterStorage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterStorage = new DefaultPresenterStorage();
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
