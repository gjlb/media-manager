package io.gbell.controllers;

import com.firebase.client.Firebase;
import io.gbell.MediaManagerMain;
import io.gbell.models.FirebaseEvent;
import io.gbell.models.anime.AnimeSearchResult;
import io.gbell.models.anime.AnimeShow;
import io.gbell.services.AnimeApiService;
import io.gbell.utils.FirebaseUtils;
import io.gbell.views.AnimeShowTile;
import io.gbell.views.ShowSearchPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import rx.Observable;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static io.gbell.utils.FirebaseUtils.FIREBASE_ANIME_SHOWS;

public class AnimeSearchAreaController implements Initializable {

    @FXML
    private Pane root;

    private ShowSearchPane searchPane;

    @Inject
    AnimeApiService animeApiService;

    @Inject
    Firebase firebase;

    private final List<AnimeSearchResult> searchResultList = new ArrayList<>();
    private CheckBox[] filters;
    private Func1<AnimeSearchResult, Boolean> filterFunc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MediaManagerMain.inject(this);

        filters = new CheckBox[] {
                createFilter("TV"),
                createFilter("OVA"),
                createFilter("Special"),
                createFilter("Movie"),
        };

        filterFunc = searchResult -> {
            for (CheckBox filter : filters) {
                if (filter.isSelected() && filter.getText().equalsIgnoreCase(searchResult.getShowType())) {
                    return true;
                }
            }
            return false;
        };

        searchPane = new ShowSearchPane(getSearchListener());
        searchPane.addFilters(filters);
        root.getChildren().add(searchPane);

        Firebase showsRef = firebase.child(FIREBASE_ANIME_SHOWS);
        ConnectableObservable<FirebaseEvent<AnimeShow>> observable = FirebaseUtils.observeForChildEvent(showsRef, AnimeShow.class).replay();
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

    private CheckBox createFilter(String text) {
        CheckBox filter = new CheckBox(text);
        filter.setSelected(true);
        JavaFxObservable.fromActionEvents(filter).subscribe(event -> updateResults());
        return filter;
    }

    private ShowSearchPane.SearchListener getSearchListener() {
        return text -> animeApiService.search(text)
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .doOnError(e -> searchPane.onSearchError(e))
                .doOnNext(searchResults -> {
                    System.out.println("Got " + searchResults.size() + " results");
                    searchPane.showSearchResults(true);
                    searchResultList.clear();
                    searchResultList.addAll(searchResults);
                })
                .flatMap(searchResult -> Observable.from(searchResult))
                .filter(filterFunc)
                .doOnNext(searchResult -> searchPane.addSearchResultTile(new AnimeShowTile(searchResult)))
                .subscribe();
    }

    private void updateResults() {
        searchPane.showSearchResults(true);
        Observable.from(searchResultList)
                .filter(filterFunc)
                .doOnNext(searchResult -> searchPane.addSearchResultTile(new AnimeShowTile(searchResult)))
                .subscribe();
    }
}
