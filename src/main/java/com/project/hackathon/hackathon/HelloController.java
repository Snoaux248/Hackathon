package com.project.hackathon.hackathon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelloController {

    @FXML private Label welcomeText;
    @FXML public AnchorPane rootPane;

    @FXML private Button Pong;
    @FXML private Button Tetris;
    @FXML private Button Pacman;
    @FXML private Button Dino;

    @FXML
    public void initialize() {
        Image bgImage = new Image(
                String.valueOf(getClass().getResource("/images/gridstillbackground.png")),
                true // background loading
        );

        ImageView bgView = new ImageView(bgImage);
        bgView.setPreserveRatio(false);
        bgView.setSmooth(true);
        bgView.setMouseTransparent(true);

        bgView.fitWidthProperty().bind(rootPane.widthProperty());
        bgView.fitHeightProperty().bind(rootPane.heightProperty());

        rootPane.getChildren().add(0, bgView);

        Pong.setOnAction(event -> {
            try {
                Views.getPongView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        Tetris.setOnAction(event -> {
            try {
                Views.getTetrisView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        Dino.setOnAction(event -> {
            try {
                Views.getDinoView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
        Pacman.setOnAction(event -> {
            try {
                Views.getPacmanView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }
}