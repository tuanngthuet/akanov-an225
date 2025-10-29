package controller.brick_control;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public interface BrickVari {
    enum BrickType {
        NORMAL, HARD, POWERUP
    }
    
    int BRICK_WIDTH = 64;
    int BRICK_HEIGHT = 32;
    int BRICK_GAP = 20;

    int NORMAL_SPRITE_ROWS = 6;
    int NORMAL_SPRITE_COLUMNS = 11;
    Image NORMAL_SPRITE = new Image(
            Objects.requireNonNull(BrickVari.class.getResource("/assets/textures/bricks/normal.png")).toExternalForm()
    );

//    int HARD_SPRITE_ROWS = 1;
    Image HARD_SPRITE = new Image(
            Objects.requireNonNull(BrickVari.class.getResource("/assets/textures/bricks/hard.png")).toExternalForm()
    );

    int POWERUP_SPRITE_COLUMNS = 11;
    Image POWERUP_SPRITE = new Image(
            Objects.requireNonNull(BrickVari.class.getResource("/assets/textures/bricks/powerup.png")).toExternalForm()
    );

    ImageView TEXTURE = new ImageView(NORMAL_SPRITE);
    default void setView() {
        TEXTURE.setScaleX(2);
        TEXTURE.setScaleY(2);

        TEXTURE.setViewport(new Rectangle2D(
                0,
                (double) (NORMAL_SPRITE_COLUMNS * BRICK_HEIGHT) / 2,
                (double) BRICK_WIDTH / 2,
                (double) BRICK_HEIGHT / 2
        ));
    }
}
