package com.neykov.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

public class SupportPresenterLifecycleDelegate<P extends Presenter> {
    private static final String KEY_PRESENTER_STATE = "presenter";
    private static final String KEY_PRESENTER_ID = "presenter_id";

    private PresenterStorage presenterStorage;
    private boolean usingTemporaryStorage;

    private final PresenterFactory<P> presenterFactory;
    @Nullable
    private P presenter;
    @Nullable
    private Bundle bundle;

    public SupportPresenterLifecycleDelegate(PresenterFactory<P> presenterFactory) {
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

    public void onDestroy(boolean destroy) {
        if (presenter != null && destroy) {
            presenter.destroy();
            presenter = null;
        }
    }

    public void onCreate(@Nullable Bundle savedState, FragmentManager supportFragmentManager){
        SupportPresenterStorageFragment fragment = (SupportPresenterStorageFragment)
                supportFragmentManager.findFragmentByTag(SupportPresenterStorageFragment.TAG);
        if(fragment == null){
                // We still haven't passed though onStart().
                presenterStorage = new DefaultPresenterStorage();
                usingTemporaryStorage = true;
        }else {
            presenterStorage = fragment.getPresenterStorage();
            usingTemporaryStorage = false;
        }

        if (savedState != null) {
            if (presenter != null)
                throw new IllegalArgumentException("onRestoreInstanceState() should be called before onResume()");
            this.bundle = savedState.getBundle(KEY_PRESENTER_STATE);
        }
    }

    public void onStart(FragmentManager manager){
        SupportPresenterStorageFragment fragment = (SupportPresenterStorageFragment)
                manager.findFragmentByTag(SupportPresenterStorageFragment.TAG);
        if(fragment == null){
            fragment = new SupportPresenterStorageFragment();
            manager.beginTransaction()
                    .add(fragment, SupportPresenterStorageFragment.TAG)
                    .disallowAddToBackStack()
                    .commitNow();
        }

        if(presenter != null && usingTemporaryStorage){
            fragment.getPresenterStorage().add(presenter);
            this.presenterStorage.remove(presenter);
            usingTemporaryStorage = false;
        }

        this.presenterStorage = fragment.getPresenterStorage();
    }


    public void onDetach(boolean destroy){
        if (presenter != null && destroy) {
            presenter.destroy();
            presenter = null;
        }
    }

}
