package com.neykov.mvp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.FragmentManager;
import android.support.annotation.RequiresApi;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
@SuppressWarnings("unused")
public class PresenterLifecycleDelegate<P extends Presenter> {
    private static final String KEY_PRESENTER_STATE = "presenter";
    private static final String KEY_PRESENTER_ID = "presenter_id";

    private PresenterStorage presenterStorage;

    private final PresenterFactory<P> presenterFactory;
    @Nullable
    private P presenter;
    @Nullable
    private Bundle bundle;

    public PresenterLifecycleDelegate(PresenterFactory<P> presenterFactory) {
        this.presenterFactory = presenterFactory;
    }

    /**
     * {@link ViewWithPresenter#getPresenter()}
     */
    public P getPresenter() {
        if (presenter == null && bundle != null) {
            String presenterId = bundle.getString(KEY_PRESENTER_ID);
            if (presenterId != null) {
                presenter = presenterStorage.getPresenter(presenterId);
            }
        }
        if (presenter == null) {
            presenter = presenterFactory.createPresenter();
            presenterStorage.add(presenter);
            presenter.create(bundle == null ? null : bundle.getBundle(KEY_PRESENTER_STATE));
        }
        bundle = null;
        return presenter;
    }

    /**
     * {@link android.app.Activity#onSaveInstanceState(Bundle)}, {@link android.app.Fragment#onSaveInstanceState(Bundle)}, {@link android.view.View#onSaveInstanceState()}.
     */
    public void onSaveInstanceState(Bundle bundle) {
        if (presenter != null) {
            Bundle presenterBundle = new Bundle();
            presenterBundle.putString(KEY_PRESENTER_ID, presenterStorage.getId(presenter));
            presenter.save(presenterBundle);
            bundle.putBundle(KEY_PRESENTER_STATE, presenterBundle);
        }
    }

    /**
     * {@link android.app.Activity#onResume()}, {@link android.app.Fragment#onResume()}, {@link android.view.View#onAttachedToWindow()}
     */
    public void onResume(Object view) {
        getPresenter();
        if (presenter != null)
            //noinspection unchecked
            presenter.takeView(view);
    }

    /**
     * {@link android.app.Activity#onPause()}, {@link android.app.Fragment#onPause()}, {@link android.view.View#onDetachedFromWindow()}
     */
    public void onPause(boolean destroy) {
        if (presenter != null) {
            presenter.dropView();
            if (destroy) {
                presenter.destroy();
                presenter = null;
            }
        }
    }

    public void onDestroy(boolean destroyPresenter) {
        if (presenter != null && destroyPresenter) {
            presenter.destroy();
            presenter = null;
        }
    }

    public void onCreate(@Nullable Bundle savedState, FragmentManager fragmentManager) {
        PresenterStorageFragment fragment = (PresenterStorageFragment)
                fragmentManager.findFragmentByTag(PresenterStorageFragment.TAG);
        if (fragment == null) {
            fragment = new PresenterStorageFragment();
            fragmentManager.beginTransaction()
                    .add(fragment, PresenterStorageFragment.TAG)
                    .disallowAddToBackStack()
                    .commit();
        }
        presenterStorage = fragment.getPresenterStorage();

        if (savedState != null) {
            if (presenter != null)
                throw new IllegalArgumentException("onRestoreInstanceState() should be called before onResume()");
            this.bundle = savedState.getBundle(KEY_PRESENTER_STATE);
        }
    }
}
