package controller.ball_control;

import controller.paddle_control.PaddleVari;
import javafx.scene.image.Image;

import java.util.Objects;

import static controller.InitVari.SCREEN_HEIGHT;

public interface BallVari {
    double BALL_RADIUS = 12;
    double MAX_ANGLE = 60;
    double DEFAULT_DirectionX = 1;
    double DEFAULT_DirectionY = -1;
    double SPAWN_BALL_GAP = 10;
    double DEFAULT_SPEED = 4;
    double POWER_UP_SPEED = 16;
    double MAX_SPEED = 10;
    double CLOCK_TIME = 10;
    double SPEED_RATE = 0.01;
    double ADJUST_BALL_SPEED = 60;
    int DEFAULT_LIFE = 3;
    int MAX_LIFE = 5;
    double RESET_TIME = 10;

    double HEART_SIZE = 60;
    double FIRST_HEART_X = 20;
    double FIRST_HEART_Y = SCREEN_HEIGHT - 60 - 40;

    Image NORMAL_BALL = new Image(
            Objects.requireNonNull(BallVari.class.getResource("/assets/textures/balls/ball_normal.png").toExternalForm())
    );

    Image HARD_BALL = new Image(
            Objects.requireNonNull(BallVari.class.getResource("/assets/textures/balls/ball_hard.png").toExternalForm())
    );

    Image SPEEDUP_BALL = new Image(
            Objects.requireNonNull(BallVari.class.getResource("/assets/textures/balls/ball_speedup.png").toExternalForm())
    );

    Image MULTI_BALL = new Image(
            Objects.requireNonNull(BallVari.class.getResource("/assets/textures/balls/ball_multi.png").toExternalForm())
    );

    Image HEART_IMG = new Image(
            Objects.requireNonNull(BallVari.class.getResource("/assets/textures/balls/heart.png").toExternalForm())
    );
}
