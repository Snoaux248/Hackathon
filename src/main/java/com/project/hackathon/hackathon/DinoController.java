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

    private double velocityY = 0;
    private final double gravity = 0.5;
    private final double jumpStrength = 10;
    private boolean jumping = false;

    private double cactusSpeed = 4;
    private boolean gameOver = false;
    private boolean useMultiCactus = false;

    private AnimationTimer gameLoop;
    private Random random = new Random();

    // Fixed timestep variables
    private final double FPS = 60.0;
    private final double dt = 1.0 / FPS; // ~0.0167 seconds
    private double accumulator = 0;
    private long lastTime = System.nanoTime();

    public void initialize() {
        // Ground line bound to window size
        ground.endXProperty().bind(rootPane.widthProperty());

        // Back button
        backToMain.setOnAction(event -> {
            try { Views.getMainView(rootPane); }
            catch (Exception e) { System.out.println(e); }
        });

        // Restart button
        restartButton.setOnAction(event -> resetGame());

        // Jumping and reset with spacebar
        Platform.runLater(() -> {
            rootPane.setFocusTraversable(true);
            rootPane.requestFocus();
            rootPane.getScene().setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE && !jumping && !gameOver) {
                    velocityY = -jumpStrength;
                    jumping = true;
                }
                if (gameOver && event.getCode() == KeyCode.SPACE) resetGame();
            });
        });

        // Initialize first cactus
        initializeNextCactus();

        // Game loop
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double delta = (now - lastTime) / 1_000_000_000.0; // convert ns to seconds
                lastTime = now;
                accumulator += delta;

                while (accumulator >= dt) {
                    if (!gameOver) updateGameFixed(dt);
                    accumulator -= dt;
                }
            }
        };
        gameLoop.start();
    }

    private void updateGameFixed(double dt) {
        // Gravity
        velocityY += gravity * dt * 60; // scale to match old speed
        dino.setY(dino.getY() + velocityY * dt * 60);

        // Stop at ground
        double groundY = ground.getStartY() - dino.getHeight();
        if (dino.getY() >= groundY) {
            dino.setY(groundY);
            velocityY = 0;
            jumping = false;
        }

        // Move Cactus
        double move = cactusSpeed * dt * 60;
        if (useMultiCactus) {
            multi.setLayoutX(multi.getLayoutX() - move);

            if (multi.getLayoutX() + getMaxWidth(multi) < 0) initializeNextCactus();

            if (dino.getBoundsInParent().intersects(multi.getBoundsInParent())) triggerGameOver();
        } else {
            cactus.setX(cactus.getX() - move);

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

            multi.setLayoutX(rootPane.getWidth() + random.nextDouble() * 400);
            multi.setLayoutY(ground.getStartY() - getMaxHeight(multi));

        } else {
            cactus.setVisible(true);
            multi.setVisible(false);

            cactus.setWidth(15 + random.nextDouble() * 15);
            cactus.setHeight(20 + random.nextDouble() * 30);
            cactus.setY(ground.getStartY() - cactus.getHeight());
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
