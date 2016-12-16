package com.neykov.mvp;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;

/**
 * Created by Georgi on 12/16/2016.
 */

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
public class SupportFragmentPresenterStorage {

    private SupportFragmentPresenterStorage() {
    }

    public static PresenterStorage from(FragmentManager fragmentManager){
        if (fragmentManager == null) {
            throw new IllegalArgumentException("FragmentManager argument cannot be null.");
        }

        SupportPresenterStorageFragment fragment = (SupportPresenterStorageFragment)
                fragmentManager.findFragmentByTag(SupportPresenterStorageFragment.TAG);
        if (fragment == null) {
            fragment = new SupportPresenterStorageFragment();
            fragmentManager.beginTransaction()
                    .add(fragment, SupportPresenterStorageFragment.TAG)
                    .disallowAddToBackStack()
                    .commit();
        }
        return fragment.getPresenterStorage();
    }
}
