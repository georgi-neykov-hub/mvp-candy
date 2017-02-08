package com.neykov.mvp;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

@SuppressWarnings("unused")
public abstract class DialogViewFragment<P extends Presenter> extends DialogFragment
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
        presenterDelegate.destroy(presenterShouldBeDestroyed() || !getActivity().isChangingConfigurations());
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        presenterDelegate.bindView(this);
    }

    @CallSuper
    @Override
    public void onStop() {
        super.onStop();
        presenterDelegate.unbindView(presenterShouldBeDestroyed());
    }

    @Override
    public final P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    protected boolean presenterShouldBeDestroyed() {
        return getActivity().isFinishing();
    }
}