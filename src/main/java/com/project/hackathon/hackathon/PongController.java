package com.project.hackathon.hackathon;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class PongController {

    @FXML public AnchorPane rootPane;

    @FXML public Button backToMain;
    @FXML public Rectangle player;
    @FXML public Rectangle clanker;
    @FXML public Rectangle ball;

    public boolean hasStarted = false;
    public boolean gameOver = false;
    public int[] direction = new  int[] {0, 0};
    public float velocity = 0;
    public int score = 0;
    long referenceTime = System.currentTimeMillis();

    public void initialize(){
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
            HelloApplication.primaryStage.getScene().setOnKeyPressed(event -> keyPressed(event));
        });

        ball.setY((int)HelloApplication.primaryStage.getScene().getHeight()/2);
        ball.setX((int)HelloApplication.primaryStage.getScene().getWidth()/2);

        player.setY((int)(HelloApplication.primaryStage.getScene().getHeight()-100)/2);
        clanker.setY((int)(HelloApplication.primaryStage.getScene().getHeight()-100)/2);

        AnimationTimer gameLoop = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if(!hasStarted){
                    initializeGame();
                    hasStarted = true;
                }
                if(!gameOver){
                    if(System.currentTimeMillis() - referenceTime >= 10) {
                        updateGame();
                        referenceTime = System.currentTimeMillis();
                    }
                }else{
                    stopGame();
                    hasStarted = false;
                }
            }
        };
        gameLoop.start();
    }

    public void initializeGame(){
        Random rand = new Random(4);
        direction[0] = rand.nextInt(10) +1;
        direction[1] = rand.nextInt(10) +1;

        velocity = (float)Math.sqrt((direction[0] * direction[0] +  direction[1] * direction[1]));
    }
    public void updateGame(){
        //System.out.println(ball.getX());

        ball.setY(ball.getY() + (direction[1]));
        ball.setX(ball.getX() + (direction[0]));

        if(ball.getY() < 0) {
            direction[1] = -direction[1];
        }
        if(ball.getX() < 0) {
            direction[0] = -direction[0];
            stopGame();
        }
        if(ball.getY() > HelloApplication.primaryStage.getHeight()-40) {
            direction[1] = -direction[1];
        }
        if(ball.getX() > HelloApplication.primaryStage.getWidth()-10) {
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
            System.out.println(hitTop +" "+ hitRight +" "+ hitBottom +" "+ hitLeft);

            if(hitBottom || hitTop) {
                direction[1] = -direction[1];
            }
            if(hitLeft || hitRight){
                direction[0] = -direction[0];
            }
            if(hitLeft){
                incrementScore();
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
                incrementScore();
            }
        }
    }
    public void stopGame(){
        gameOver = true;
    }
    public void incrementScore(){
        score++;
        ((Label)(rootPane.lookup("GridPane")).lookup("Label")).setText("Score: "+ score);
    }



    public void keyPressed(javafx.scene.input.KeyEvent event) {
        System.out.println("Pong "+ event.getCode());
        if (event.getCode() == KeyCode.UP) {
            if ((player.getY() - 20) > (-20)){
                player.setY(player.getY() -20);
            }
        } else if (event.getCode() == KeyCode.DOWN) {
            if ((player.getY() + 20) < ((int)HelloApplication.primaryStage.getScene().getHeight() -80)){
                System.out.println(""+ (int)HelloApplication.primaryStage.getScene().getHeight()+ " "+ player.getY());
                player.setY(player.getY() + 20);
            }
        } else if (event.getCode() == KeyCode.W) {
            if ((clanker.getY() - 20) > (-20)){
                clanker.setY(clanker.getY() -20);
            }
        } else if (event.getCode() == KeyCode.S) {
            if ((clanker.getY() + 20) < ((int)HelloApplication.primaryStage.getScene().getHeight() -80)){
                System.out.println(""+ (int)HelloApplication.primaryStage.getScene().getHeight()+ " "+ clanker.getY());
                clanker.setY(clanker.getY() + 20);
            }
        }
    }

    // Start the loop
}
