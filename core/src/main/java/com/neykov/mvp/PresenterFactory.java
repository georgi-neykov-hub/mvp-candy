package com.neykov.mvp;

public interface PresenterFactory<P extends Presenter> {
    P createPresenter();
}