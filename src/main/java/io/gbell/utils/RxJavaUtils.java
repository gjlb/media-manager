package io.gbell.utils;

import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

public class RxJavaUtils {

//    public static <T> Observable<T> observeInBackground(Observable<T> observable) {
//        return observable
//                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
//                .observeOn(JavaFxScheduler.getInstance());  // ...then take action with result on main thread
//    }
//
//    public static <T> Observable<T> toHotObservable(Observable<T> coldObservable) {
//        ConnectableObservable<T> hotObservable = coldObservable.replay();
//        hotObservable.connect();
//        return hotObservable;
//    }

}
