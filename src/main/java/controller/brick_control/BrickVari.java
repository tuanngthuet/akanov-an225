package controller.brick_control;

import javafx.scene.image.Image;

import java.util.Objects;

public class BrickVari {
    public static int BRICK_WIDTH = 64;
    public static int BRICK_HEIGHT = 32;
    public static int BRICK_GAP = 10;

    public static int NORMAL_SPRITE_ROWS = 6;
    public static int NORMAL_SPRITE_COLUMNS = 11;
    public static final Image NORMAL_SPRITE = new Image(
            Objects.requireNonNull(BrickVari.class.getResource("/assets/textures/bricks/normal.png")).toExternalForm()
    );

    public static final int HARD_SPRITE_ROWS = 1;
    public static final Image HARD_SPRITE = new Image(
            Objects.requireNonNull(BrickVari.class.getResource("/assets/textures/bricks/hard.png")).toExternalForm()
    );

    public static final int POWERUP_SPRITE_ROWS = 1;
    public static final Image POWERUP_SPRITE = new Image(
            Objects.requireNonNull(BrickVari.class.getResource("/assets/textures/bricks/powerup.png")).toExternalForm()
    );
}
