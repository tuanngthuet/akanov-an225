package controller.paddle_control;

import javafx.scene.paint.Color;

public class BuffPaddle extends Paddle {

    public BuffPaddle(int x, int y) {
        super(x, y,
                PaddleVari.BUFF_PAD_WIDTH,
                PaddleVari.PAD_HEIGHT,
                PaddleVari.BASIC_PAD_SPEED,
                PaddleType.BUFF, Color.GREEN
        );
    }
}
