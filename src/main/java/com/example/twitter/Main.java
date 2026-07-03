package com.example.twitter;

import LogicController.HashtagService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        HashtagService.getInstance();
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("signUp-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void setMainStage(Scene scene){
        mainStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(Main.class, args);
    }
}