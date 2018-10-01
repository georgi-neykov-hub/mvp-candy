package com.neykov.mvp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.app.Fragment;
import androidx.annotation.RequiresApi;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class PresenterStorageFragment extends Fragment{

    public static final String TAG = PresenterStorageFragment.class.getSimpleName();

    private final DefaultPresenterStorage presenterStorage = new DefaultPresenterStorage();;

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
