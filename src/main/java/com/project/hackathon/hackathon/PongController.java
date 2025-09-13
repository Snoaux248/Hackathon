package com.project.hackathon.hackathon;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;

import java.awt.event.KeyEvent;

public class PongController {

    @FXML public AnchorPane rootPane;

    @FXML public Button backToMain;
    @FXML public Rectangle player;
    @FXML public Rectangle clanker;

    @FXML public Rectangle ball;

    public boolean hasStarted = false;

    public void initialize(){
        backToMain.setOnAction(event -> {
            try {
                Views.getMainView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        player.setOnMouseClicked(event -> {
            System.out.println(player.getY());
        });
        HelloApplication.primaryStage.getScene().setOnMouseClicked(event -> {
            System.out.println("Mouse clicked at: " + event.getSceneX() + ", " + event.getSceneY());
        });
        Platform.runLater(() -> {
            HelloApplication.primaryStage.getScene().getRoot().setFocusTraversable(true);
            HelloApplication.primaryStage.getScene().getRoot().requestFocus();
            HelloApplication.primaryStage.getScene().setOnKeyPressed(event -> {
                System.out.println(event.getCode());
                if (event.getCode() == KeyCode.UP) {
                    if ((player.getY() - 20) > (-20)){
                        player.setY(player.getY() -20);
                    }
                } else if (event.getCode() == KeyCode.DOWN) {
                    if ((player.getY() + 20) < ((int)HelloApplication.primaryStage.getScene().getHeight() -80)){
                        System.out.println(""+ (int)HelloApplication.primaryStage.getScene().getHeight()+ " "+ player.getY());
                        player.setY(player.getY() + 20);
                    }
                }
            });
        });
        ball.setY((int)HelloApplication.primaryStage.getScene().getHeight()/2);
        ball.setX((int)HelloApplication.primaryStage.getScene().getWidth()/2);

        player.setY((int)(HelloApplication.primaryStage.getScene().getHeight()-100)/2);
        clanker.setY((int)(HelloApplication.primaryStage.getScene().getHeight()-100)/2);

    }

    AnimationTimer gameLoop = new AnimationTimer() {
        @Override
        public void handle(long now) {
            // This code runs every frame (~60 times per second)
            //update();  // update game logic
            //render();  // redraw objects if needed
        }
    };

    // Start the loop
}
