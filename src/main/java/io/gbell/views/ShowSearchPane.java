package io.gbell.views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
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

public class ShowSearchPane<T> extends VBox {

    public interface SearchListener {
        public Subscription performSearch(String text);
    }

    @FXML
    private JFXTextField query;

    @FXML
    private HBox filterArea;

    @FXML
    private VBox results;

    @FXML
    private JFXSpinner waiting;

    @FXML
    private Label error;

    @FXML
    private Label empty;

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

    public void addFilters(JFXCheckBox[] filters) {
        for (JFXCheckBox filter : filters) {
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

    public void addSearchResultTile(ShowTile tile) {
        resultsContainer.getChildren().add(tile);
    }

    public void showSearchResults() {
        setSearchAreaChild(scrollPane, Pos.CENTER_LEFT);
        scrollPane.setHvalue(0);
    }

    public void clearSearchResults() {
        resultsContainer.getChildren().clear();
    }

    public void showEmptyMessage() {
        setSearchAreaChild(empty, Pos.CENTER_LEFT);
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
        clearSearchResults();
        setSearchAreaChild(null, null);
        prevQuery = "";
        query.setText(prevQuery);
    }
}
