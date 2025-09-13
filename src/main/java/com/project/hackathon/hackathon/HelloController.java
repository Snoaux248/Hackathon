package com.project.hackathon.hackathon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class HelloController {

    @FXML private Label welcomeText;
    @FXML public AnchorPane rootPane;

    @FXML private Button Pong;
    @FXML private Button Tetris;
    @FXML private Button Pacman;

    public void initialize() {
        Pong.setOnAction(event -> {
            try {
                Views.getPongView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }
}