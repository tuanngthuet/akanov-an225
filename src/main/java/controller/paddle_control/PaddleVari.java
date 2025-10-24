package controller.paddle_control;

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
    int BUFF_PAD_WIDTH = 200;

    // Values for the NERF paddle
    int NERF_PAD_WIDTH = 150;
    int NERF_PAD_SPEED = 15;

    // Value for the FROZEN paddle
    int FROZEN_PAD_SPEED = 0;
}
