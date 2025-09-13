package com.project.hackathon.hackathon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;

import java.util.Objects;
import java.util.stream.Stream;

public class HelloController {

    @FXML private AnchorPane rootPane;     // fx:id="rootPane"
    @FXML private StackPane centerLayer;   // fx:id="centerLayer"
    @FXML private GridPane buttonGrid;     // fx:id="buttonGrid"

    @FXML private Label welcomeText;

    @FXML private Button Pong;
    @FXML private Button Tetris;
    @FXML private Button Pacman;
    @FXML private Button Dino;

    @FXML
    public void initialize() {
        // --- Background image ---
        String bgUrl = Objects.requireNonNull(
                getClass().getResource("/images/gridstillbackground.png"),
                "Missing: /images/gridstillbackground.png"
        ).toExternalForm();

        ImageView bgView = new ImageView(new Image(bgUrl, true));
        bgView.setPreserveRatio(false);
        bgView.setSmooth(true);
        bgView.setMouseTransparent(true);
        bgView.fitWidthProperty().bind(centerLayer.widthProperty());
        bgView.fitHeightProperty().bind(centerLayer.heightProperty());
        centerLayer.getChildren().add(0, bgView);

        // --- Make buttons fill cells ---
        Stream.of(Pong, Tetris, Pacman, Dino).forEach(b -> {
            b.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            GridPane.setHgrow(b, Priority.ALWAYS);
            GridPane.setVgrow(b, Priority.ALWAYS);
            b.setWrapText(true);
            b.setTextAlignment(TextAlignment.CENTER);
            b.setGraphicTextGap(8);
            b.setContentDisplay(ContentDisplay.TOP); // image above text
        });

        // --- Pong icon ---
        ImageView pongIV = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResource("/images/ponglabeltemp.jpg")).toExternalForm(),
                true
        ));
        pongIV.setPreserveRatio(true);
        pongIV.setSmooth(true);
        pongIV.fitWidthProperty().bind(Pong.widthProperty().multiply(0.7));
        pongIV.fitHeightProperty().bind(Pong.heightProperty().multiply(0.7));
        Pong.setGraphic(pongIV);

        // --- Tetris icon ---
        ImageView tetrisIV = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResource("/images/tetrislabeltemp.jpg")).toExternalForm(),
                true
        ));
        tetrisIV.setPreserveRatio(true);
        tetrisIV.setSmooth(true);
        tetrisIV.fitWidthProperty().bind(Tetris.widthProperty().multiply(0.7));
        tetrisIV.fitHeightProperty().bind(Tetris.heightProperty().multiply(0.7));
        Tetris.setGraphic(tetrisIV);

        // --- Pacman icon ---
        ImageView pacmanIV = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResource("/images/pacmanlabeltemp.jpg")).toExternalForm(),
                true
        ));
        pacmanIV.setPreserveRatio(true);
        pacmanIV.setSmooth(true);
        pacmanIV.fitWidthProperty().bind(Pacman.widthProperty().multiply(0.7));
        pacmanIV.fitHeightProperty().bind(Pacman.heightProperty().multiply(0.7));
        Pacman.setGraphic(pacmanIV);

        // --- Dino icon ---
        ImageView dinoIV = new ImageView(new Image(
                Objects.requireNonNull(getClass().getResource("/images/dinolabeltemp.png")).toExternalForm(),
                true
        ));
        dinoIV.setPreserveRatio(true);
        dinoIV.setSmooth(true);
        dinoIV.fitWidthProperty().bind(Dino.widthProperty().multiply(0.7));
        dinoIV.fitHeightProperty().bind(Dino.heightProperty().multiply(0.7));
        Dino.setGraphic(dinoIV);

        // --- Button actions ---
        Pong.setOnAction(e -> { try { Views.getPongView(rootPane); } catch (Exception ex) { ex.printStackTrace(); } });
        Tetris.setOnAction(e -> { try { Views.getTetrisView(rootPane); } catch (Exception ex) { ex.printStackTrace(); } });
        Pacman.setOnAction(e -> { try { Views.getPacmanView(rootPane); } catch (Exception ex) { ex.printStackTrace(); } });
        Dino.setOnAction(e -> { try { Views.getDinoView(rootPane); } catch (Exception ex) { ex.printStackTrace(); } });
    }
}
