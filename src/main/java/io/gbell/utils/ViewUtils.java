package io.gbell.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class ViewUtils {

    public static void init(Parent view, String fxml, String css) {
        FXMLLoader fxmlLoader = new FXMLLoader(view.getClass().getClassLoader().getResource(fxml));
        fxmlLoader.setRoot(view);
        fxmlLoader.setController(view);
        view.getStylesheets().add(css);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
