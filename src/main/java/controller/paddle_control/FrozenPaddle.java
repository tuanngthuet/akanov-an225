package controller.paddle_control;

import javafx.scene.paint.Color;

public class FrozenPaddle extends Paddle {

    public FrozenPaddle(int x, int y) {
        super(x, y,
                PaddleVari.BASIC_PAD_WIDTH,
                PaddleVari.PADDLE_HEIGHT,
                PaddleVari.FROZEN_PAD_SPEED,
                PaddleType.FROZEN, Color.BLUE
        );
    }
}
