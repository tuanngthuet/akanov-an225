package controller.paddle_control;

import javafx.scene.paint.Color;

public class NerfPaddle extends Paddle implements PaddleVari {

    public NerfPaddle(int x, int y) {
        super(x, y,
                NERF_PAD_WIDTH,
                PADDLE_HEIGHT,
                NERF_PAD_SPEED,
                PaddleType.NERF, Color.RED
        );
    }
}
