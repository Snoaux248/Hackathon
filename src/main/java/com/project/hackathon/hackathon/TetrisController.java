package com.project.hackathon.hackathon;

//<!-- gahhh -->

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.geometry.VPos;
import javafx.util.Duration;

import java.net.URL;
import java.util.Random;

public class TetrisController {

    // ---- FXML ----
    @FXML public AnchorPane rootPane;
    @FXML public Button backToMain;
    @FXML public Canvas canvas;
    @FXML public Label scoreLabel;
    @FXML public Label gameLogLabel;
    @FXML public Label statusLabel;   // kept for compatibility; no longer used to show pause/over
    @FXML public TextArea textBox;    // notes box (no pause/over text anymore)

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

        // ======= Set background images (EDIT THESE PATHS) =======
        setRootBackgroundImage("/images/bg_full.jpg");     // or "file:/C:/path/bg.jpg"
        setTextBoxBackgroundImage("/images/bg_notes.jpg"); // or "file:/C:/path/bg2.jpg"

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

        DropShadow outline = new DropShadow();
        outline.setColor(Color.BLACK); // outline color switched to black
        outline.setRadius(3);          // you can tweak thickness
        outline.setSpread(1.0);
        outline.setOffsetX(0);
        outline.setOffsetY(0);

        scoreLabel.setEffect(outline);
        gameLogLabel.setEffect(outline);

