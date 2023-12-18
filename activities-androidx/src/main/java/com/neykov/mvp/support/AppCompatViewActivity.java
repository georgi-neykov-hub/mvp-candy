package com.neykov.mvp.support;

import android.os.Bundle;

import com.neykov.mvp.Presenter;
import com.neykov.mvp.PresenterFactory;
import com.neykov.mvp.PresenterLifecycleHelper;
import com.neykov.mvp.ViewWithPresenter;

import androidx.annotation.CallSuper;
import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class AppCompatViewActivity<P extends Presenter> extends AppCompatActivity implements ViewWithPresenter<P>, PresenterFactory<P> {

    private PresenterLifecycleHelper<P> presenterDelegate;

    public AppCompatViewActivity() {super();}

    @ContentView
    public AppCompatViewActivity(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    public void setUnbindOnStateSaved(boolean unbind) {
        if (presenterDelegate == null) {
            throw new IllegalStateException("setUnbindOnStateSaved() should be called inside or after onCreate().");
        }
        presenterDelegate.setUnbindOnStateSaved(unbind);
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenterDelegate = new PresenterLifecycleHelper<>(this,
                FragmentPresenterStorage.from(getSupportFragmentManager()));
        presenterDelegate.restoreState(savedInstanceState);
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
        presenterDelegate.destroy(presenterShouldBeDestroyed() || !isChangingConfigurations());
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
    public void onStateNotSaved() {
        presenterDelegate.markSaveStateChanged(false);
        super.onStateNotSaved();
    }

    @Override
    public final P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    protected boolean presenterShouldBeDestroyed() {
        return isFinishing();
    }
}
