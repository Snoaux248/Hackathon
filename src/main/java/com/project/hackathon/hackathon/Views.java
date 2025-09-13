package com.project.hackathon.hackathon;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class Views {

    public static AnchorPane getPongView(AnchorPane rootPane) throws Exception{
        Stage stage = (Stage) rootPane.getScene().getWindow();
        AnchorPane pane = FXMLLoader.load(Objects.requireNonNull(Views.class.getResource("pong.fxml")));
        stage.getScene().setRoot(pane);
        stage.setTitle("Pong! ");
        return pane;
    }
}
