package io.gbell.views;

import com.jfoenix.controls.JFXButton;
import io.gbell.utils.TextUtils;
import io.gbell.utils.ViewUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

abstract class ShowTile extends HBox {

    @FXML
    private ImageView coverImage;

    @FXML
    private VBox details;

    @FXML
    private Label title;

    @FXML
    private Label altTitle;

    @FXML
    private HBox minorDetails;

    @FXML
    private Label minorDetail1;

    @FXML
    private Label minorDetail2;

    @FXML
    private Label minorDetail3;

    @FXML
    JFXButton action;

    ShowTile(String coverImage, String title, String altTitle, String detail1, String detail2, String detail3) {
        ViewUtils.initView(this, "fxml/show_tile.fxml", "css/show_tile.css");

        this.coverImage.setImage(new Image(coverImage, 80, 150, true, true, true));
        setText(this.details, this.title, title);
        setText(this.details, this.altTitle, altTitle);
        setText(this.minorDetails, this.minorDetail1, detail1);
        setText(this.minorDetails, this.minorDetail2, detail2);
        setText(this.minorDetails, this.minorDetail3, detail3);

//        details.getChildren().remove(addToLibrary);
//        JavaFxObservable.fromNodeEvents(this, MouseEvent.MOUSE_CLICKED).subscribe(event -> browseShow(show));
    }

    private void setText(Pane parent, Label label, String text) {
        if (TextUtils.isBlank(text)) {
            parent.getChildren().remove(label);
        } else {
            label.setText(text);
        }
    }
}
