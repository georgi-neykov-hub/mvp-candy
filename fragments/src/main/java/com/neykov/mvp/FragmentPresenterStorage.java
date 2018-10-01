package com.neykov.mvp;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.os.Build;
import androidx.annotation.RequiresApi;

/**
 * Created by Georgi on 12/16/2016.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class FragmentPresenterStorage {

    private FragmentPresenterStorage() {
    }

    public static PresenterStorage from(FragmentManager fragmentManager){
        if (fragmentManager == null) {
            throw new IllegalArgumentException("FragmentManager argument cannot be null.");
        }

        PresenterStorageFragment fragment = (PresenterStorageFragment)
                fragmentManager.findFragmentByTag(PresenterStorageFragment.TAG);
        if (fragment == null) {
            fragment = new PresenterStorageFragment();
            fragmentManager.beginTransaction()
                    .add(fragment, PresenterStorageFragment.TAG)
                    .disallowAddToBackStack()
                    .commitAllowingStateLoss();
        }
        return fragment.getPresenterStorage();
    }
}
