package com.project.hackathon.hackathon;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class Views {

    public static AnchorPane getPongView(AnchorPane rootPane) throws Exception{
        Stage stage = (Stage) rootPane.getScene().getWindow();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(Views.class.getResource("Pong.fxml")));
        stage.getScene().setRoot(pane);
        stage.setTitle("Pong! ");
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(Objects.requireNonNull(Views.class.getResource("styles/Pong.css")).toExternalForm());
        return pane;
    }
    public static AnchorPane getTetrisView(AnchorPane rootPane) throws Exception{
        Stage stage = (Stage) rootPane.getScene().getWindow();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(Views.class.getResource("Tetris.fxml")));
        stage.getScene().setRoot(pane);
        stage.setTitle("Tetris! ");
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(Objects.requireNonNull(Views.class.getResource("styles/Tetris.css")).toExternalForm());

        return pane;
    }
    public static AnchorPane getDinoView(AnchorPane rootPane) throws Exception{
        Stage stage = (Stage) rootPane.getScene().getWindow();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(Views.class.getResource("Dino.fxml")));
        stage.getScene().setRoot(pane);
        stage.setTitle("Dino! ");
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(Objects.requireNonNull(Views.class.getResource("styles/Dino.css")).toExternalForm());

        return pane;
    }
    public static AnchorPane getPacmanView(AnchorPane rootPane) throws Exception{
        Stage stage = (Stage) rootPane.getScene().getWindow();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(Views.class.getResource("Pacman.fxml")));
        stage.getScene().setRoot(pane);
        stage.setTitle("Pacman! ");
        stage.getScene().getStylesheets().clear();
        stage.getScene().getStylesheets().add(Objects.requireNonNull(Views.class.getResource("styles/Pacman.css")).toExternalForm());

        return pane;
    }
    public static AnchorPane getMainView(AnchorPane rootPane) throws Exception{
        Stage stage = (Stage) rootPane.getScene().getWindow();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(Views.class.getResource("hello-view.fxml")));
        stage.getScene().setRoot(pane);
        stage.setTitle("Main! ");
        stage.getScene().getStylesheets().clear();
        return pane;
    }
}
