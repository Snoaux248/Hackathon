package com.project.hackathon.hackathon;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.Random;

public class DinoController {

    @FXML public AnchorPane rootPane;
    @FXML public Button backToMain;
    @FXML public Rectangle dino;
    @FXML public Rectangle cactus;
    @FXML public Line ground;
    @FXML public Text gameOverText;
    @FXML public Button restartButton;
    @FXML public HBox multi;

    // Physics
    private double velocityY = 0;
    private final double gravity = 800;      // pixels per second²
    private final double jumpStrength = 400; // pixels per second
    private boolean jumping = false;

    private double cactusSpeed = 200;        // pixels per second
    private boolean gameOver = false;
    private boolean useMultiCactus = false;

    private Random random = new Random();
    private AnimationTimer gameLoop;
    private long lastUpdate = 0;

    public void initialize() {
        ground.endXProperty().bind(rootPane.widthProperty());

        backToMain.setOnAction(event -> {
            try {
                Views.getMainView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        restartButton.setOnAction(event -> resetGame());

        Platform.runLater(() -> {
            rootPane.setFocusTraversable(true);
            rootPane.requestFocus();
            rootPane.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE && !jumping && !gameOver) {
                    velocityY = -jumpStrength;
                    jumping = true;
                }
                if (gameOver && event.getCode() == KeyCode.SPACE) {
                    resetGame();
                }
            });
        });

        initializeNextCactus();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double deltaTime = (now - lastUpdate) / 1_000_000_000.0; // seconds
                lastUpdate = now;

                if (!gameOver) updateGame(deltaTime);
            }
        };
        gameLoop.start();
    }

    private void updateGame(double deltaTime) {
        // Update Dino
        velocityY += gravity * deltaTime;
        dino.setY(dino.getY() + velocityY * deltaTime);

        double groundY = ground.getStartY() - dino.getHeight();
        if (dino.getY() >= groundY) {
            dino.setY(groundY);
            velocityY = 0;
            jumping = false;
        }

        // Update Cactus
        double moveAmount = cactusSpeed * deltaTime;

        if (useMultiCactus) {
            multi.setLayoutX(multi.getLayoutX() - moveAmount);
            if (multi.getLayoutX() + getMaxWidth(multi) < 0) initializeNextCactus();
            if (dino.getBoundsInParent().intersects(multi.getBoundsInParent())) triggerGameOver();
        } else {
            cactus.setX(cactus.getX() - moveAmount);
            if (cactus.getX() + cactus.getWidth() < 0) initializeNextCactus();
            if (dino.getBoundsInParent().intersects(cactus.getBoundsInParent())) triggerGameOver();
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
        velocityY = 0;
        jumping = false;

        dino.setY(ground.getStartY() - dino.getHeight());
        initializeNextCactus();
        Platform.runLater(() -> rootPane.requestFocus());
    }

    private void initializeNextCactus() {
        useMultiCactus = random.nextBoolean();

        if (useMultiCactus) {
            multi.setVisible(true);
            cactus.setVisible(false);

            for (var node : multi.getChildren()) {
                Rectangle r = (Rectangle) node;
                r.setWidth(15 + random.nextDouble() * 15);
                r.setHeight(20 + random.nextDouble() * 30);
            }

            double startX = rootPane.getWidth() + random.nextDouble() * 400;
            multi.setLayoutX(startX);
            multi.setLayoutY(ground.getStartY() - getMaxHeight(multi));
        } else {
            cactus.setVisible(true);
            multi.setVisible(false);

            double w = 15 + random.nextDouble() * 15;
            double h = 20 + random.nextDouble() * 30;
            cactus.setWidth(w);
            cactus.setHeight(h);
            cactus.setY(ground.getStartY() - h);
            cactus.setX(rootPane.getWidth());
        }
    }

    private double getMaxHeight(HBox box) {
        double max = 0;
        for (var node : box.getChildren()) {
            Rectangle r = (Rectangle) node;
            if (r.getHeight() > max) max = r.getHeight();
        }
        return max;
    }

    private double getMaxWidth(HBox box) {
        double width = 0;
        for (var node : box.getChildren()) {
            Rectangle r = (Rectangle) node;
            width += r.getWidth();
        }
        width += (box.getChildren().size() - 1) * box.getSpacing();
        return width;
    }
}
