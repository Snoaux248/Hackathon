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
    @FXML private AnchorPane rootPane;
    @FXML private Button backToMain;
    @FXML private Rectangle dino;
    @FXML private Rectangle cactus;
    @FXML private Line ground;
    @FXML private Text gameOverText;
    @FXML private Button restartButton;
    @FXML private HBox multi;
    @FXML private Text scoreText; // from FXML

    // Dino physics
    private double velocityY = 0;
    private final double gravity = 0.5;
    private final double jumpStrength = 10;
    private boolean jumping = false;

    // Cactus movement
    private double cactusSpeed = 4;
    private boolean gameOver = false;
    private boolean useMultiCactus = false;

    // Game loop and timing
    private AnimationTimer gameLoop;
    private Random random = new Random();
    private final double FPS = 60.0;
    private final double dt = 1.0 / FPS;
    private double accumulator = 0;
    private long lastTime = System.nanoTime();

    // Score (time-based)
    private double score = 0;
    private final double SCORE_RATE = 60; // points per second

    public void initialize() {
        // Ground line bound to window width
        ground.endXProperty().bind(rootPane.widthProperty());

        // Back button
        backToMain.setOnAction(event -> {
            try { Views.getMainView(rootPane); }
            catch (Exception e) { System.out.println(e); }
        });

        // Restart button
        restartButton.setOnAction(event -> resetGame());

        // Key input for jumping and reset
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

        // Game loop with fixed timestep
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double delta = (now - lastTime) / 1_000_000_000.0;
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
        // Update timer-based score
        if (!gameOver) {
            score += SCORE_RATE * dt;
            scoreText.setText("Score: " + (int) score);
        }

        // Apply gravity to dino
        velocityY += gravity * dt * 60;
        dino.setY(dino.getY() + velocityY * dt * 60);

        // Stop dino at ground
        double groundY = ground.getStartY() - dino.getHeight();
        if (dino.getY() >= groundY) {
            dino.setY(groundY);
            velocityY = 0;
            jumping = false;
        }

        // Move cactus / multi-cactus
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

        score = 0;
        scoreText.setText("Score: 0");

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
                r.setWidth(15 + random.nextDouble());
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
