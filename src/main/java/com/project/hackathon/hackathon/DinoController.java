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
    //
    @FXML public AnchorPane rootPane;
    @FXML public Button backToMain;
    @FXML public Rectangle dino;
    @FXML public Rectangle cactus;
    @FXML public Line ground;
    @FXML public Text gameOverText;
    @FXML public Button restartButton;
    @FXML public HBox multi;

    // Knowledge
    private double velocityY = 0;
    private final double gravity = 0.5;
    private final double jumpStrength = 10;
    private boolean jumping = false;

    private double cactusSpeed = 4;
    private AnimationTimer gameLoop;
    private boolean gameOver = false;
    private Random random = new Random();

    private boolean useMultiCactus = false;

    public void initialize() {
        // Ground line bound to window size
        ground.endXProperty().bind(rootPane.widthProperty());

        // Back button
        backToMain.setOnAction(event -> {
            try {
                Views.getMainView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        // Restart button
        restartButton.setOnAction(event -> resetGame());

        // Jumping and reset with spacebar
        Platform.runLater(() -> {
            rootPane.setFocusTraversable(true);
            rootPane.requestFocus();
            rootPane.getScene().setOnKeyPressed(event -> {
                // Jump
                if (event.getCode() == KeyCode.SPACE && !jumping && !gameOver) {
                    velocityY = -jumpStrength;
                    jumping = true;
                }
                // Reset after GAME OVER
                if (gameOver && event.getCode() == KeyCode.SPACE) {
                    resetGame();
                }
            });
        });

        // Initialize first cactus
        initializeNextCactus();

        // Game loop
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver) updateGame();
            }
        };
        gameLoop.start();
    }

    private void updateGame() {
        // Gravity
        velocityY += gravity;
        dino.setY(dino.getY() + velocityY);

        // Stop at ground
        double groundY = ground.getStartY() - dino.getHeight();
        if (dino.getY() >= groundY) {
            dino.setY(groundY);
            velocityY = 0;
            jumping = false;
        }

        // Move Cactus
        if (useMultiCactus) {
            multi.setLayoutX(multi.getLayoutX() - cactusSpeed);

            // Reset multi cactus if offscreen
            if (multi.getLayoutX() + getMaxWidth(multi) < 0) {
                initializeNextCactus();
            }

            // Collision detection with each rectangle in multi
                if (dino.getBoundsInParent().intersects(multi.getBoundsInParent())) {
                    triggerGameOver();

            }
        } else {
            cactus.setX(cactus.getX() - cactusSpeed);

            // Reset cactus if offscreen
            if (cactus.getX() + cactus.getWidth() < 0) {
                initializeNextCactus();
            }

            // Collision detection
            if (dino.getBoundsInParent().intersects(cactus.getBoundsInParent())) {
                triggerGameOver();
            }
        }
    }

    // Game Over Sign
    private void triggerGameOver() {
        gameOverText.setVisible(true);
        restartButton.setVisible(true);
        gameOver = true;
        velocityY = 0;
    }

    // Game Reset
    private void resetGame() {
        gameOverText.setVisible(false);
        restartButton.setVisible(false);
        gameOver = false;
        velocityY = 0;
        jumping = false;

        // Reset dino position
        dino.setY(ground.getStartY() - dino.getHeight());

        // Reset cactus/multi cactus
        initializeNextCactus();

        Platform.runLater(() -> rootPane.requestFocus());
    }

    // Initialize cactus or multi cactus randomly
    private void initializeNextCactus() {
        useMultiCactus = random.nextBoolean(); // 50% chance

        if (useMultiCactus) {
            // Multi cactus setup
            multi.setVisible(true);
            cactus.setVisible(false);

            // Randomize rectangle sizes inside multi
            for (var node : multi.getChildren()) {
                Rectangle r = (Rectangle) node;
                double w = 15 + random.nextDouble() * 15;
                double h = 20 + random.nextDouble() * 30;
                r.setWidth(w);
                r.setHeight(h);
            }

            // Position HBox offscreen and bottom aligned
            double startX = rootPane.getWidth() + random.nextDouble() * 400;
            multi.setLayoutX(startX);
            multi.setLayoutY(ground.getStartY() - getMaxHeight(multi));

        } else {
            // Single cactus setup
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

    // max height of rectangles in HBox
    private double getMaxHeight(HBox box) {
        double max = 0;
        for (var node : box.getChildren()) {
            Rectangle r = (Rectangle) node;
            if (r.getHeight() > max) max = r.getHeight();
        }
        return max;
    }

    //total width of rectangles in Hbox
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
