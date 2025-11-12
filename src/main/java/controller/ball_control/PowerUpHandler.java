package controller.ball_control;

import controller.powerup.PowerUp;
import controller.paddle_control.*;
import view.Launch;

import java.util.ArrayList;
import java.util.List;

public class PowerUpHandler implements BallVari {

    private BallManager ballManager;
    private LifeManager lifeManager;
    private Paddle paddle;
    public static double prev_speed = 0;

    public PowerUpHandler(BallManager ballManager, LifeManager lifeManager, Paddle paddle) {
        this.ballManager = ballManager;
        this.lifeManager = lifeManager;
        this.paddle = paddle;
    }

    public void Pick_Up(PowerUp.PowerType type) {
        switch (type) {
            case MULTIBALL -> {
                List<Ball> balls = new ArrayList<>(ballManager.getBalls());
                for(Ball ball : balls) {
                    ballManager.spawnExtraBall(ball);
                }
            }
            case SPEEDUPBALL -> {
                for (Ball ball : ballManager.getBalls()) {
                    if(prev_speed != POWER_UP_SPEED) {
                        prev_speed = ball.getSpeed();
                    }
                    ball.setSpeed(POWER_UP_SPEED);
                    ball.setType(Ball.BallType.SPEEDUPBALL);
                }
            }
            case HARDBALL -> {
                for (Ball ball : ballManager.getBalls()) {
                    ball.setHardBall(true);
                    ball.setType(Ball.BallType.HARDBALL);
                }
            }
            case EXTRALIFE -> lifeManager.gainHeart();

            case BUFF -> PaddleManager.applyPaddlePowerUp(PaddleVari.PaddleType.BUFF, 20);

            case NERF -> PaddleManager.applyPaddlePowerUp(PaddleVari.PaddleType.NERF, 20);

            case FROZEN -> PaddleManager.applyPaddlePowerUp(PaddleVari.PaddleType.FROZEN, 1);
        }
        for (Ball ball : ballManager.getBalls()) {
            ballManager.reset_ToNormal(ball);
        }
    }
}
