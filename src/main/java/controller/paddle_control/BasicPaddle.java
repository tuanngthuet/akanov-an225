package controller.paddle_control;

import javafx.scene.paint.Color;

public class BasicPaddle extends Paddle {

    public BasicPaddle(int x, int y) {
        super(x, y,
                PaddleVari.BASIC_PAD_WIDTH,
                PaddleVari.PAD_HEIGHT,
                PaddleVari.BASIC_PAD_SPEED,
                PaddleType.BASIC, Color.YELLOW
        );
    }

}
