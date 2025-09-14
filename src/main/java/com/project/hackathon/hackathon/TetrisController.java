package com.project.hackathon.hackathon;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Random;

public class TetrisController {

    // ---- FXML ----
    @FXML public AnchorPane rootPane;
    @FXML public Button backToMain;
    @FXML public Canvas canvas;
    @FXML public Label scoreLabel;
    @FXML public Label statusLabel;

    // ---- Game constants ----
    private static final int COLS = 10;
    private static final int ROWS = 20;
    private static final int CELL = 30; // pixel size

    // ---- Board & piece state ----
    private final int[][] board = new int[ROWS][COLS]; // 0 empty; >0 color index
    private int[][] curShape;          // 4x4 0/1
    private int curRow, curCol;        // top-left of 4x4 bounding box on board
    private int curColorIndex;         // 1..colors.length
    private final Random rng = new Random();

    // ---- Timing/score ----
    private Timeline loop;
    private int score = 0;
    private boolean gameOver = false;
    private boolean paused = false;

    // 7 tetromino shapes as 4x4 matrices
    private static final int[][] S_I = {
            {0,0,0,0},
            {1,1,1,1},
            {0,0,0,0},
            {0,0,0,0}
    };
    private static final int[][] S_O = {
            {1,1,0,0},
            {1,1,0,0},
            {0,0,0,0},
            {0,0,0,0}
    };
    private static final int[][] S_T = {
            {0,1,0,0},
            {1,1,1,0},
            {0,0,0,0},
            {0,0,0,0}
    };
    private static final int[][] S_S = {
            {0,1,1,0},
            {1,1,0,0},
            {0,0,0,0},
            {0,0,0,0}
    };
    private static final int[][] S_Z = {
            {1,1,0,0},
            {0,1,1,0},
            {0,0,0,0},
            {0,0,0,0}
    };
    private static final int[][] S_J = {
            {1,0,0,0},
            {1,1,1,0},
            {0,0,0,0},
            {0,0,0,0}
    };
    private static final int[][] S_L = {
            {0,0,1,0},
            {1,1,1,0},
            {0,0,0,0},
            {0,0,0,0}
    };

    private static final int[][][] SHAPES = { S_I, S_O, S_T, S_S, S_Z, S_J, S_L };

    // pleasant defaults; index 0 unused so board values map 1..7
    private static final Color[] COLORS = {
            Color.TRANSPARENT,
            Color.web("#00FFFF"), // I
            Color.web("#F1C40F"), // O
            Color.web("#9B59B6"), // T
            Color.web("#2ECC71"), // S
            Color.web("#E74C3C"), // Z
            Color.web("#2980B9"), // J
            Color.web("#E67E22")  // L
    };

    @FXML
    public void initialize() {
        // Back button keeps your existing flow
        backToMain.setOnAction(event -> {
            try {
                Views.getMainView(rootPane);
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        // Set up input once the Scene exists
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKey);
                rootPane.requestFocus();
            }
        });

        // Start a new game
        resetGame();

        // Game loop (gravity)
        loop = new Timeline(new KeyFrame(Duration.millis(500), e -> {
            if (!paused && !gameOver) {
                tick();
            }
        }));
        loop.setCycleCount(Animation.INDEFINITE);
        loop.play();

