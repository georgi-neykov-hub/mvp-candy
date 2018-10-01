package com.neykov.mvp.support;

import android.annotation.TargetApi;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;

import com.neykov.mvp.PresenterStorage;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
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
