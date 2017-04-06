package com.neykov.mvp;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DefaultPresenterStorage implements PresenterStorage {
    private HashMap<String, Presenter<?>> idToPresenter = new HashMap<>();
    private HashMap<Presenter<?>, String> presenterToId = new HashMap<>();

    /**
     * Adds a presenter to the storage
     *
     * @param presenter a presenter to add
     */
    @Override
    public void add(@NonNull final Presenter<?> presenter) {
        String id = presenter.getClass().getSimpleName() + "/" + System.nanoTime() + "/" + (int) (Math.random() * Integer.MAX_VALUE);
        idToPresenter.put(id, presenter);
        presenterToId.put(presenter, id);
        presenter.addOnDestroyListener(onDestroyListener);
    }

    /**
     * Returns a presenter by id or null if such presenter does not exist.
     *
     * @param id  id of a presenter that has been received by calling {@link #getId(Presenter)}
     * @param <P> a type of presenter
     * @return a presenter or null
     */
    @Override
    public <P extends Presenter<?>> P getPresenter(@NonNull String id) {
        //noinspection unchecked
        return (P)idToPresenter.get(id);
    }

    /**
     * Returns id of a given presenter.
     *
     * @param presenter a presenter to get id for.
     * @return if of the presenter.
     */
    @Override
    public String getId(@NonNull Presenter presenter) {
        return presenterToId.get(presenter);
    }

    @Override
    public Presenter remove(String id) {
        Presenter presenter = idToPresenter.remove(id);
        if(presenter != null){
            presenterToId.remove(presenter);
            presenter.removeOnDestroyListener(onDestroyListener);
        }
        return presenter;
    }

    @Override
    public String remove(Presenter presenter) {
        String id = presenterToId.remove(presenter);
        if(id != null){
            idToPresenter.remove(id);
            presenter.removeOnDestroyListener(onDestroyListener);
        }
        return id;
    }

    /**
     * Removes all presenters from the storage.
     * Use this method for testing purposes only.
     */
    @Override
    public void clear() {
        final Collection<Presenter<?>> presenters = new ArrayList<>(presenterToId.keySet());
        for (Presenter<?> presenter : presenters){
            presenter.removeOnDestroyListener(onDestroyListener);
            presenter.destroy();
        }
        presenterToId.clear();
        idToPresenter.clear();
    }

    private Presenter.OnDestroyListener onDestroyListener = new Presenter.OnDestroyListener() {
        @Override
        public void onDestroy(Presenter<?> instance) {
            instance.removeOnDestroyListener(this);
            String id = presenterToId.remove(instance);
            if (id != null) {
                idToPresenter.remove(id);
            }
        }
    };
}
