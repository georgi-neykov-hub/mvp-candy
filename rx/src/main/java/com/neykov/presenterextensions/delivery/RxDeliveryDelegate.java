package com.neykov.presenterextensions.delivery;

import rx.Notification;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

public class RxDeliveryDelegate<R> {

    private Observable<R> targetObservable;
    private Observable<Boolean> targetStatusObservable;

    public RxDeliveryDelegate(Observable<R> targetObservable, Observable<Boolean> targetStatusObservable) {
        if(targetObservable == null){
            throw new IllegalArgumentException("Null target Observable provided.");
        }

        if(targetStatusObservable == null){
            throw new IllegalArgumentException("Null status Observable provided.");
        }

        this.targetObservable = targetObservable;
        this.targetStatusObservable = targetStatusObservable;
    }

    /**
     * Returns a transformer that will delay onNext, onError and onComplete emissions unless a view become available.
     * getView() is guaranteed to be != null during all emissions. This transformer can only be used on application's main thread.
     * <p/>
     * Use this operator if you need to deliver *all* emissions to a view, in example when you're sending items
     * into adapter one by one.
     *
     * @param <T> a type of onNext value.
     * @return the delaying operator.
     */
    public  <T> Observable.Transformer<T, Delivery<R,T>> deliver(){
        return new Observable.Transformer<T, Delivery<R, T>>() {
            @Override
            public Observable<Delivery<R, T>> call(Observable<T> observable) {
                return observable.lift(
                        OperatorSemaphore.<T>semaphore(targetStatusObservable))
                        .materialize()
                        .filter(new Func1<Notification<T>, Boolean>() {
                            @Override
                            public Boolean call(Notification<T> notification) {
                                return !notification.isOnCompleted();
                            }
                        })
                        .withLatestFrom(targetObservable, new Func2<Notification<T>, R, Delivery<R, T>>() {
                            @Override
                            public Delivery<R, T> call(Notification<T> notification, R view) {
                                return new Delivery<R, T>(view, notification);
                            }
                        });
            }
        };
    }

    /**
     * Returns a transformer that will delay onNext, onError and onComplete emissions unless a view become available.
     * getView() is guaranteed to be != null during all emissions. This transformer can only be used on application's main thread.
     * <p/>
     * If this transformer receives a next value while the previous value has not been delivered, the
     * previous value will be dropped.
     * <p/>
     * Use this operator when you need to show updatable data.
     *
     * @param <T> a type of onNext value.
     * @return the delaying operator.
     */
    public <T> Observable.Transformer<T, Delivery<R,T>> deliverLatest(){
        return new Observable.Transformer<T, Delivery<R, T>>() {
            @Override
            public Observable<Delivery<R, T>> call(Observable<T> observable) {
                return observable.lift(
                        OperatorSemaphore.<T>semaphoreLatest(targetStatusObservable))
                        .materialize()
                        .filter(new Func1<Notification<T>, Boolean>() {
                            @Override
                            public Boolean call(Notification<T> notification) {
                                return !notification.isOnCompleted();
                            }
                        })
                        .withLatestFrom(targetObservable, new Func2<Notification<T>, R, Delivery<R, T>>() {
                            @Override
                            public Delivery<R, T> call(Notification<T> notification, R view) {
                                return new Delivery<R, T>(view, notification);
                            }
                        });
            }
        };
    }
}
