package com.project.hackathon.hackathon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javafx.scene.shape.Circle;

import javafx.animation.AnimationTimer;

public class PacmanController {

    @FXML private Circle Pacman;
    @FXML private Pane gamePane;
    @FXML private AnchorPane rootPane;
    @FXML private Button backToMain;

    private double speed = 100; // pixels per second

    private double dx = 0, dy = 0;         // current direction

    public void initialize() {
        backToMain.setOnAction(event -> {
            try {
                Views.getMainView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        // Delay setup until scene is ready
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                setupGlobalControls(newScene);
            }
        });

        startGameLoop();
    }

    private void setupGlobalControls(javafx.scene.Scene scene) {
        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP -> { dx = 0; dy = -1; }
                case DOWN -> { dx = 0; dy = 1; }
                case LEFT -> { dx = -1; dy = 0; }
                case RIGHT -> { dx = 1; dy = 0; }
            }
        });
    }

    // ===== Game loop =====
    private void startGameLoop() {
        gamePane.requestFocus();
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double deltaSeconds = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                movePacman(deltaSeconds);
            }
        };
        timer.start();
    }

    // ===== Movement with responsive turning =====
    private void movePacman(double deltaSeconds) {
        double buffer = 0.1; // allows turning slightly before full alignment

        // Move in current direction
        gamePane.setFocusTraversable(true);
        double newX = Pacman.getLayoutX() + dx * speed * deltaSeconds;
        double newY = Pacman.getLayoutY() + dy * speed * deltaSeconds;
        Circle temp = new Circle(newX, newY, Pacman.getRadius() - buffer);


        // Keep inside gamePane
        if (newX < Pacman.getRadius()) newX = Pacman.getRadius();
        if (newX > gamePane.getWidth() - Pacman.getRadius()) newX = gamePane.getWidth() - Pacman.getRadius();
        if (newY < Pacman.getRadius()) newY = Pacman.getRadius();
        if (newY > gamePane.getHeight() - Pacman.getRadius()) newY = gamePane.getHeight() - Pacman.getRadius();

        Pacman.setLayoutX(newX);
        Pacman.setLayoutY(newY);
    }
}
