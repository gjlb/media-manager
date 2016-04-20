package io.gbell.controllers;

import com.firebase.client.Firebase;
import io.gbell.MediaManagerMain;
import io.gbell.models.FirebaseEvent;
import io.gbell.models.tv.TVSearchResult;
import io.gbell.models.tv.TVShow;
import io.gbell.providers.FirebaseProvider;
import io.gbell.services.TVApiService;
import io.gbell.utils.DialogUtils;
import io.gbell.utils.FirebaseUtils;
import io.gbell.utils.TextUtils;
import io.gbell.views.ShowSearchPane;
import io.gbell.views.TVShowTile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import rx.Observable;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static io.gbell.utils.FirebaseUtils.FIREBASE_TV_SHOWS;

public class TVSearchAreaController implements Initializable {

    @FXML
    private Pane root;

    private ShowSearchPane<TVShow> searchPane;

    @Inject
    TVApiService tvApiService;

    @Inject
    FirebaseProvider firebaseProvider;

    private Firebase firebase;

    private final Func1<TVShow, Boolean> filterFunc = searchResult -> !TextUtils.isBlank(searchResult.getStartedAiringDate());

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MediaManagerMain.inject(this);
        searchPane = new ShowSearchPane<>(getSearchListener());
        root.getChildren().add(searchPane);

        ConnectableObservable<FirebaseEvent<TVShow>> observable = firebaseProvider.get()
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .doOnError(e -> DialogUtils.showExceptionDialog(null, "Failed to fetch show information.", e))
                .doOnNext(firebaseRef -> firebase = firebaseRef)
                .flatMap(firebaseRef -> FirebaseUtils.observeForChildEvent(firebaseRef.child(FIREBASE_TV_SHOWS), TVShow.class))
                .replay();

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
                .doOnError(e -> searchPane.onSearchError(e))
                .map(searchResponse -> {
                    List<TVSearchResult> searchResults = searchResponse.getSearchResults();
                    if (searchResults == null) {
                        searchResults = Collections.emptyList();
                    }
                    System.out.println("Got " + searchResults.size() + " results");
                    searchPane.showEmptyMessage();
                    searchPane.clearSearchResults();
                    return searchResults;
                })
                .flatMap(searchResults -> Observable.from(searchResults))
                .flatMap(searchResult -> tvApiService.getDetails(searchResult.getId(), searchResult.getLanguage()))
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .map(searchResult -> searchResult.getShowDetails())
                .filter(filterFunc)
                .subscribe(show -> {
                    searchPane.showSearchResults();
                    searchPane.addSearchResultTile(new TVShowTile(show, firebase));
                });
    }
}
