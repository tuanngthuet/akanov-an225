package controller.paddle_control;

import com.almasb.fxgl.dsl.FXGL;
import javafx.util.Duration;

public class PaddleManager implements PaddleVari {

    private static Paddle currentPaddle;

    public static void initPaddle(int x, int y) {
        currentPaddle = new BasicPaddle(x, y);
        FXGL.getGameWorld().addEntity(currentPaddle);
    }

    public static Paddle getCurrentPaddle() {
        return currentPaddle;
    }

    public static void applyPaddlePowerUp(PaddleVari.PaddleType powerUpType, int duration) {
        if (currentPaddle == null) return;

        switch (powerUpType) {
            case BUFF -> {
                currentPaddle.setType(PaddleType.BUFF);
                currentPaddle.setSize(BUFF_PAD_WIDTH, PADDLE_HEIGHT);
                currentPaddle.setSpeed(BASIC_PAD_SPEED);
            }
            case NERF -> {
                currentPaddle.setType(PaddleType.NERF);
                currentPaddle.setSize(NERF_PAD_WIDTH, PADDLE_HEIGHT);
                currentPaddle.setSpeed(NERF_PAD_SPEED);
            }
            case FROZEN -> {
                currentPaddle.setType(PaddleType.FROZEN);
                currentPaddle.setSize(BASIC_PAD_WIDTH, PADDLE_HEIGHT);
                currentPaddle.setSpeed(FROZEN_PAD_SPEED);
            }
            default -> {
                currentPaddle.resetToBasic();
            }
        }

        FXGL.runOnce(() -> {
            if (currentPaddle != null && currentPaddle.getPaddleType() != PaddleType.BASIC) {
                currentPaddle.resetToBasic();
            }
        }, Duration.seconds(duration));
    }
}
