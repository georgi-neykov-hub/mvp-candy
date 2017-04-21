package com.neykov.mvp;


import android.os.Bundle;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by Georgi on 4/21/2017.
 */
public class PresenterTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Object view;
    private Presenter<Object> presenter;
    private Presenter.OnDestroyListener listener;

    @SuppressWarnings("unchecked")
    @org.junit.Before
    public void setUp() throws Exception {
        presenter = spy(new Presenter<>());
        view = mock(Object.class);
        listener = spy(new Presenter.OnDestroyListener() {
            @Override
            public void onDestroy(Presenter<?> instance) {

            }
        });
    }

    @Test
    public void takeView_ShouldCallOnTakeViewOnce(){

        presenter.takeView(view);
        verify(presenter, times(1)).onTakeView(view);
    }

    @Test
    public void getView_ShouldReturnTheSameAsTakeViewTakes(){

        presenter.takeView(view);
        assertEquals(view, presenter.getView());
    }

    @Test
    public void takeView_ShouldThrowIllegalArgumentExceptionOnNullView(){

        expectedException.expect(IllegalArgumentException.class);
        presenter.takeView(null);
    }

    @Test
    public void dropView_ShouldCallOnDropViewOnce(){

        presenter.takeView(new Object());
        presenter.dropView();
        verify(presenter).onDropView();
    }

    @Test
    public void getView_ShouldReturnNullAfterDropViewIsCalled(){

        presenter.takeView(new Object());
        presenter.dropView();
        assertNull(presenter.getView());
    }

    @Test
    public void destroy_ShouldCallOnDestroyOnce(){

        presenter.destroy();
        verify(presenter).onDestroy();
    }

    @Test
    public void destroy_ShouldCallListenerOnce(){

        presenter.addOnDestroyListener(listener);
        presenter.destroy();
        verify(listener).onDestroy(presenter);
    }

    @Test
    public void removeOnDestroyListener_ShouldPreventOnDestroyToBeCalledOnListener(){

        presenter.addOnDestroyListener(listener);
        presenter.removeOnDestroyListener(listener);
        presenter.destroy();
        verify(listener, never()).onDestroy(presenter);
    }


    @org.junit.After
    public void tearDown() throws Exception {
        presenter = null;
        listener = null;
        view = null;
    }

}