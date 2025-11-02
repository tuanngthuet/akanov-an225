package controller.ball_control;

import com.almasb.fxgl.entity.Entity;
import controller.powerup.PowerUp;
import view.Launch;

public class PowerUpHandler implements BallVari{
    private BallManager ballManager;
    private LifeManager lifeManager;

    public PowerUpHandler(BallManager ballManager, LifeManager lifeManager) {
        this.ballManager = ballManager;
        this.lifeManager = lifeManager;
    }

    public void Pick_Up(PowerUp.PowerType type) {
        switch (type) {
            case MULTIBALL -> ballManager.spawnExtraBall();
            case SPEEDUPBALL -> {
                for(Ball ball : ballManager.getBalls()) {
                    ball.setSpeed(POWER_UP_SPEED);
                    ball.setType(Ball.BallType.SPEEDUPBALL);
                }
            }
            case HARDBALL -> {
                for(Ball ball : ballManager.getBalls()) {
                    ball.setHardBall(true);
                    ball.setType(Ball.BallType.HARDBALL);
                }
            }
            case EXTRALIFE -> lifeManager.gainHeart();
        }
        for(Ball ball : ballManager.getBalls()) {
            ballManager.reset_ToNormal(ball);
        }
    }
}
