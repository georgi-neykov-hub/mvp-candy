package com.neykov.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

public class PresenterLifecycleHelper<P extends Presenter> {
    private static final String KEY_PRESENTER_STATE = "presenter";
    private static final String KEY_PRESENTER_ID = "presenter_id";
    protected final PresenterStorage presenterStorage;
    protected final PresenterFactory<P> presenterFactory;
    @Nullable
    private P presenter;
    @Nullable
    private Bundle bundle;

    private boolean unbindOnStateSaved;
    private boolean stateSaved;

    public void setUnbindOnStateSaved(boolean unbind){
        unbindOnStateSaved = unbind;
        unbindOnSaveStateChangeIfNeeded();
    }

    public PresenterLifecycleHelper(PresenterFactory<P> presenterFactory, PresenterStorage presenterStorage) {
        if (presenterFactory == null) {
            throw new IllegalArgumentException("PresenterFactory argument cannot be null.");
        }
        if (presenterStorage == null) {
            throw new IllegalArgumentException("PresenterStorage argument cannot be null.");
        }
        this.presenterFactory = presenterFactory;
        this.presenterStorage = presenterStorage;
    }

    /**
     * {@link ViewWithPresenter#getPresenter()}
     */
    public P getPresenter() {
        if (presenter == null && bundle != null) {
            String presenterId = bundle.getString(KEY_PRESENTER_ID);
            if (presenterId != null) {
                presenter = presenterStorage.getPresenter(presenterId);
                if (presenter != null) {
                    presenter.addOnDestroyListener(presenterDestroyListener);
                }
            }
        }
        if (presenter == null) {
            presenter = presenterFactory.createPresenter();
            presenterStorage.add(presenter);
            presenter.addOnDestroyListener(presenterDestroyListener);
            presenter.create(bundle == null ? null : bundle.getBundle(KEY_PRESENTER_STATE));
        }
        bundle = null;
        return presenter;
    }

    public void markViewStateRestored(){
        stateSaved= false;
    }

    /**
     * {@link android.app.Activity#onSaveInstanceState(Bundle)}, {@link android.app.Fragment#onSaveInstanceState(Bundle)}, {@link android.view.View#onSaveInstanceState()}.
     */
    public void saveState(Bundle bundle) {
        if (presenter != null) {
            Bundle presenterBundle = new Bundle();
            presenterBundle.putString(KEY_PRESENTER_ID, presenterStorage.getId(presenter));
            presenter.save(presenterBundle);
            bundle.putBundle(KEY_PRESENTER_STATE, presenterBundle);
        }
        stateSaved = true;
        unbindOnSaveStateChangeIfNeeded();
    }

    private void unbindOnSaveStateChangeIfNeeded() {
        if (unbindOnStateSaved && // Action desired
                stateSaved && // Action needed
                presenter != null && // There's a presenter
                presenter.getView() != null // There's a bound view
                ){
            presenter.dropView();
        }
    }

    /**
     * {@link android.app.Activity#onResume()}, {@link android.app.Fragment#onResume()}, {@link android.view.View#onAttachedToWindow()}
     */
    public void bindView(Object view) {
        //noinspection unchecked
        getPresenter().takeView(view);
    }

    /**
     * {@link android.app.Activity#onPause()}, {@link android.app.Fragment#onPause()}, {@link android.view.View#onDetachedFromWindow()}
     */
    public void unbindView(boolean destroy) {
        if (presenter != null) {
            presenter.dropView();
            if (destroy) {
                destroyPresenter();
            }
        }
    }

    private void destroyPresenter() {
        if (presenter == null) {
            throw new AssertionError("Cannot destroy, presenter is null.");
        }
        presenter.removeOnDestroyListener(presenterDestroyListener);
        presenter.destroy();
        presenter = null;
    }

    public void destroy(boolean destroyPresenter) {
        if (presenter != null && destroyPresenter) {
            destroyPresenter();
        }
    }

    public void restoreState(@Nullable Bundle savedState){
        if (savedState != null) {
            if (presenter != null)
                throw new IllegalStateException("getPresenter() called before onCreate()");
            this.bundle = savedState.getBundle(KEY_PRESENTER_STATE);
        }
    }

    private Presenter.OnDestroyListener presenterDestroyListener = new Presenter.OnDestroyListener() {
        @Override
        public void onDestroy(Presenter<?> presenter) {
            presenter.removeOnDestroyListener(this);
            if (PresenterLifecycleHelper.this.presenter == presenter){
                PresenterLifecycleHelper.this.presenter = null;
            }
        }
    };
}
