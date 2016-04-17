package io.gbell.views;

import io.gbell.utils.TextUtils;
import io.gbell.utils.ViewUtils;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import rx.Subscription;
import rx.observables.JavaFxObservable;

public class ShowSearchPane extends VBox {

    public interface SearchListener {
        public Subscription performSearch(String text);
    }

    @FXML
    private TextField query;

    @FXML
    private HBox filterArea;

    @FXML
    private VBox results;

    @FXML
    private ProgressIndicator waiting;

    @FXML
    private Label error;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private HBox resultsContainer;

    private final SearchListener searchListener;
    private String prevQuery = "";
    private Subscription subscription;

    public ShowSearchPane(SearchListener searchListener) {
        ViewUtils.initView(this, "fxml/show_search_pane.fxml", "css/show_search_pane.css");

        this.searchListener = searchListener;
        results.getChildren().clear();
        resultsContainer.minWidthProperty().bind(results.widthProperty());

        if (searchListener != null) {
            JavaFxObservable.fromActionEvents(query).subscribe(event -> onSearch());
        }
    }

    public void addFilters(CheckBox[] filters) {
        for (CheckBox filter : filters) {
            filterArea.getChildren().addAll(filter);
        }
    }

    private void setSearchAreaChild(Node child, Pos alignment) {
        results.getChildren().clear();
        if (child != null) {
            results.getChildren().add(child);
            results.setAlignment(alignment);
        }
    }

    public void addSearchResultTile(Node tile) {
        resultsContainer.getChildren().add(tile);
    }

    public void showSearchResults(boolean clear) {
        setSearchAreaChild(scrollPane, Pos.CENTER_LEFT);
        scrollPane.setHvalue(0);
        if (clear) {
            resultsContainer.getChildren().clear();
        }
    }

    public void onSearchError(Throwable e) {
        subscription = null;
        setSearchAreaChild(error, Pos.CENTER);
        e.printStackTrace();
    }

    private void onSearch() {
        final String text = query.getText();
        if (prevQuery.equalsIgnoreCase(text)) {
            return;
        }

        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }

        if (TextUtils.isBlank(text)) {
            setSearchAreaChild(null, null);
            prevQuery = "";
            return;
        }

        prevQuery = text;
        setSearchAreaChild(waiting, Pos.CENTER);

        subscription = searchListener.performSearch(text);
    }

    public void reset() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
        setSearchAreaChild(null, null);
        prevQuery = "";
        query.setText(prevQuery);
    }
}
