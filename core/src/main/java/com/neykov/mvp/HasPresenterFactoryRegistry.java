package com.neykov.mvp;

import androidx.annotation.NonNull;

/**
 * A marker interface for structures providing access to a {@linkplain PresenterFactoryRegistry} instance.
 */
public interface HasPresenterFactoryRegistry {
    /**
     * @return a non-null {@linkplain PresenterFactoryRegistry} instance
     */
    @NonNull
    PresenterFactoryRegistry getPresenterFactoryRegistry();
}
