package com.project.hackathon.hackathon;
//
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
import java.util.Set;

public class PacmanController {

    @FXML private GridPane board;
    @FXML private Circle Pacman;
    @FXML private Button backToMain;
    @FXML private AnchorPane rootPane;
    @FXML private ImageView pacmanImage;

    private final int rows = 32;
    private final int cols = 29;
    private int pacmanRow = 2;
    private int pacmanCol = 2;
    private int direction = 0;

    private int pelletCount = 0;

    private ImageView pacmanSprite;


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
        Image pacmanPNG = new Image(
                getClass().getResource("/images/PacmanCharacter.png").toExternalForm()
        );
        pacmanSprite = new ImageView(pacmanPNG);
        pacmanSprite.setPreserveRatio(true);
        pacmanSprite.setFitWidth(30); // adjust size to match your circle
        pacmanSprite.setFitHeight(30);


        rootPane.getChildren().add(pacmanSprite);
        Pacman.setVisible(false); // hide the yellow circle
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
        Pacman.setRadius(radius*2);
        Pacman.setCenterX((double)(pacmanCol * (imageWidth/cols)*.99) + boardOffsets[0] );
        Pacman.setCenterY((double)(pacmanRow * (imageHeight/rows)*.99) + boardOffsets[1] );

        rootPane.getChildren().removeIf(node -> node instanceof Circle && node != Pacman);
        buildDots();
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
//pass
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
            case 0:
                //up -2r
                if (pacmanRow - 2 >= 0 && boardArray[pacmanRow-2][pacmanCol] != 1 && boardArray[pacmanRow-2][pacmanCol-1] != 1){
                    pacmanRow--;
                }
            break;
            case 1:
                //down +1r
                if (pacmanRow + 1 <= rows && boardArray[pacmanRow+1][pacmanCol] != 1 && boardArray[pacmanRow+1][pacmanCol-1] != 1){
                    pacmanRow++;
                }
            break;
            case 2:
                //right +1c
                if(pacmanCol + 1 <= cols && boardArray[pacmanRow][pacmanCol+1] != 1 && boardArray[pacmanRow-1][pacmanCol+1] != 1){
                    pacmanCol++;
                    if(pacmanCol == 28 && pacmanRow == 15){
                        pacmanCol = 1;
                    }
                }
            break;
            case 3:
                //left -2c
                if(pacmanCol - 2 >= 0 && boardArray[pacmanRow][pacmanCol-2] != 1 && boardArray[pacmanRow-1][pacmanCol-2] != 1){
                    pacmanCol--;
                    if(pacmanCol == 1 && pacmanRow == 15){
                        pacmanCol = 28;
                    }
                }
            break;

        }
        // Move hidden circle for collisions
        Pacman.setCenterX((double)(pacmanCol * (imageWidth/cols)*.99) + boardOffsets[0] + 5);
        Pacman.setCenterY((double)(pacmanRow * (imageHeight/rows)*.99) + boardOffsets[1] - 10);
        checkCollision();
        // Move PNG sprite for visuals
        double x = (double)(pacmanCol * (imageWidth/cols)*.99) + boardOffsets[0] - 14;
        double y = (double)(pacmanRow * (imageHeight/rows)*.99) + boardOffsets[1] - 25;

        AnchorPane.setLeftAnchor(pacmanSprite, x);
        AnchorPane.setTopAnchor(pacmanSprite, y);


        switch (dir) {
            case 0: pacmanSprite.setRotate(270); break; // up
            case 1: pacmanSprite.setRotate(90);  break; // down
            case 2: pacmanSprite.setRotate(0);   break; // right
            case 3: pacmanSprite.setRotate(180); break; // left
        }
    }
    public void checkCollision(){
        Set<Node> temp = rootPane.lookupAll(".pellet");
        for(Node node : temp){
            double x1 = (double)((Circle)node).getCenterX();
            double x2 = Pacman.getCenterX();
            double y1 = (double)((Circle)node).getCenterY();
            double y2 = Pacman.getCenterY();
            if(Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2)) < 5){
                if(((Circle)node).getStyleClass().contains("bigPellet")){
                    System.out.println("BigPellet");
                }
                rootPane.getChildren().remove(node);
                pelletCount += 1;
            }
        }
        System.out.println("Pellet count: " + pelletCount);
    }
    public void buildDots(){
        for(int i = 0; i < rows-1; i++){
            for(int j = 0; j < cols-1; j++){
                if(i == 11 || i == 17){
                    if(j >= 0 && j < 4 || j >= 24 && j < cols) {
                        continue;
                    }
                }
                if(i == 14){
                    if(j >= 12 && j < 16) {
                        continue;
                    }
                }
                if(boardArray[i][j] == 0 && boardArray[i+1][j] == 0 && boardArray[i][j+1] == 0 && boardArray[i+1][j+1] == 0){
                    Circle newPellet = new Circle();
                    newPellet.setRadius(2.5);
                    if((i == 3 || i == 22) && (j == 1 || j == 26)){
                        newPellet.setRadius((double)7);
                        bigPellet();
                        newPellet.getStyleClass().add("bigPellet");
                    }
                    newPellet.getStyleClass().add("pellet");
                    newPellet.setCenterX(((double)(j+1) * (imageWidth/cols)*.99) + boardOffsets[0] +4);
                    newPellet.setCenterY(((double)(i) * (imageHeight/rows)*.99) + boardOffsets[1] +10);
                    rootPane.getChildren().add(newPellet);
                }
            }
        }
    }
    public void bigPellet(){
        //make 2nd image appear

    }
}
