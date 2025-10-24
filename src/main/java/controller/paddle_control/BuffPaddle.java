package controller.paddle_control;

import javafx.scene.paint.Color;

public class BuffPaddle extends Paddle implements PaddleVari {

    public BuffPaddle(int x, int y) {
        super(x, y,
                BUFF_PAD_WIDTH,
                PADDLE_HEIGHT,
                BASIC_PAD_SPEED,
                PaddleType.BUFF, Color.GREEN
        );
    }
}
