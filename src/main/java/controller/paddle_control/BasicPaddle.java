package controller.paddle_control;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.onKey;

public class BasicPaddle extends Paddle {

    public BasicPaddle(int x, int y) {
        super(x, y,
                PaddleVari.BASIC_PAD_WIDTH,
                PaddleVari.PADDLE_HEIGHT,
                PaddleVari.BASIC_PAD_SPEED,
                PaddleType.BASIC, Color.YELLOW
        );
    }

    @Override
    public int getPADDLE_HEIGHT() {
        return super.getPADDLE_HEIGHT();
    }
}
