package io.gbell.providers;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import rx.Observable;
import rx.Subscriber;
import rx.observables.ConnectableObservable;

public class FirebaseProvider {

    private static final String URL = "https://gjlb-media-manager.firebaseio.com";
    private static final String TEST_URL = "https://gjlb-media-manager-test.firebaseio.com";
    private static final String USERNAME = "graham13@gmail.com";
    private static final String PASSWORD = "media-manager-admin-456";

    private final ConnectableObservable<Firebase> firebaseObservable;

    public FirebaseProvider() {
        firebaseObservable = Observable
                .create(new Observable.OnSubscribe<Firebase>() {
                    @Override
                    public void call(Subscriber<? super Firebase> subscriber) {
                        final Firebase firebase = new Firebase(URL);
                        firebase.authWithPassword(USERNAME, PASSWORD, new Firebase.AuthResultHandler() {
                            @Override
                            public void onAuthenticated(AuthData authData) {
                                subscriber.onNext(firebase);
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {
                                subscriber.onError(firebaseError.toException());
                            }
                        });
                    }
                })
                .replay();
        firebaseObservable.connect();
    }

    public Observable<Firebase> get() {
        return firebaseObservable.asObservable();
    }
}
