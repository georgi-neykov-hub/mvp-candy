package com.neykov.mvp;

import android.os.Bundle;

import com.neykov.mvp.core.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Created by Georgi on 4/21/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class PresenterLifecycleHelperTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PresenterLifecycleHelper helper;

    private PresenterStorage presenterStorage;
    private PresenterFactory presenterFactory;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp(){
        presenterStorage = mock(PresenterStorage.class);
        presenterFactory = spy(new PresenterFactory() {
            @Override
            public Presenter createPresenter() {
                return new Presenter();
            }
        });
        helper = createLifecycleHelper();
    }

    @SuppressWarnings("unchecked")
    private <T extends Presenter> PresenterLifecycleHelper<T> createLifecycleHelper() {
        return spy(new PresenterLifecycleHelper<>(presenterFactory, presenterStorage));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void presenterHelperShouldNotAcceptNullConstructorArguments(){

        expectedException.expect(IllegalArgumentException.class);
        new PresenterLifecycleHelper<>(presenterFactory, null);
        new PresenterLifecycleHelper<>(null, presenterStorage);
        new PresenterLifecycleHelper<>(null, null);
    }

    @Test
    public void presenterFactoryShouldBeInvoked(){

        helper.getPresenter();
        verify(presenterFactory).createPresenter();
    }

    @Test
    public void getPresenter_ShouldNotReturnNull(){

        assertNotNull(helper.getPresenter());
    }

    @Test
    public void presenterStorageShouldBeCalled(){

        when(presenterStorage.getId(any(Presenter.class))).thenReturn("abc");

        Bundle innerBundle = new Bundle();
        helper.getPresenter();
        helper.saveState(innerBundle);
        helper = createLifecycleHelper();
        helper.restoreState(innerBundle);
        helper.getPresenter();
        verify(presenterStorage).getPresenter(eq("abc"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void bindView_ShouldCallPresenterTakeViewOnlyOnce(){
        Object view = new Object();
        Presenter<Object> presenter = spy(new Presenter<>());
        when(presenterFactory.createPresenter()).thenReturn(presenter);
        helper.bindView(view);
        verify(presenter).takeView(eq(view));
    }

    @Test
    public void destroy_ShouldCallDestroyPresenter(){

        Presenter<Object> presenter = spy(new Presenter<>());
        when(presenterFactory.createPresenter()).thenReturn(presenter);
        helper.getPresenter();
        helper.destroy(true);
        verify(presenter).destroy();
        verify(presenter).removeOnDestroyListener(any(Presenter.OnDestroyListener.class));
    }

    @Test
    public void destroy_ShouldNotCallDestroyPresenter(){

        Presenter<Object> presenter = spy(new Presenter<>());
        when(presenterFactory.createPresenter()).thenReturn(presenter);
        helper.getPresenter();
        helper.destroy(false);
        verify(presenter, never()).destroy();
        verify(presenter, never()).removeOnDestroyListener(any(Presenter.OnDestroyListener.class));
    }

    @Test
    public void unBindView_ShouldCallPresenterDestroy(){

        Presenter<Object> presenter = spy(new Presenter<>());
        when(presenterFactory.createPresenter()).thenReturn(presenter);
        helper.getPresenter();
        helper.unbindView(true);
        verify(presenter).dropView();
        verify(presenter).destroy();
    }


    @Test
    public void unBindView_ShouldNotCallPresenterDestroy(){

        Presenter<Object> presenter = spy(new Presenter<>());
        when(presenterFactory.createPresenter()).thenReturn(presenter);
        helper.getPresenter();
        helper.unbindView(false);
        verify(presenter).dropView();
        verify(presenter, never()).destroy();
    }



    @After
    public void tearDown(){
        presenterFactory = null;
        presenterStorage = null;
        helper = null;
    }
}
