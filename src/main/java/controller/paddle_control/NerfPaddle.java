package controller.paddle_control;

import javafx.scene.paint.Color;

public class NerfPaddle extends Paddle {

    public NerfPaddle(int x, int y) {
        super(x, y,
                PaddleVari.NERF_PAD_WIDTH,
                PaddleVari.PAD_HEIGHT,
                PaddleVari.NERF_PAD_SPEED,
                PaddleType.NERF, Color.RED
        );
    }
}
