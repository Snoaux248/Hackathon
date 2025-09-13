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
    @FXML private Button Dino;

    public void initialize() {
        Pong.setOnAction(event -> {
            try {
                Views.getPongView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        Tetris.setOnAction(event -> {
            try {
                Views.getTetrisView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        Dino.setOnAction(event -> {
            try {
                Views.getDinoView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        Pacman.setOnAction(event -> {
            try {
                Views.getPacmanView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });

    }
}