package io.gbell;

import dagger.ObjectGraph;
import io.gbell.modules.MediaManagerModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MediaManagerMain extends Application {

    private static ObjectGraph objectGraph;

    public static void main(String[] args) {
        objectGraph = ObjectGraph.create(new MediaManagerModule());
        launch(args);
    }

    public static void inject(Object object) {
        objectGraph.inject(object);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setOnCloseRequest(event -> System.exit(0));

        URL resource = getClass().getClassLoader().getResource("fxml/library.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root, 1280, 720);

        primaryStage.setTitle("Media Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}