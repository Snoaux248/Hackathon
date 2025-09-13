package com.project.hackathon.hackathon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PacmanController {

    @FXML private Circle Pacman;
    @FXML private Pane gamePane;
    @FXML private AnchorPane rootPane;
    @FXML private Button backToMain;
    @FXML private GridPane board;

    private double speed = 100; // pixels per second

    private double dx = 0, dy = 0;         // current direction
    private double nextDx = 0, nextDy = 0; // desired direction

    private List<Rectangle> walls = new ArrayList<>();

    private final int rows = 32;
    private final int cols = 29;
    private int[][] wallArray = new int[rows][cols];

    private int[][] boardArray =
    {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,1,1,1,0,0,1,1,1,1,0,0,1,0,0,1,1,1,1,0,0,1,1,1,0,0,1},
            {1,0,0,1,1,1,0,0,1,1,1,1,0,0,1,0,0,1,1,1,1,0,0,1,1,1,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,1,1,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,1,0,0,1,1,1,0,0,1},
            {1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,0,0,1,1,1,1,0,0,1,0,0,1,1,1,1,0,0,1,1,1,1,1,1},
            {0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0},
            {1,1,1,1,1,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,1,0,0,1,1,1,1,1,1},
            {0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
            {1,1,1,1,1,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,1,0,0,1,1,1,1,1,1},
            {0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0},
            {0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0},
            {1,1,1,1,1,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,1,0,0,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,1,1,1,0,0,1,1,1,1,0,0,1,0,0,1,1,1,1,0,0,1,1,1,0,0,1},
            {1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
            {1,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1},
            {1,1,1,0,0,1,0,0,1,0,0,1,1,1,1,1,1,1,0,0,1,0,0,1,0,0,1,1,1},
            {1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,1},
            {1,0,0,1,1,1,1,1,1,1,1,1,0,0,1,0,0,1,1,1,1,1,1,1,1,1,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    };

    private int scale = 0;
    public void initialize() {
        backToMain.setOnAction(event -> {
            try {
                Views.getMainView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        if(HelloApplication.primaryStage.getWidth() < HelloApplication.primaryStage.getHeight()){
            scale = (int)((HelloApplication.primaryStage.getWidth()-100.0)/cols);
        }else{
            scale = (int)((HelloApplication.primaryStage.getHeight()-100.0)/rows);
        }
        Random rand = new Random();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
               System.out.println(boardArray[i][j]);
               if(boardArray[i][j] == 1){
                   Rectangle r =  new Rectangle();
                   r.setWidth(scale);
                   r.setHeight(scale);
                   r.setFill(Color.BLACK);

                   board.setRowIndex(r, i);
                   board.setColumnIndex(r, j);
                   board.getChildren().add(r);
                   int[] temp = {0, 0, 0, 0};
                   if(i > 0 && i < rows-1 && j > 0 && j < cols-1){
                       temp[0] = boardArray[i][j-1] == 1 ? 1: 0;
                       temp[1] = boardArray[i+1][j] == 1 ? 1: 0;
                       temp[2] = boardArray[i][j+1] == 1 ? 1: 0;
                       temp[3] = boardArray[i-1][j] == 1 ? 1: 0;
                   }
               }
            }
        }
    }


}


/* // ===== Input handling =====
    private void setupControls() {
        gamePane.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            switch (code) {
                case UP -> { nextDx = 0; nextDy = -1; }
                case DOWN -> { nextDx = 0; nextDy = 1; }
                case LEFT -> { nextDx = -1; nextDy = 0; }
                case RIGHT -> { nextDx = 1; nextDy = 0; }
            }
        });
        gamePane.setFocusTraversable(true);
    }

    // ===== Game loop =====
    private void startGameLoop() {
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

        // Check if next desired direction is clear
        double attemptX = Pacman.getLayoutX() + nextDx * speed * deltaSeconds;
        double attemptY = Pacman.getLayoutY() + nextDy * speed * deltaSeconds;
        Circle tempNext = new Circle(attemptX, attemptY, Pacman.getRadius() - buffer);

        boolean canMoveNext = true;
        for (Rectangle wall : walls) {
            if (tempNext.getBoundsInParent().intersects(wall.getBoundsInParent())) {
                canMoveNext = false;
                break;
            }
        }

        if (canMoveNext) {
            dx = nextDx;
            dy = nextDy;
        }

        // Move in current direction
        double newX = Pacman.getLayoutX() + dx * speed * deltaSeconds;
        double newY = Pacman.getLayoutY() + dy * speed * deltaSeconds;
        Circle temp = new Circle(newX, newY, Pacman.getRadius() - buffer);

        // Collision check
        for (Rectangle wall : walls) {
            if (temp.getBoundsInParent().intersects(wall.getBoundsInParent())) {
                dx = 0;
                dy = 0;
                return;
            }
        }

        // Keep inside gamePane
        if (newX < Pacman.getRadius()) newX = Pacman.getRadius();
        if (newX > gamePane.getWidth() - Pacman.getRadius()) newX = gamePane.getWidth() - Pacman.getRadius();
        if (newY < Pacman.getRadius()) newY = Pacman.getRadius();
        if (newY > gamePane.getHeight() - Pacman.getRadius()) newY = gamePane.getHeight() - Pacman.getRadius();

        Pacman.setLayoutX(newX);
        Pacman.setLayoutY(newY);
    }

    // ===== Corridor-style maze =====
    private void generateCorridorMaze() {
        // Step 1: initialize all empty
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                wallArray[i][j] = 0;

        // Step 2: add random walls at intersections to form junctions
        for (int i = 1; i < rows - 1; i += 2) {
            for (int j = 1; j < cols - 1; j += 2) {
                wallArray[i][j] = ThreadLocalRandom.current().nextInt(0, 2); // 0 = empty, 1 = wall
            }
        }

        // Step 3: ensure Pac-Man start is empty
        wallArray[0][0] = 0;
        wallArray[1][0] = 0;
        wallArray[0][1] = 0;
        wallArray[1][1] = 0;
    }

    // ===== Render walls =====
    private void generateWallsFromArray() {
        double cellWidth = gamePane.getPrefWidth() / cols;
        double cellHeight = gamePane.getPrefHeight() / rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (wallArray[i][j] == 1) {
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    Rectangle wall = new Rectangle(x, y, cellWidth, cellHeight);
                    wall.setFill(Color.BLUE);
                    walls.add(wall);
                    gamePane.getChildren().add(wall);
                }
            }
        }
    }*/