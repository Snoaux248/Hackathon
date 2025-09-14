    package com.project.hackathon.hackathon;

    import javafx.animation.AnimationTimer;
    import javafx.application.Platform;
    import javafx.fxml.FXML;
    import javafx.scene.control.Button;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyCode;
    import javafx.scene.layout.AnchorPane;
    import javafx.scene.layout.GridPane;
    import javafx.scene.layout.Pane;
    import javafx.scene.shape.Circle;
    import java.util.Date;


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
        private int scale;
        private int direction = 0;

        boolean gameOver = false;
        int imageWidth;
        int imageHeight;
        private int[] boardOffsets = new int[2];
        long referenceTime = System.currentTimeMillis();

        // 1 = wall, 0 = free space
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
            // Calculate scale
            scale = 20; // You can adjust or calculate from stage size if you want

            Pacman.setRadius(scale/2);
            GridPane.setColumnSpan(Pacman, 2);
            GridPane.setRowSpan(Pacman, 2);
            if(HelloApplication.primaryStage.getWidth()/HelloApplication.primaryStage.getHeight() < 9735/10752){
                imageWidth = (int)HelloApplication.primaryStage.getWidth() -250;
                imageHeight = imageWidth * 9735/10752;
            }else{
                imageHeight = (int)HelloApplication.primaryStage.getHeight()-100;
                imageWidth = imageHeight * 9735/10752;
            }
            boardOffsets[0] = (int)((HelloApplication.primaryStage.getWidth() - imageWidth) / 2);
            boardOffsets[1] = (int)((HelloApplication.primaryStage.getHeight() - imageHeight) / 2);
            AnchorPane.setLeftAnchor(Pacman, (double)boardOffsets[0]);
            AnchorPane.setTopAnchor(Pacman, (double)boardOffsets[1]);


            // Draw walls
            pacmanImage.setImage(new Image(getClass().getResource("/images/Pacman.png").toExternalForm()));
            pacmanImage.setFitWidth(HelloApplication.primaryStage.getWidth()-100);
            pacmanImage.setFitHeight(HelloApplication.primaryStage.getHeight()-100);

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (boardArray[i][j] == 1) {
                        Pane wall = new Pane();
                        wall.setPrefSize(scale, scale);
                        wall.setStyle("-fx-background-color: transparent;");
                        board.getChildren().add(wall);
                        GridPane.setRowIndex(wall, i);
                        GridPane.setColumnIndex(wall, j);
                    }
                }
            }

            Platform.runLater(() -> {
                HelloApplication.primaryStage.getScene().getRoot().setFocusTraversable(true);
                HelloApplication.primaryStage.getScene().getRoot().requestFocus();
                HelloApplication.primaryStage.getScene().setOnKeyPressed(event -> keyPressed(event));
            });
            // Wrap Pac-Man in a StackPane


            backToMain.setOnAction(e -> {
                try { Views.getMainView(rootPane); }
                catch (Exception ex) { ex.printStackTrace(); }
            });

            AnimationTimer gameLoop = new AnimationTimer(){
                @Override
                public void handle(long now) {

                    if(!gameOver){
                        if(System.currentTimeMillis() - referenceTime >= 100){
                            updateGame();
                            referenceTime = System.currentTimeMillis();
                        }
                    }else{
                        stopGame();
                    }
                }
            };
            gameLoop.start();
        }

        public void keyPressed(javafx.scene.input.KeyEvent event) {
            System.out.println("Pong "+ event.getCode());
            if (event.getCode() == KeyCode.UP) {
                direction = 0;
            } else if (event.getCode() == KeyCode.DOWN) {
                direction = 1;
            } else if (event.getCode() == KeyCode.RIGHT) {
                direction = 2;
            } else if (event.getCode() == KeyCode.LEFT) {
                direction = 3;
            }
        }

        public void updateGame(){
            System.out.println(direction);
            switch(direction){
                case 0:
                    if(pacmanRow - 1 > 0){
                        if(boardArray[pacmanRow-1][pacmanCol] != 1){
                            System.out.println("updateGame UP");
                            move_packman(0);
                        }
                    }
                    break;
                case 1:
                    if((pacmanRow + 1 ) < rows){
                        if(boardArray[pacmanRow+1][pacmanCol] != 1){
                            System.out.println("updateGame Down");
                            move_packman(1);
                        }
                    }
                    break;
                case 2:
                    if(pacmanCol + 1 < cols) {
                        if(boardArray[pacmanRow][pacmanCol+1] != 1){
                            System.out.println("updateGame Right");
                            move_packman(2);
                        }
                    }
                    break;
                case 3:
                    if(pacmanCol - 1 > 0){
                        if(boardArray[pacmanRow][pacmanCol-1] != 1){
                            System.out.println("updateGame Left");
                            move_packman(3);
                        }
                    }
                    break;
            }
        }

        public void stopGame(){

        }

        public void move_packman(int direction){
            switch (direction) {
                case 0:
                    boardArray[pacmanRow - 1][pacmanCol] = 1;
                    boardArray[pacmanRow][pacmanCol] = 0;
                    pacmanRow--;
                    AnchorPane.setTopAnchor(Pacman, (double)(boardOffsets[1] + imageHeight/rows *pacmanRow) -10);
                    break;
                case 1:
                    boardArray[pacmanRow + 1][pacmanCol] = 1;
                    boardArray[pacmanRow][pacmanCol] = 0;
                    pacmanRow++;
                    AnchorPane.setTopAnchor(Pacman, (double)(boardOffsets[1] + imageHeight/rows *pacmanRow)-10);
                    break;
                case 2:
                    boardArray[pacmanRow][pacmanCol + 1] = 1;
                    boardArray[pacmanRow][pacmanCol] = 0;
                    pacmanCol++;
                    AnchorPane.setLeftAnchor(Pacman, (double)(boardOffsets[0] + imageWidth/cols *pacmanCol));
                    break;
                case 3:
                    boardArray[pacmanRow][pacmanCol - 1] = 1;
                    boardArray[pacmanRow][pacmanCol] = 0;
                    pacmanCol--;
                    AnchorPane.setLeftAnchor(Pacman, (double)(boardOffsets[0] + imageWidth/cols *pacmanCol));
                    break;
            }
        }
    }