        // initial draw
        draw();
    }

    // ---- Game control ----
    private void resetGame() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) board[r][c] = 0;
        }
        score = 0;
        gameOver = false;
        paused = false;
        statusLabel.setText("");
        updateScore();
        spawnNewPiece();
    }

    private void tick() {
        if (canFit(curShape, curRow + 1, curCol)) {
            curRow++;
        } else {
            // lock piece
            merge(curShape, curRow, curCol, curColorIndex);
            int cleared = clearFullLines();
            if (cleared > 0) {
                score += switch (cleared) {
                    case 1 -> 10;
                    case 2 -> 30;
                    case 3 -> 50;
                    case 4 -> 80;
                    default -> 0;
                };
                updateScore();
            }
            // next piece
            spawnNewPiece();
        }
        draw();
    }

    private void spawnNewPiece() {
        int idx = rng.nextInt(SHAPES.length); // 0..6
        curShape = deepCopy(SHAPES[idx]);
        curColorIndex = idx + 1;              // map to COLORS[1..7]
        curRow = 0;
        curCol = 3;
        if (!canFit(curShape, curRow, curCol)) {
            gameOver = true;
            statusLabel.setText("Game Over — press R to restart");
        }
    }

    private void handleKey(KeyEvent e) {
        if (gameOver) {
            if (e.getCode() == KeyCode.R) {
                resetGame();
                draw();
            }
            return;
        }

        if (e.getCode() == KeyCode.P) {
            paused = !paused;
            statusLabel.setText(paused ? "Paused" : "");
            return;
        }

        if (paused) return;

        switch (e.getCode()) {
            case LEFT -> {
                if (canFit(curShape, curRow, curCol - 1)) curCol--;
            }
            case RIGHT -> {
                if (canFit(curShape, curRow, curCol + 1)) curCol++;
            }
            case DOWN -> {
                if (canFit(curShape, curRow + 1, curCol)) curRow++;
            }
            case UP, X -> {
                int[][] rot = rotateCW(curShape);
                if (canFit(rot, curRow, curCol)) curShape = rot;
            }
            case Z -> { // counter-clockwise
                int[][] rot = rotateCCW(curShape);
                if (canFit(rot, curRow, curCol)) curShape = rot;
            }
            case SPACE -> { // hard drop
                while (canFit(curShape, curRow + 1, curCol)) curRow++;
                tick(); // lock and continue
            }
            case R -> { // quick restart
                resetGame();
            }
            default -> {}
        }
        draw();
    }

    // ---- Board helpers ----
    private boolean canFit(int[][] shape, int baseRow, int baseCol) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (shape[r][c] == 0) continue;
                int rr = baseRow + r;
                int cc = baseCol + c;
                if (cc < 0 || cc >= COLS || rr < 0 || rr >= ROWS) return false;
                if (board[rr][cc] != 0) return false;
            }
        }
        return true;
    }

    private void merge(int[][] shape, int baseRow, int baseCol, int colorIdx) {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (shape[r][c] == 0) continue;
                int rr = baseRow + r;
                int cc = baseCol + c;
                if (rr >= 0 && rr < ROWS && cc >= 0 && cc < COLS) {
                    board[rr][cc] = colorIdx;
                }
            }
        }
    }

    private int clearFullLines() {
        int cleared = 0;
        for (int r = ROWS - 1; r >= 0; r--) {
            boolean full = true;
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == 0) { full = false; break; }
            }
            if (full) {
                cleared++;
                // shift everything down
                for (int rr = r; rr > 0; rr--) {
                    System.arraycopy(board[rr - 1], 0, board[rr], 0, COLS);
                }
                // new empty top row
                for (int c = 0; c < COLS; c++) board[0][c] = 0;
                r++; // re-check same row index after shift
            }
        }
        return cleared;
    }

    private void updateScore() {
        scoreLabel.setText("Score: " + score);
    }

    // ---- Rotation helpers ----
    private static int[][] rotateCW(int[][] src) {
        int[][] dst = new int[4][4];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                dst[r][c] = src[4 - 1 - c][r];
            }
        }
        return dst;
    }

    private static int[][] rotateCCW(int[][] src) {
        int[][] dst = new int[4][4];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                dst[r][c] = src[c][4 - 1 - r];
            }
        }
        return dst;
    }

    private static int[][] deepCopy(int[][] m) {
        int[][] copy = new int[m.length][];
        for (int i = 0; i < m.length; i++) {
            copy[i] = m[i].clone();
        }
        return copy;
    }

    private int pickColorIndexFor(int[][] shape) {
        // Map shapes to stable color indices by identity
        if (shape == S_I) return 1;
        if (shape == S_O) return 2;
        if (shape == S_T) return 3;
        if (shape == S_S) return 4;
        if (shape == S_Z) return 5;
        if (shape == S_J) return 6;
        return 7; // L
    }

    // ---- Rendering ----
    private void draw() {
        GraphicsContext g = canvas.getGraphicsContext2D();

        // background
        g.setFill(Color.web("#111"));
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // grid backdrop
        g.setStroke(Color.web("#222"));
        for (int y = 0; y <= ROWS; y++) {
            g.strokeLine(0, y * CELL, COLS * CELL, y * CELL);
        }
        for (int x = 0; x <= COLS; x++) {
            g.strokeLine(x * CELL, 0, x * CELL, ROWS * CELL);
        }

        // board cells
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int v = board[r][c];
                if (v != 0) fillCell(g, c, r, COLORS[v]);
            }
        }

        // current piece
        if (!gameOver) {
            Color col = COLORS[curColorIndex];
            for (int r = 0; r < 4; r++) {
                for (int c = 0; c < 4; c++) {
                    if (curShape[r][c] == 1) {
                        int br = curRow + r;
                        int bc = curCol + c;
                        if (br >= 0 && br < ROWS && bc >= 0 && bc < COLS) {
                            fillCell(g, bc, br, col);
                        }
                    }
                }
            }
        }
    }

    private void fillCell(GraphicsContext g, int x, int y, Color col) {
        double px = x * CELL;
        double py = y * CELL;
        g.setFill(col);
        g.fillRect(px + 1, py + 1, CELL - 2, CELL - 2);
        g.setStroke(col.deriveColor(0, 1, 0.6, 1));
        g.strokeRect(px + 0.5, py + 0.5, CELL - 1, CELL - 1);
    }
}

