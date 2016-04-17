package io.gbell.controllers;

import com.firebase.client.Firebase;
import io.gbell.MediaManagerMain;
import io.gbell.models.FirebaseEvent;
import io.gbell.models.anime.AnimeShow;
import io.gbell.models.tv.TVShow;
import io.gbell.utils.DialogUtils;
import io.gbell.utils.FirebaseUtils;
import io.gbell.utils.TextUtils;
import io.gbell.views.AnimeShowTile;
import io.gbell.views.TVShowTile;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import rx.observables.ConnectableObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;

import static io.gbell.utils.FirebaseUtils.FIREBASE_ANIME_SHOWS;
import static io.gbell.utils.FirebaseUtils.FIREBASE_TV_SHOWS;

public class LibraryController implements Initializable {

    @FXML
    private FlowPane tvLibrary;

    @FXML
    private FlowPane animeLibrary;

    @Inject
    Firebase firebase;

    private final Map<Integer, TVShow> tvShowMap = new Hashtable<>();
    private final Map<Integer, AnimeShow> animeShowMap = new Hashtable<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MediaManagerMain.inject(this);

        Firebase tvShowsRef = firebase.child(FIREBASE_TV_SHOWS);
        ConnectableObservable<FirebaseEvent<TVShow>> tvObservable = FirebaseUtils.observeForChildEvent(tvShowsRef, TVShow.class).replay();
        tvObservable.connect();
        tvObservable
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .doOnError(e -> DialogUtils.showExceptionDialog(null, "Failed to fetch show information.", e))
                .subscribe(event -> {
                    try {
                        TVShow updatedShow = event.getData();
                        switch (event.getAction()) {
                            case FirebaseEvent.ADDED:
                            case FirebaseEvent.CHANGED:
                                tvShowMap.put(updatedShow.getId(), updatedShow);
                                break;
                            case FirebaseEvent.REMOVED:
                                tvShowMap.remove(updatedShow.getId());
                                break;
                        }

                        List<TVShow> shows = new ArrayList<>(tvShowMap.values());
                        shows.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));

                        ObservableList<Node> children = tvLibrary.getChildren();
                        children.clear();

                        for (TVShow show : shows) {
                            children.add(new TVShowTile(show));
                        }
                    } catch (Exception e) {
                        DialogUtils.showExceptionDialog(null, "Failed to fetch show information.", e);
                    }
                });

        Firebase animeShowsRef = firebase.child(FIREBASE_ANIME_SHOWS);
        ConnectableObservable<FirebaseEvent<AnimeShow>> animeObservable = FirebaseUtils.observeForChildEvent(animeShowsRef, AnimeShow.class).replay();
        animeObservable.connect();
        animeObservable
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .doOnError(e -> DialogUtils.showExceptionDialog(null, "Failed to fetch show information.", e))
                .subscribe(event -> {
                    try {
                        AnimeShow updatedShow = event.getData();
                        switch (event.getAction()) {
                            case FirebaseEvent.ADDED:
                            case FirebaseEvent.CHANGED:
                                animeShowMap.put(updatedShow.getId(), updatedShow);
                                break;
                            case FirebaseEvent.REMOVED:
                                animeShowMap.remove(updatedShow.getId());
                                break;
                        }

                        List<AnimeShow> shows = new ArrayList<>(animeShowMap.values());
                        shows.sort((o1, o2) -> TextUtils.getTitle(o1).compareTo(TextUtils.getTitle(o2)));

                        ObservableList<Node> children = animeLibrary.getChildren();
                        children.clear();

                        for (AnimeShow show : shows) {
                            children.add(new AnimeShowTile(show));
                        }
                    } catch (Exception e) {
                        DialogUtils.showExceptionDialog(null, "Failed to fetch show information.", e);
                    }
                });
    }
}