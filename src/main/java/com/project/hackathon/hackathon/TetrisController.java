package com.project.hackathon.hackathon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class TetrisController {

    @FXML public AnchorPane rootPane;

    @FXML public Button backToMain;
    public void initialize(){
        backToMain.setOnAction(event -> {
            try {
                Views.getMainView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });
    }
}
