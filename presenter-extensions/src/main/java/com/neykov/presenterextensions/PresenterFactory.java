package com.neykov.presenterextensions;

public interface PresenterFactory<P extends Presenter> {
    P createPresenter();
}