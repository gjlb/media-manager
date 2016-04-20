package io.gbell.views;

import io.gbell.utils.TextUtils;
import io.gbell.utils.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

abstract class ShowRow extends HBox {

    @FXML
    private HBox posterArea;

    @FXML
    private ImageView coverImage;

    @FXML
    private FlowPane mainDetails;

    @FXML
    private Label title;

    @FXML
    private Label altTitle;

    @FXML
    private Label minorDetail1;

    @FXML
    private Label minorDetail2;

    @FXML
    private Label minorDetail3;

    @FXML
    private Label minorDetail4;

    @FXML
    private Label airing;

    @FXML
    private Label synopsis;

    ShowRow(String coverImage, String title, String altTitle, String detail1, String detail2, String detail3, String detail4, String airing, String synopsis) {
        ViewUtils.initView(this, "fxml/show_row.fxml", "css/show_row.css");
        setImage(this.posterArea, this.coverImage, coverImage);
        setText(this.mainDetails, this.title, title);
        setText(this.mainDetails, this.altTitle, altTitle);
        setText(this.mainDetails, this.minorDetail1, detail1);
        setText(this.mainDetails, this.minorDetail2, detail2);
        setText(this.mainDetails, this.minorDetail3, detail3);
        setText(this.mainDetails, this.minorDetail4, detail4);
        setText(this.mainDetails, this.airing, airing);
        setText(this.mainDetails, this.synopsis, synopsis);
    }

    private void setImage(Pane parent, ImageView imageView, String url) {
        if (TextUtils.isBlank(url)) {
            parent.getChildren().remove(imageView);
        } else {
            imageView.setImage(new Image(url, 80, 150, true, true, true));
        }
    }

    private void setText(Pane parent, Label label, String text) {
        if (TextUtils.isBlank(text)) {
            parent.getChildren().remove(label);
        } else {
            label.setText(text);
        }
    }
}
