package com.project.hackathon.hackathon;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class DinoController {

    @FXML public AnchorPane rootPane;
    @FXML public Button backToMain;
    @FXML public Rectangle dino;
    @FXML public Rectangle cactus;
    @FXML public Line ground;

    //Knowledge
    private double velocityY = 0;
    private final double gravity = -9.8;
    private final double jumpStrength = 10;
    private boolean jumping = false;

    private double cactusSpeed = 4;

    public void initialize() {
        backToMain.setOnAction(event -> {
            try {
                Views.getMainView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        //Jumping
        Platform.runLater(() -> {
            HelloApplication.primaryStage.getScene().getRoot().setFocusTraversable(true);
            HelloApplication.primaryStage.getScene().getRoot().requestFocus();
            HelloApplication.primaryStage.getScene().setOnKeyPressed(event -> {
                System.out.println(event.getCode());
                if (event.getCode() == KeyCode.SPACE && !jumping) {
                    velocityY = jumpStrength;
                    jumping = true;
                }
            });

        });
    }
}
