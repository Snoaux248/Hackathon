package com.project.hackathon.hackathon;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;
import javafx.scene.text.Text;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class PongController {

    @FXML public AnchorPane rootPane;

    @FXML public Button backToMain;
    @FXML public Rectangle player;
    @FXML public Rectangle clanker;
    @FXML public Rectangle ball;
    @FXML public Text gameOverText;

    @FXML public Label Score;

    @FXML public CheckBox BotL;
    @FXML public CheckBox BotR;
    @FXML public Button Start;
    @FXML public GridPane StartMenu;


    private HashMap<KeyCode, Boolean> keys = new HashMap<>();
    public boolean gameOver = true;
    public int[] direction = new  int[] {0, 0};
    public int pScoreI = 0;
    public int cScoreI = 0;
    public boolean turn = false;
    long referenceTime = System.currentTimeMillis();

    public void initialize() {
        backToMain.setOnAction(event -> {
            try {
                Views.getMainView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        Platform.runLater(() -> {
            HelloApplication.primaryStage.getScene().getRoot().setFocusTraversable(true);
            HelloApplication.primaryStage.getScene().getRoot().requestFocus();

            for (KeyCode code : KeyCode.values()) {
                keys.put(code, false);
            }
            HelloApplication.primaryStage.getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
                keys.put(event.getCode(), true);
            });
            HelloApplication.primaryStage.getScene().addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED, event -> {
                keys.put(event.getCode(), false);
            });
            HelloApplication.primaryStage.getScene().setOnKeyPressed(event -> keyPressed(event));
        });

        BotL.setOnAction(event -> {
            HelloApplication.primaryStage.getScene().getRoot().setFocusTraversable(true);
            HelloApplication.primaryStage.getScene().getRoot().requestFocus();
        });
        BotR.setOnAction(event -> {
            HelloApplication.primaryStage.getScene().getRoot().setFocusTraversable(true);
            HelloApplication.primaryStage.getScene().getRoot().requestFocus();
        });
        Start.setOnAction(event -> {
            initializeGame();
        });
        ball.setY((int)HelloApplication.primaryStage.getScene().getHeight()/2);
        ball.setX((int)HelloApplication.primaryStage.getScene().getWidth()/2);
        player.setY((int)(HelloApplication.primaryStage.getScene().getHeight()-100)/2);
        clanker.setY((int)(HelloApplication.primaryStage.getScene().getHeight()-100)/2);

    }

    AnimationTimer gameLoop = new AnimationTimer() {

        @Override
        public void handle(long now) {
            if(!gameOver){
                if(System.currentTimeMillis() - referenceTime >= 10) {
                    updateGame();
                    referenceTime = System.currentTimeMillis();
                }
            }else{
                stopGame();
            }
        }
    };

    public void initializeGame(){
        Random rand = new Random(System.currentTimeMillis() % 4 +1);
        ball.setY((int)HelloApplication.primaryStage.getScene().getHeight()/2);
        ball.setX((int)HelloApplication.primaryStage.getScene().getWidth()/2);
        player.setY((int)(HelloApplication.primaryStage.getScene().getHeight()-100)/2);
        clanker.setY((int)(HelloApplication.primaryStage.getScene().getHeight()-100)/2);

        direction[0] = rand.nextInt(10) +1;
        direction[1] = rand.nextInt(10) +1;

        pScoreI = 0;
        cScoreI = 0;

        if (Start.getText().equals("Start Game")) {
            gameLoop.start();
        }
        Score.setText("0 : 0");
        gameOver = false;
        StartMenu.setVisible(false);
        StartMenu.setManaged(false);
        HelloApplication.primaryStage.getScene().getRoot().setFocusTraversable(true);
        HelloApplication.primaryStage.getScene().getRoot().requestFocus();
    }
    public void updateGame(){

        ball.setY(ball.getY() + ( direction[1]));
        ball.setX(ball.getX() + ( direction[0]));
        System.out.println(direction[0] + " " + direction[1]);

        if(ball.getY() < 0 || ball.getY() > HelloApplication.primaryStage.getHeight()-40){
            direction[1] = -direction[1];
        }
        if(ball.getX() < 0 || ball.getX() > HelloApplication.primaryStage.getWidth()-10) {
            direction[0] = -direction[0];
            stopGame();
        }
        if(ball.getBoundsInParent().intersects(player.getBoundsInParent())){
            Bounds ballBounds = ball.getBoundsInParent();
            Bounds playerBounds = player.getBoundsInParent();
            boolean hitLeft = ballBounds.getMaxX() >= playerBounds.getMinX() &&
                    ballBounds.getMinX() < playerBounds.getMinX();
            boolean hitRight = ballBounds.getMinX() <= playerBounds.getMaxX() &&
                    ballBounds.getMaxX() > playerBounds.getMaxX();
            boolean hitTop = ballBounds.getMaxY() >= playerBounds.getMinY() &&
                    ballBounds.getMinY() < playerBounds.getMinY();
            boolean hitBottom = ballBounds.getMinY() <= playerBounds.getMaxY() &&
                    ballBounds.getMaxY() > playerBounds.getMaxY();

            if(hitBottom || hitTop) {
                direction[1] = -direction[1];
            }
            if(hitLeft || hitRight){
                direction[0] = -direction[0];
            }
            if(hitLeft){
                incrementPScore();
            }
        }
        if(ball.getBoundsInParent().intersects(clanker.getBoundsInParent())){
            Bounds ballBounds = ball.getBoundsInParent();
            Bounds clankerBounds = clanker.getBoundsInParent();
            boolean hitLeft = ballBounds.getMaxX() >= clankerBounds.getMinX() &&
                    ballBounds.getMinX() < clankerBounds.getMinX();
            boolean hitRight = ballBounds.getMinX() <= clankerBounds.getMaxX() &&
                    ballBounds.getMaxX() > clankerBounds.getMaxX();
            boolean hitTop = ballBounds.getMaxY() >= clankerBounds.getMinY() &&
                    ballBounds.getMinY() < clankerBounds.getMinY();
            boolean hitBottom = ballBounds.getMinY() <= clankerBounds.getMaxY() &&
                    ballBounds.getMaxY() > clankerBounds.getMaxY();
            System.out.println(hitTop +" "+ hitRight +" "+ hitBottom +" "+ hitLeft);

            if(hitBottom || hitTop) {
                direction[1] = -direction[1];
            }
            if(hitLeft || hitRight){
                direction[0] = -direction[0];
            }
            if(hitRight){
                incrementBScore();
            }
        }
        System.out.println();
        float[] ballVector = {(float)-direction[1], (float)direction[0], (float)( -direction[1] * ball.getX()+5 + direction[0] * ball.getY()+5 )};
        float[] xLowVector = {1f, 0f, 30f};
        float[] xHighVector = {1f, 0f, (float)HelloApplication.primaryStage.getWidth()-30f};
        float[] yLowVector = {0f, 1f, 0f};
        float[] yHighVector = {0f, 1f, (float)HelloApplication.primaryStage.getHeight()-30};

        if(direction[0] > 0){
            if(BotR.isSelected()){
                float[] coordinates1 = systemSolve(ballVector[0], ballVector[1], ballVector[2],
                        xHighVector[0], xHighVector[1], xHighVector[2]);
                //System.out.println(coordinates1[0] + " " + coordinates1[1] + " " + HelloApplication.primaryStage.getHeight());
                if (Math.abs(coordinates1[1]) < HelloApplication.primaryStage.getHeight() - 30 && Math.abs(coordinates1[1]) > 0) {
                    player.setY(coordinates1[1] - 50);
                }
            }
        }else{
            if(BotL.isSelected()){
                float[] coordinates1 = systemSolve(ballVector[0], ballVector[1], ballVector[2],
                        xLowVector[0], xLowVector[1], xLowVector[2]);
                //System.out.println(coordinates1[0] + " " + coordinates1[1] + " " + HelloApplication.primaryStage.getHeight());
                if (Math.abs(coordinates1[1]) < HelloApplication.primaryStage.getHeight() - 30 && Math.abs(coordinates1[1]) > 0) {
                    clanker.setY(coordinates1[1] - 50);
                }
            }
        }
        if (isPressed(KeyCode.UP)) {
            if ((player.getY() - 15) > (-20)){
                player.setY(player.getY() -15);
            }
        } else if (isPressed(KeyCode.DOWN)) {
            if ((player.getY() + 15) < ((int)HelloApplication.primaryStage.getScene().getHeight() -80)){
                player.setY(player.getY() + 15);
            }
        } else if (isPressed(KeyCode.W)) {
            if ((clanker.getY() - 15) > (-20)){
                clanker.setY(clanker.getY() -15);
            }
        } else if (isPressed(KeyCode.S)) {
            if ((clanker.getY() + 15) < ((int)HelloApplication.primaryStage.getScene().getHeight() -80)){
                clanker.setY(clanker.getY() + 15);
            }
        }
    }
    public void incrementPScore(){
        pScoreI++;
        turn = true;
        Score.setText(cScoreI+" : "+pScoreI);
    }
    public void incrementBScore(){
        cScoreI++;
        turn = false;
        Score.setText(cScoreI+" : "+pScoreI);
    }
    public static float[] systemSolve(float a1, float b1, float c1, float a2, float b2, float c2){
        float[] temp = {0, 0};
        float d = a1*b2 - a2*b1;
        temp[0] = (c1*b2 - c2*b1)/d;
        temp[1] = (c2*a1 - c1*a2)/d;
        System.out.println(temp[0] + " " + temp[1]);
        return temp;
    }

    public void stopGame(){
        gameOver = true;
        StartMenu.setManaged(true);
        StartMenu.setVisible(true);
        gameOverText.setManaged(true);
        gameOverText.setVisible(true);
        Start.setText("Play Again");
    }

    public void keyPressed(javafx.scene.input.KeyEvent event) {
        System.out.println("Pong "+ event.getCode());
        if(event.getCode() == KeyCode.SPACE){
            if(gameOver == true){
                initializeGame();
            }
        }
    }

    public boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }
}
