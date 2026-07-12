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
import network.ChatServer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    private static Stage mainStage;
    private ChatServer chatServer;
    private ExecutorService serverPool;

    @Override
    public void start(Stage stage) throws IOException {
        HashtagService.getInstance();

        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("signUp-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("");
        chatServer = new ChatServer();
        serverPool = Executors.newFixedThreadPool(1);
        serverPool.execute(() -> {
            chatServer.start();
        });
        mainStage.setScene(scene);
        mainStage.show();
    }

    @Override
    public void stop() {
        if (chatServer != null) {
            chatServer.stopServer();
        }
        serverPool.shutdown();
        try {
            if (!serverPool.awaitTermination(3, TimeUnit.SECONDS)) {
                serverPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            serverPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void setMainStage(Scene scene){
        mainStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(Main.class, args);
    }
}