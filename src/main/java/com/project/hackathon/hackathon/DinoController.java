package com.project.hackathon.hackathon;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class DinoController {

    @FXML public AnchorPane rootPane;
    @FXML public Button backToMain;
    @FXML public Rectangle dino;
    @FXML public Rectangle cactus;
    @FXML public Line ground;
    @FXML public Text gameOverText;
    @FXML public Button restartButton;

    //Knowledge
    private double velocityY = 0;
    private final double gravity = 0.5;
    private final double jumpStrength = 10;
    private boolean jumping = false;

    private double cactusSpeed = 4;
    private AnimationTimer gameLoop;
    private boolean gameOver = false;

    public void initialize() {
        ground.endXProperty().bind(rootPane.widthProperty());
        backToMain.setOnAction(event -> {
            try {
                Views.getMainView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        //Restart Button
        restartButton.setOnAction(event -> resetGame());

        //Jumping and Reset with Space
        Platform.runLater(() -> {
            HelloApplication.primaryStage.getScene().getRoot().setFocusTraversable(true);
            HelloApplication.primaryStage.getScene().getRoot().requestFocus();
            HelloApplication.primaryStage.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE && !jumping) {
                    velocityY = -jumpStrength;
                    jumping = true;
                }
                //Game reset after GAME OVER
                if (gameOver && event.getCode() == KeyCode.SPACE) {
                    resetGame();
                }
            });

            //reset button
            restartButton.setOnAction(event -> resetGame());
        });

        //Game Loop
        gameLoop = new AnimationTimer() {
            @Override public void handle(long now) {
                if (!gameOver) updateGame();
            }
        };
        gameLoop.start();
    }

    private void updateGame() {
        //gravity usage
        velocityY += gravity;
        dino.setY(dino.getY() + velocityY);

        //stop at ground
        double groundY = ground.getStartY() - dino.getHeight();
        if(dino.getY() >= groundY) {
            dino.setY(groundY);
            velocityY = 0;
            jumping = false;
        }

        //move cactus
        cactus.setX(cactus.getX() - cactusSpeed);

        //reset cactus when it goes offscreen
        if(cactus.getX() + cactus.getWidth() < 0) {
            cactus.setX(rootPane.getWidth());
        }

        //collision detection
        if(dino.getBoundsInParent().intersects(cactus.getBoundsInParent())) {
            triggerGameOver();
        }
    }

    private void triggerGameOver() {
        gameOverText.setVisible(true);
        restartButton.setVisible(true);
        gameOver = true;
        velocityY = 0;
    }

    private void resetGame() {
        gameOverText.setVisible(false);
        restartButton.setVisible(false);
        gameOver = false;
        cactus.setX(rootPane.getWidth());
        dino.setY(ground.getStartY() - dino.getHeight());
        velocityY = 0;
        jumping = false;

        Platform.runLater(() -> rootPane.requestFocus());
    }
}
