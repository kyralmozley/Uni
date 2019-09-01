package group27.weatherapp;

import group27.weatherapp.map_view.MapSceneFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hill Walkers Weather");
        try {
            MapSceneFactory sceneFactory = new MapSceneFactory();
            sceneFactory.setDimensions(500, 756);
            primaryStage.setScene(sceneFactory.loadScene()); //phone resolution
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
