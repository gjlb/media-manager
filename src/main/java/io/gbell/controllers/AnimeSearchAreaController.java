package io.gbell.controllers;

import com.firebase.client.Firebase;
import com.jfoenix.controls.JFXCheckBox;
import io.gbell.MediaManagerMain;
import io.gbell.models.FirebaseEvent;
import io.gbell.models.anime.AnimeSearchResult;
import io.gbell.models.anime.AnimeShow;
import io.gbell.providers.FirebaseProvider;
import io.gbell.services.AnimeApiService;
import io.gbell.utils.DialogUtils;
import io.gbell.utils.FirebaseUtils;
import io.gbell.views.AnimeShowTile;
import io.gbell.views.ShowSearchPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.observables.JavaFxObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static io.gbell.utils.FirebaseUtils.FIREBASE_ANIME_SHOWS;

public class AnimeSearchAreaController implements Initializable {

    @FXML
    private Pane root;

    private ShowSearchPane<AnimeSearchResult> searchPane;

    @Inject
    AnimeApiService animeApiService;

    @Inject
    FirebaseProvider firebaseProvider;

    private Firebase firebase;

    private List<AnimeSearchResult> searchResultList = new ArrayList<>();
    private Func1<AnimeSearchResult, Boolean> filterFunc;
    private JFXCheckBox[] filters;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MediaManagerMain.inject(this);

        filters = new JFXCheckBox[] {
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

        searchPane = new ShowSearchPane<>(getSearchListener());
        searchPane.addFilters(filters);
        root.getChildren().add(searchPane);

        ConnectableObservable<FirebaseEvent<AnimeShow>> observable = firebaseProvider.get()
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .doOnError(e -> DialogUtils.showExceptionDialog(null, "Failed to fetch show information.", e))
                .doOnNext(firebaseRef -> firebase = firebaseRef)
                .flatMap(firebaseRef -> FirebaseUtils.observeForChildEvent(firebaseRef.child(FIREBASE_ANIME_SHOWS), AnimeShow.class))
                .replay();

        observable.connect();
        observable
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .subscribe(event -> {
                    if (event.getAction() == FirebaseEvent.ADDED) {
                        searchPane.reset();
                        searchResultList.clear();
                    }
                });
    }

    private JFXCheckBox createFilter(String text) {
        JFXCheckBox filter = new JFXCheckBox(text);
        filter.getStyleClass().add("jfx-checkbox");
        filter.setSelected(true);
        JavaFxObservable.fromActionEvents(filter).subscribe(event -> updateResults());
        return filter;
    }

    private ShowSearchPane.SearchListener getSearchListener() {
        return text -> animeApiService.search(text)
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .doOnError(e -> searchPane.onSearchError(e))
                .map(searchResults -> {
                    searchResultList = (searchResults == null ? Collections.emptyList() : searchResults);
                    System.out.println("Got " + searchResultList.size() + " results");
                    searchPane.showEmptyMessage();
                    searchPane.clearSearchResults();
                    return searchResultList;
                })
                .flatMap(searchResult -> Observable.from(searchResult))
                .filter(filterFunc)
                .subscribe(searchResult -> {
                    searchPane.showSearchResults();
                    searchPane.addSearchResultTile(new AnimeShowTile(searchResult, firebase));
                });
    }

    private void updateResults() {
        searchPane.clearSearchResults();
        Observable.from(searchResultList)
                .filter(filterFunc)
                .doOnNext(searchResult -> searchPane.addSearchResultTile(new AnimeShowTile(searchResult, firebase)))
                .subscribe();
    }
}
