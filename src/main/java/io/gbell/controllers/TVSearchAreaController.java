package io.gbell.controllers;

import com.firebase.client.Firebase;
import io.gbell.MediaManagerMain;
import io.gbell.models.FirebaseEvent;
import io.gbell.models.tv.TVShow;
import io.gbell.services.TVApiService;
import io.gbell.utils.FirebaseUtils;
import io.gbell.views.ShowSearchPane;
import io.gbell.views.TVShowTile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import rx.Observable;
import rx.observables.ConnectableObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static io.gbell.utils.FirebaseUtils.FIREBASE_TV_SHOWS;

public class TVSearchAreaController implements Initializable {

    @FXML
    private Pane root;

    private ShowSearchPane searchPane;

    @Inject
    TVApiService tvApiService;

    @Inject
    Firebase firebase;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MediaManagerMain.inject(this);
        searchPane = new ShowSearchPane(getSearchListener());
        root.getChildren().add(searchPane);

        Firebase showsRef = firebase.child(FIREBASE_TV_SHOWS);
        ConnectableObservable<FirebaseEvent<TVShow>> observable = FirebaseUtils.observeForChildEvent(showsRef, TVShow.class).replay();
        observable.connect();
        observable
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .subscribe(event -> {
                    if (event.getAction() == FirebaseEvent.ADDED) {
                        searchPane.reset();
                    }
                });
    }

    private ShowSearchPane.SearchListener getSearchListener() {
        return text -> tvApiService.search(text)
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .map(searchResponse -> searchResponse.getSearchResults())
                .doOnError(e -> searchPane.onSearchError(e))
                .doOnNext(searchResults -> {
                    System.out.println("Got " + searchResults.size() + " results");
                    searchPane.showSearchResults(true);
                })
                .flatMap(searchResults -> Observable.from(searchResults))
                .flatMap(result -> tvApiService.getDetails(result.getId(), result.getLanguage()))
                .doOnNext(tvShow -> searchPane.addSearchResultTile(new TVShowTile(tvShow.getShowDetails())))
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .subscribe();
    }
}
