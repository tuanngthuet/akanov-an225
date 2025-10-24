package controller.brick_control;

import javafx.scene.image.Image;

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
//    Image HARD_SPRITE = new Image(
//            Objects.requireNonNull(BrickVari.class.getResource("/assets/textures/bricks/hard.png")).toExternalForm()
//    );
//
//    int POWERUP_SPRITE_ROWS = 1;
//    Image POWERUP_SPRITE = new Image(
//            Objects.requireNonNull(BrickVari.class.getResource("/assets/textures/bricks/powerup.png")).toExternalForm()
//    );
}
