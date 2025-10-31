package controller.paddle_control;

import javafx.scene.paint.Color;

public class FrozenPaddle extends Paddle implements PaddleVari {

    public FrozenPaddle(int x, int y) {
        super(x, y,
                BASIC_PAD_WIDTH,
                PADDLE_HEIGHT,
                FROZEN_PAD_SPEED,
                PaddleType.FROZEN, Color.BLUE
        );
    }
}
