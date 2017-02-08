package com.neykov.mvp.support;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;

import com.neykov.mvp.Presenter;
import com.neykov.mvp.PresenterFactory;
import com.neykov.mvp.PresenterLifecycleHelper;
import com.neykov.mvp.ViewWithPresenter;

@SuppressWarnings("unused")
public abstract class AppCompatDialogViewFragment<P extends Presenter> extends AppCompatDialogFragment
        implements ViewWithPresenter<P>, PresenterFactory<P> {

    private PresenterLifecycleHelper<P> presenterDelegate;

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate = new PresenterLifecycleHelper<>(this,
                FragmentPresenterStorage.from(getActivity().getSupportFragmentManager()));
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