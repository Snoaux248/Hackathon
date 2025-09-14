package com.project.hackathon.hackathon;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.LinkedList;
import java.util.Queue;

public class PacmanController {

    @FXML private GridPane board;
    @FXML private Circle Pacman;
    @FXML private Button backToMain;
    @FXML private AnchorPane rootPane;
    @FXML private ImageView pacmanImage;

    private final int rows = 32;
    private final int cols = 29;
    private int pacmanRow = 1;
    private int pacmanCol = 1;
    private int direction = 0;

    private double pacmanOffsetY = -6; // shift Pacman slightly up
    private double pacmanOffsetX = -4; // shift slightly left

    boolean gameOver = false;
    int imageWidth;
    int imageHeight;
    private double[] boardOffsets = new double[2];
    long referenceTime = System.currentTimeMillis();

    private int[][] boardArray = {
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
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    public void initialize() {
        pacmanImage.setImage(new Image(getClass().getResource("/images/Pacman.png").toExternalForm()));

        drawWalls();
        resizeBoard();

        HelloApplication.primaryStage.widthProperty().addListener(obs -> resizeBoard());
        HelloApplication.primaryStage.heightProperty().addListener(obs -> resizeBoard());

        Platform.runLater(() -> {
            HelloApplication.primaryStage.getScene().getRoot().setFocusTraversable(true);
            HelloApplication.primaryStage.getScene().getRoot().requestFocus();
            HelloApplication.primaryStage.getScene().setOnKeyPressed(event -> keyPressed(event));
        });

        backToMain.setOnAction(e -> {
            try { Views.getMainView(rootPane); }
            catch (Exception ex) { ex.printStackTrace(); }
        });

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver && System.currentTimeMillis() - referenceTime >= 100) {
                    updateGame();
                    referenceTime = System.currentTimeMillis();
                }
            }
        };
        gameLoop.start();
    }

    private void drawWalls() {
        board.getChildren().clear();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (boardArray[i][j] == 1) {
                    Pane wall = new Pane();
                    wall.setStyle("-fx-background-color: transparent;");
                    board.getChildren().add(wall);
                    GridPane.setRowIndex(wall, i);
                    GridPane.setColumnIndex(wall, j);
                }
            }
        }
    }

    private void resizeBoard() {
        if(HelloApplication.primaryStage.getWidth() / HelloApplication.primaryStage.getHeight() < 9735/10752.0){
            imageWidth = (int)HelloApplication.primaryStage.getWidth() - 250;
            imageHeight = (int)(imageWidth * 9735/10752.0);
        } else {
            imageHeight = (int)HelloApplication.primaryStage.getHeight() - 100;
            imageWidth = (int)(imageHeight * 9735/10752.0);
        }

        boardOffsets[0] = (HelloApplication.primaryStage.getWidth() - imageWidth) / 2;
        boardOffsets[1] = (HelloApplication.primaryStage.getHeight() - imageHeight) / 2;

        double scale = Math.min(imageWidth / (double) cols, imageHeight / (double) rows);

        pacmanImage.setFitWidth(imageWidth);
        pacmanImage.setFitHeight(imageHeight);
        AnchorPane.setLeftAnchor(pacmanImage, boardOffsets[0]);
        AnchorPane.setTopAnchor(pacmanImage, boardOffsets[1]);

        double radius = scale / 2.2;
        Pacman.setRadius(radius);
        AnchorPane.setLeftAnchor(Pacman, boardOffsets[0] + pacmanCol*scale + scale/2 - radius + pacmanOffsetX);
        AnchorPane.setTopAnchor(Pacman, boardOffsets[1] + pacmanRow*scale + scale/2 - radius + pacmanOffsetY);

        rootPane.getChildren().removeIf(node -> node instanceof Circle && node != Pacman);
        spawnPellets(scale);
    }

    private void spawnPellets(double scale) {
        boolean[][] reachable = new boolean[rows][cols];
        floodFill(pacmanRow, pacmanCol, reachable);
        double pelletRadius = scale / 6;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (boardArray[i][j] == 0 && reachable[i][j]) {
                    double x = boardOffsets[0] + j * scale * 0.98 - 1;
                    double y = boardOffsets[1] + i * scale * 0.96 - 1;

                    boolean wallAbove = i > 0 && boardArray[i-1][j] == 1;
                    boolean wallBelow = i < rows-1 && boardArray[i+1][j] == 1;
                    boolean wallLeft  = j > 0 && boardArray[i][j-1] == 1;
                    boolean wallRight = j < cols-1 && boardArray[i][j+1] == 1;

                    // Horizontal corridor
                    if ((wallLeft || wallRight) && !(wallAbove || wallBelow)) {
                        addPellet(x + scale/2, y + scale*0.23, pelletRadius); // slightly up
                        addPellet(x + scale/2, y + scale*0.73, pelletRadius);
                    }
                    // Vertical corridor
                    else if ((wallAbove || wallBelow) && !(wallLeft || wallRight)) {
                        addPellet(x + scale*0.23, y + scale/2, pelletRadius);
                        addPellet(x + scale*0.73, y + scale/2, pelletRadius);
                    }
                    // Corner or open space
                    else {
                        addPellet(x + scale/2, y + scale/2, pelletRadius);
                    }
                }
            }
        }
    }

    private void addPellet(double x, double y, double r) {
        Circle pellet = new Circle(r);
        pellet.getStyleClass().add("pellet");
        pellet.setCenterX(x);
        pellet.setCenterY(y);
        pellet.setStyle("-fx-fill:white;");
        rootPane.getChildren().add(pellet);
    }

    private void floodFill(int r, int c, boolean[][] reachable) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{r,c});
        while (!queue.isEmpty()) {
            int[] p = queue.poll();
            int row = p[0], col = p[1];
            if (row < 0 || row >= rows || col < 0 || col >= cols) continue;
            if (reachable[row][col] || boardArray[row][col] == 1) continue;
            reachable[row][col] = true;
            queue.add(new int[]{row+1,col});
            queue.add(new int[]{row-1,col});
            queue.add(new int[]{row,col+1});
            queue.add(new int[]{row,col-1});
        }
    }

    public void keyPressed(javafx.scene.input.KeyEvent event) {
        if (event.getCode() == KeyCode.UP) direction = 0;
        else if (event.getCode() == KeyCode.DOWN) direction = 1;
        else if (event.getCode() == KeyCode.RIGHT) direction = 2;
        else if (event.getCode() == KeyCode.LEFT) direction = 3;
    }

    public void updateGame() {
        switch(direction){
            case 0: if(pacmanRow-1>=0 && boardArray[pacmanRow-1][pacmanCol]!=1) movePackman(0); break;
            case 1: if(pacmanRow+1<rows && boardArray[pacmanRow+1][pacmanCol]!=1) movePackman(1); break;
            case 2: if(pacmanCol+1<cols && boardArray[pacmanRow][pacmanCol+1]!=1) movePackman(2); break;
            case 3: if(pacmanCol-1>=0 && boardArray[pacmanRow][pacmanCol-1]!=1) movePackman(3); break;
        }
    }

    private void movePackman(int dir) {
        double scale = Math.min(imageWidth / (double) cols, imageHeight / (double) rows);
        switch(dir){
            case 0: if (pacmanRow - 1 >= 0 && boardArray[pacmanRow-1][pacmanCol] != 1) pacmanRow--; break;
            case 1: if (pacmanRow + 1 < rows && boardArray[pacmanRow+1][pacmanCol] != 1) pacmanRow++; break;
            case 2: if (pacmanCol + 1 < cols && boardArray[pacmanRow][pacmanCol+1] != 1) {
                pacmanCol++;
            } else if (pacmanCol + 1 >= cols) {
                pacmanCol = 0; // wrap to left
            } pacmanCol++; break;
            case 3: if (pacmanCol - 1 >= 0 && boardArray[pacmanRow][pacmanCol-1] != 1) {
                pacmanCol--;
            } else if (pacmanCol - 1 < 0) {
                pacmanCol = cols - 1; // wrap to right
            }
                pacmanCol--; break;
        }
        double radius = Pacman.getRadius();
        AnchorPane.setLeftAnchor(Pacman, boardOffsets[0] + pacmanCol*scale + scale/2 - radius + pacmanOffsetX);
        AnchorPane.setTopAnchor(Pacman, boardOffsets[1] + pacmanRow*scale + scale/2 - radius + pacmanOffsetY - 5);
    }
}
