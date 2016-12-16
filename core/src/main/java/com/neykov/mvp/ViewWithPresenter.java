package com.neykov.mvp;

public interface ViewWithPresenter<P extends Presenter> {

    /**
     * @return the currently attached presenter or null.
     */
    P getPresenter();
}
