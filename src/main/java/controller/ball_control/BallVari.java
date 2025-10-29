package controller.ball_control;

import javafx.scene.image.Image;

import java.util.Objects;

public interface BallVari {
    double BALL_RADIUS = 12;
    double MAX_ANGLE = 70;
    double BALL_HITBOX = 10;

    Image NORMAL_BALL = new Image(
            Objects.requireNonNull(BallVari.class.getResource("/assets/textures/balls/ball_normal.png").toExternalForm())
    );

    Image HARD_BALL = new Image(
            Objects.requireNonNull(BallVari.class.getResource("/assets/textures/balls/ball_hard.png").toExternalForm())
    );

    Image POWERUP_BALL = new Image(
            Objects.requireNonNull(BallVari.class.getResource("/assets/textures/balls/ball_powerup.png").toExternalForm())
    );

    Image SPEEDUP_BALL = new Image(
            Objects.requireNonNull(BallVari.class.getResource("/assets/textures/balls/ball_speedup.png").toExternalForm())
    );
}
