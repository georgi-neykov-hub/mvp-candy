package com.neykov.mvp.support;

import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.neykov.mvp.Presenter;
import com.neykov.mvp.PresenterFactory;
import com.neykov.mvp.PresenterLifecycleHelper;
import com.neykov.mvp.ViewWithPresenter;

@SuppressWarnings("unused")
public abstract class ViewFragment<P extends Presenter> extends Fragment
        implements ViewWithPresenter<P>, PresenterFactory<P> {

    public ViewFragment() {
        super();
    }

    @ContentView
    public ViewFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    private PresenterLifecycleHelper<P> presenterDelegate;

    public void setUnbindOnStateSaved(boolean unbind){
        if (presenterDelegate == null){
            throw new IllegalStateException("setUnbindOnStateSaved() should be called inside or after onCreate().");
        }
        presenterDelegate.setUnbindOnStateSaved(unbind);
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate = new PresenterLifecycleHelper<>(this,
                FragmentPresenterStorage.from(requireActivity().getSupportFragmentManager()));
        presenterDelegate.restoreState(savedInstanceState);
        presenterDelegate.markSaveStateChanged(false);
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        presenterDelegate.markSaveStateChanged(true);
        super.onSaveInstanceState(bundle);
        presenterDelegate.saveState(bundle);
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterDelegate.destroy(presenterShouldBeDestroyed());
    }

    @Override
    public void onStart() {
        super.onStart();
        presenterDelegate.markSaveStateChanged(false);
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        presenterDelegate.markSaveStateChanged(false);
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
        return requireActivity().isFinishing();
    }
}