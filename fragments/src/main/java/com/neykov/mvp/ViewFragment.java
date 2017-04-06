package com.neykov.mvp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

public abstract class ViewFragment<P extends Presenter> extends Fragment
        implements ViewWithPresenter<P>, PresenterFactory<P> {

    private PresenterLifecycleHelper<P> presenterDelegate;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate = new PresenterLifecycleHelper<>(this,
                FragmentPresenterStorage.from(getActivity().getFragmentManager()));
        presenterDelegate.restoreState(savedInstanceState);
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        presenterDelegate.saveState(bundle);
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterDelegate.destroy(presenterShouldBeDestroyed());
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.bindView(this);
    }

    @CallSuper
    @Override
    public void onPause() {
        presenterDelegate.unbindView(presenterShouldBeDestroyed());
        super.onPause();
    }

    @Override
    public final P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    protected boolean presenterShouldBeDestroyed() {
        return getActivity().isFinishing();
    }
}