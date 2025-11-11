package controller.paddle_control;

import controller.ball_control.BallVari;
import javafx.scene.image.Image;

import java.util.Objects;

public interface PaddleVari {
    enum PaddleType {
        BASIC, BUFF, NERF, FROZEN;
    }

    // Values for the default paddle
    int PADDLE_HEIGHT = 20;

    // Values for the BASIC paddle
    int BASIC_PAD_WIDTH = 180;
    int BASIC_PAD_SPEED = 10;

    // Value for the BUFF paddle
    int BUFF_PAD_WIDTH = 240;

    // Values for the NERF paddle
    int NERF_PAD_WIDTH = 150;
    int NERF_PAD_SPEED = 15;

    // Value for the FROZEN paddle
    int FROZEN_PAD_SPEED = 0;

    Image BASIC_PADDLE = new Image(
            Objects.requireNonNull(PaddleVari.class.getResource("/assets/textures/paddles/basicpaddle.png")).toExternalForm()
    );

    Image BUFF_PADDLE = new Image(
            Objects.requireNonNull(PaddleVari.class.getResource("/assets/textures/paddles/buffpaddle.png")).toExternalForm()
    );

    Image NERF_PADDLE = new Image(
            Objects.requireNonNull(PaddleVari.class.getResource("/assets/textures/paddles/nerfpaddle.png")).toExternalForm()
    );

    Image FROZEN_PADDLE = new Image(
            Objects.requireNonNull(PaddleVari.class.getResource("/assets/textures/paddles/frozenpaddle.png")).toExternalForm()
    );

}
