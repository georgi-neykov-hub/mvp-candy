package com.neykov.mvp;

import androidx.annotation.NonNull;

/**
 * A utility for centralizing the provisioning of {@linkplain PresenterFactory}
 * instances over different types of {@linkplain Presenter} subclasses.
 */
public interface PresenterFactoryRegistry {
    /**
     * @param type the non-null type for which a factory is requested.
     * @param <P> A subclass of {@linkplain Presenter}
     * @throws IllegalArgumentException if a {@linkplain PresenterFactory} cannot be provided for the given type
     * @return an non-null {@linkplain PresenterFactory} for the provided type, if one is available
     */
    @NonNull
    <P extends Presenter<?>> PresenterFactory<P> get(@NonNull Class<P> type);
}
