package io.gbell.controllers;

import com.jfoenix.controls.JFXListView;
import io.gbell.MediaManagerMain;
import io.gbell.models.FirebaseEvent;
import io.gbell.models.anime.AnimeShow;
import io.gbell.models.tv.TVShow;
import io.gbell.providers.FirebaseProvider;
import io.gbell.utils.DialogUtils;
import io.gbell.utils.FirebaseUtils;
import io.gbell.views.AnimeShowRow;
import io.gbell.views.TVShowRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import rx.observables.ConnectableObservable;
import rx.schedulers.JavaFxScheduler;
import rx.schedulers.Schedulers;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

import static io.gbell.utils.FirebaseUtils.FIREBASE_ANIME_SHOWS;
import static io.gbell.utils.FirebaseUtils.FIREBASE_TV_SHOWS;

public class LibraryController implements Initializable {

    @FXML
    private JFXListView<TVShow> tvLibrary;

    @FXML
    private JFXListView<AnimeShow> animeLibrary;

    @Inject
    FirebaseProvider firebaseProvider;

    private final ObservableList<TVShow> tvShowList = FXCollections.observableArrayList();
    private final ObservableList<AnimeShow> animeShowList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MediaManagerMain.inject(this);

        tvLibrary.setItems(tvShowList.sorted((o1, o2) -> o1.compareTo(o2)));
        tvLibrary.setCellFactory(new Callback<ListView<TVShow>, ListCell<TVShow>>() {
                    @Override
                    public ListCell<TVShow> call(ListView<TVShow> listView) {
                        return new ListCell<TVShow>() {
                            @Override
                            protected void updateItem(TVShow item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item != null) {
                                    TVShowRow row = new TVShowRow(item);
                                    row.prefWidthProperty().bind(tvLibrary.widthProperty().subtract(35));
                                    setGraphic(row);
                                }
                            }
                        };
                    }
                });

        ConnectableObservable<FirebaseEvent<TVShow>> tvObservable = firebaseProvider.get()
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .doOnError(e -> DialogUtils.showExceptionDialog(null, "Failed to connect to Firebase.", e))
                .flatMap(firebaseRef -> FirebaseUtils.observeForChildEvent(firebaseRef.child(FIREBASE_TV_SHOWS), TVShow.class))
                .replay();

        tvObservable.connect();
        tvObservable
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .doOnError(e -> DialogUtils.showExceptionDialog(null, "Failed to fetch show information.", e))
                .subscribe(event -> handleShowEvent(event, tvShowList));
//                .doOnNext(event -> {
//                    updateShowMap(event, tvShowMap, event.getData().getId());
//                    tvLibrary.getItems().clear();
//                })
//                .flatMap(searchResults -> {
//                    List<TVShow> shows = new ArrayList<>(tvShowMap.values());
//                    shows.sort((o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
//                    return Observable.from(shows);
//                })
//                .flatMap(tvShow -> {
//                    try {
//                        return Observable.just(new TVShowTile(tvShow, null));
//                    } catch (Exception e) {
//                        return Observable.error(e);
//                    }
//                })
//                .subscribe(tile -> {
//                    tvLibrary.getChildren().add(tile);
//                });

        animeLibrary.setItems(animeShowList.sorted((o1, o2) -> o1.compareTo(o2)));
        animeLibrary.setCellFactory(new Callback<ListView<AnimeShow>, ListCell<AnimeShow>>() {
            @Override
            public ListCell<AnimeShow> call(ListView<AnimeShow> listView) {
                return new ListCell<AnimeShow>() {
                    @Override
                    protected void updateItem(AnimeShow item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            AnimeShowRow row = new AnimeShowRow(item);
                            row.prefWidthProperty().bind(animeLibrary.widthProperty().subtract(35));
                            setGraphic(row);
                        }
                    }
                };
            }
        });

        ConnectableObservable<FirebaseEvent<AnimeShow>> animeObservable = firebaseProvider.get()
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .doOnError(e -> DialogUtils.showExceptionDialog(null, "Failed to connect to Firebase.", e))
                .flatMap(firebaseRef -> FirebaseUtils.observeForChildEvent(firebaseRef.child(FIREBASE_ANIME_SHOWS), AnimeShow.class))
                .replay();

        animeObservable.connect();
        animeObservable
                .subscribeOn(Schedulers.newThread())        // wait for result on background thread...
                .observeOn(JavaFxScheduler.getInstance())   // ...then take action with result on main thread
                .doOnError(e -> DialogUtils.showExceptionDialog(null, "Failed to fetch show information.", e))
                .subscribe(event -> handleShowEvent(event, animeShowList));
//                .doOnNext(event -> {
//                    updateShowMap(event, animeShowMap, event.getData().getId());
//                    animeLibrary.getItems().clear();
//                })
//                .flatMap(searchResults -> {
//                    List<AnimeShow> shows = new ArrayList<>(animeShowMap.values());
//                    shows.sort((o1, o2) -> TextUtils.getTitle(o1).compareTo(TextUtils.getTitle(o2)));
//                    return Observable.from(shows);
//                })
//                .flatMap(animeShow -> {
//                    try {
//                        return Observable.just(new AnimeShowRow(animeShow));
//                    } catch (Exception e) {
//                        return Observable.error(e);
//                    }
//                })
//                .subscribe(row -> {
//                    animeLibrary.getChildren().add(row);
//                    row.prefWidthProperty().bind(animeLibrary.prefWidthProperty());
//                });
    }
//
    private <T> void handleShowEvent(FirebaseEvent<T> event, ObservableList<T> resultList) {
        T show = event.getData();
        switch (event.getAction()) {
            case FirebaseEvent.ADDED:
                resultList.add(show);
                break;
            case FirebaseEvent.CHANGED:
                resultList.remove(show);
                resultList.add(show);
                break;
            case FirebaseEvent.REMOVED:
                resultList.remove(show);
                break;
        }
    }
}