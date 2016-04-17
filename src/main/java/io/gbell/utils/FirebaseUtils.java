package io.gbell.utils;

import com.firebase.client.*;
import io.gbell.models.FirebaseEvent;
import rx.Observable;
import rx.Subscriber;

public class FirebaseUtils {

    public static final String FIREBASE_TV_SHOWS = "tv_shows";
    public static final String FIREBASE_TV_EPISODES = "tv_episodes";
    public static final String FIREBASE_ANIME_SHOWS = "anime_shows";
    public static final String FIREBASE_ANIME_EPISODES = "anime_episodes";

    public static Observable<DataSnapshot> observeForSingleValueEvent(final Firebase child) {
        return Observable
                .create(new Observable.OnSubscribe<DataSnapshot>() {
                    @Override
                    public void call(Subscriber<? super DataSnapshot> subscriber) {
                        child.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                subscriber.onNext(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                subscriber.onError(firebaseError.toException());
                            }
                        });
                    }
                });
    }

    public static <T> Observable<FirebaseEvent<T>> observeForChildEvent(final Firebase child, final Class<T> clazz) {
        return Observable
                .create(new Observable.OnSubscribe<FirebaseEvent<T>>() {
                    @Override
                    public void call(Subscriber<? super FirebaseEvent<T>> subscriber) {
                        child.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                subscriber.onNext(new FirebaseEvent<>(FirebaseEvent.ADDED, dataSnapshot.getValue(clazz)));
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                subscriber.onNext(new FirebaseEvent<>(FirebaseEvent.CHANGED, dataSnapshot.getValue(clazz)));
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                subscriber.onNext(new FirebaseEvent<>(FirebaseEvent.REMOVED, dataSnapshot.getValue(clazz)));
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                subscriber.onNext(new FirebaseEvent<>(FirebaseEvent.MOVED, dataSnapshot.getValue(clazz)));
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                subscriber.onError(firebaseError.toException());
                            }
                        });
                    }
                });
    }

}
