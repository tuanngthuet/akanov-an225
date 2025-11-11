package controller.ball_control;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import controller.InitVari;
import controller.paddle_control.PaddleVari;
import javafx.util.Duration;
import view.Launch;

import java.util.ArrayList;
import java.util.List;

public class BallManager implements InitVari, PaddleVari, BallVari {
    private List<Ball> balls = new ArrayList<>();

    public Ball spawn_InitBall() {
        Ball ball = new Ball(BallVari.DEFAULT_DirectionX, BallVari.DEFAULT_DirectionY, Ball.BallType.NORMAL);
        ball.setPosition((double) SCREEN_WIDTH / 2, SCREEN_HEIGHT - PaddleVari.PADDLE_HEIGHT - 50);
        FXGL.getGameWorld().addEntity(ball);
        balls.add(ball);
        return ball;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void spawnExtraBall() {
        Ball ball1 = new Ball(-Launch.ball.getDirectionX(), Launch.ball.getDirectionY(), Ball.BallType.MULTIBALL);
        Ball ball2 = new Ball(Launch.ball.getDirectionX(), -Launch.ball.getDirectionY(), Ball.BallType.MULTIBALL);
        ball1.setPosition(Launch.ball.getX() - SPAWN_BALL_GAP, Launch.ball.getY());
        ball2.setPosition(Launch.ball.getX() + SPAWN_BALL_GAP, Launch.ball.getY());

        ball1.setSpeed(DEFAULT_SPEED);
        ball2.setSpeed(DEFAULT_SPEED);

        normalizeDirection(ball1);
        normalizeDirection(ball2);

        FXGL.getGameWorld().addEntities(ball1,ball2);
        balls.add(ball1);
        balls.add(ball2);
    }

    public void reset_ToNormal(Ball ball) {
        FXGL.runOnce(() -> {
            ball.setType(Ball.BallType.NORMAL);
            ball.setSpeed(DEFAULT_SPEED);
            ball.setHardBall(false);
        }, Duration.seconds(RESET_TIME));
    }

    private void normalizeDirection(Ball ball) {
        double len = Math.sqrt(ball.getDirectionX() * ball.getDirectionX() + ball.getDirectionY() * ball.getDirectionY());
        if (len != 0) {
            ball.setDirection(ball.getDirectionX() / len, ball.getDirectionY() / len);
        }
    }
}
