package controller.paddle_control;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.onKey;

public class BasicPaddle extends Paddle implements PaddleVari {

    public BasicPaddle(int x, int y) {
        super(x, y,
                BASIC_PAD_WIDTH,
                PADDLE_HEIGHT,
                BASIC_PAD_SPEED,
                PaddleType.BASIC
        );
    }

    @Override
    public int getPADDLE_HEIGHT() {
        return super.getPADDLE_HEIGHT();
    }
}
