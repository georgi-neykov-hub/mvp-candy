package com.neykov.presenterextensions;

import android.support.annotation.NonNull;

public interface PresenterStorage {
    /**
     * Adds a presenter to the storage
     *
     * @param presenter a presenter to add
     */
    void add(@NonNull final Presenter presenter);

    /**
     * Returns a presenter by id or null if such presenter does not exist.
     *
     * @param id  id of a presenter that has been received by calling {@link #getId(Presenter)}
     * @param <P> a type of presenter
     * @return a presenter or null
     */
    <P> P getPresenter(@NonNull String id);

    /**
     * Returns id of a given presenter.
     *
     * @param presenter a presenter to get id for.
     * @return if of the presenter.
     */
    String getId(@NonNull Presenter presenter);

    Presenter remove(String id);
    String remove(Presenter presenter);

    /**
     * Removes all presenters from the storage.
     * Use this method for testing purposes only.
     */
    void clear();
}