        // Game log setup (non-interactive)
        textBox.setText("Welcome to Tetris!\nUse arrow keys to move, up arrow to rotate, R to restart, P to pause.");
        textBox.setEditable(false);
        textBox.setFocusTraversable(false);
        textBox.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> e.consume());
        textBox.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> e.consume());
    }

    // ---- Game control ----
    private void resetGame() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) board[r][c] = 0;
        }
        score = 0;
        gameOver = false;
        paused = false;
        statusLabel.setText(""); // not used for pause/over anymore
        updateScore();
        spawnNewPiece();

        if (textBox != null) {
            textBox.appendText("\nGame reset!\n");
        }
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
                if (textBox != null) {
                    textBox.appendText("\nCleared " + cleared + " line(s)!");
                }
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
            statusLabel.setText(""); // no label text
            // overlay handles game over text
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
            statusLabel.setText(""); // no label text
            draw(); // overlay shows paused
            return;
        }

        if (paused) return;

        switch (e.getCode()) {
            case LEFT -> { if (canFit(curShape, curRow, curCol - 1)) curCol--; }
            case RIGHT -> { if (canFit(curShape, curRow, curCol + 1)) curCol++; }
            case DOWN -> { if (canFit(curShape, curRow + 1, curCol)) curRow++; }
            case UP, X -> {
                int[][] rot = rotateCW(curShape);
                if (canFit(rot, curRow, curCol)) curShape = rot;
            }
            case Z -> {
                int[][] rot = rotateCCW(curShape);
                if (canFit(rot, curRow, curCol)) curShape = rot;
            }
            case SPACE -> {
                while (canFit(curShape, curRow + 1, curCol)) curRow++;
                tick(); // lock and continue
                if (textBox != null) textBox.appendText("\nHard drop!");
            }
            case R -> resetGame();
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
                for (int rr = r; rr > 0; rr--) {
                    System.arraycopy(board[rr - 1], 0, board[rr], 0, COLS);
                }
                for (int c = 0; c < COLS; c++) board[0][c] = 0;
                r++;
            }
        }
        return cleared;
    }

    private void updateScore() { scoreLabel.setText("Score: " + score); }

    // ---- Rotation helpers ----
    private static int[][] rotateCW(int[][] src) {
        int[][] dst = new int[4][4];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) dst[r][c] = src[4 - 1 - c][r];
        }
        return dst;
    }
    private static int[][] rotateCCW(int[][] src) {
        int[][] dst = new int[4][4];
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) dst[r][c] = src[c][4 - 1 - r];
        }
        return dst;
    }
    private static int[][] deepCopy(int[][] m) {
        int[][] copy = new int[m.length][];
        for (int i = 0; i < m.length; i++) copy[i] = m[i].clone();
        return copy;
    }
    private int pickColorIndexFor(int[][] shape) {
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

        // grid
        g.setStroke(Color.web("#222"));
        for (int y = 0; y <= ROWS; y++) g.strokeLine(0, y * CELL, COLS * CELL, y * CELL);
        for (int x = 0; x <= COLS; x++) g.strokeLine(x * CELL, 0, x * CELL, ROWS * CELL);

        // board cells
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int v = board[r][c];
                if (v != 0) fillCell(g, c, r, COLORS[v]);
            }
        }

        // current piece (hide when game over)
        if (!gameOver) {
            Color col = COLORS[curColorIndex];
            for (int r = 0; r < 4; r++) for (int c = 0; c < 4; c++) {
                if (curShape[r][c] == 1) {
                    int br = curRow + r, bc = curCol + c;
                    if (br >= 0 && br < ROWS && bc >= 0 && bc < COLS) fillCell(g, bc, br, col);
                }
            }
        }

        // overlay for paused / game over
        if (paused || gameOver) {
            drawOverlay(g,
                    gameOver ? "YOU LOST" : "PAUSED",
                    gameOver ? "Press R to restart" : "Press P to resume • R to restart");
        }
    }

    private void fillCell(GraphicsContext g, int x, int y, Color col) {
        double px = x * CELL, py = y * CELL;
        g.setFill(col);
        g.fillRect(px + 1, py + 1, CELL - 2, CELL - 2);
        g.setStroke(col.deriveColor(0, 1, 0.6, 1));
        g.strokeRect(px + 0.5, py + 0.5, CELL - 1, CELL - 1);
    }

    private void drawOverlay(GraphicsContext g, String main, String sub) {
        double w = canvas.getWidth(), h = canvas.getHeight();
        g.setFill(Color.rgb(0, 0, 0, 0.65)); g.fillRect(0, 0, w, h);
        g.setTextAlign(TextAlignment.CENTER); g.setTextBaseline(VPos.CENTER);
        g.setFill(Color.WHITE); g.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 56));
        g.fillText(main, w / 2.0, h / 2.0 - 10);
        g.setFont(Font.font("System", FontWeight.SEMI_BOLD, 20));
        g.setFill(Color.web("#DDDDDD"));
        g.fillText(sub, w / 2.0, h / 2.0 + 34);
    }

    // ========= Background helpers =========

    /** Set a cover background image on the whole screen (root AnchorPane). */
    private void setRootBackgroundImage(String pathOrUrl) {
        Image img = loadImage(pathOrUrl);
        if (img == null) return;
        BackgroundSize size = new BackgroundSize(100, 100, true, true, false, true); // cover
        BackgroundImage bg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, size);
        rootPane.setBackground(new Background(bg));
    }

    /**
     * Properly styled background for the TextArea:
     * - Image on the viewport (behind the text; does not scroll with text)
     * - Semi-transparent scrim on the content for readability
     * - Rounded corners, padding, and subtle border
     */
    private void setTextBoxBackgroundImage(String pathOrUrl) {
        Image img = loadImage(pathOrUrl);
        if (img == null) return;

        // Make the TextArea chrome transparent so our custom layers show.
        textBox.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-control-inner-background: transparent;" +
                        "-fx-text-fill: white;" +
                        "-fx-highlight-fill: rgba(255,255,255,0.25);" +
                        "-fx-highlight-text-fill: white;"
        );

        Runnable apply = () -> {
            // ScrollPane and viewport must be transparent so the image is visible.
            Region scrollPane = (Region) textBox.lookup(".scroll-pane");
            if (scrollPane != null) {
                scrollPane.setStyle("-fx-background-color: transparent;");
            }

            // Put the background IMAGE on the viewport so it doesn't scroll with the text.
            Region viewport = (Region) textBox.lookup(".viewport");
            if (viewport != null) {
                BackgroundSize size = new BackgroundSize(100, 100, true, true, false, true); // cover
                BackgroundImage bg = new BackgroundImage(
                        img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER, size
                );
                viewport.setBackground(new Background(bg));
            }

            // Put a semi-transparent scrim + rounding/padding on the CONTENT above the image.
            Region content = (Region) textBox.lookup(".content");
            if (content != null) {
                content.setStyle(
                        "-fx-background-color: rgba(0,0,0,0.40);" +
                                "-fx-background-insets: 0;" +
                                "-fx-background-radius: 10;" +
                                "-fx-padding: 10;" +
                                "-fx-border-color: rgba(255,255,255,0.15);" +
                                "-fx-border-radius: 10;" +
                                "-fx-border-insets: 0;"
                );
            }
        };

        // Apply after the skin exists (on first layout).
        if (textBox.getSkin() == null) {
            textBox.skinProperty().addListener((obs, o, n) -> Platform.runLater(apply));
        } else {
            Platform.runLater(apply);
        }
    }

    /** Loads an Image from classpath ("/...") or direct URL ("file:/", "http://", etc.). */
    private Image loadImage(String pathOrUrl) {
        try {
            if (pathOrUrl == null || pathOrUrl.isBlank()) return null;
            if (pathOrUrl.startsWith("file:") || pathOrUrl.startsWith("http")) {
                return new Image(pathOrUrl, true);
            }
            URL url = getClass().getResource(pathOrUrl);
            if (url == null) {
                System.err.println("Image not found on classpath: " + pathOrUrl);
                return null;
            }
            return new Image(url.toExternalForm(), true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
