package view;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import controller.InitVari;
import controller.brick_control.Brick;
import controller.brick_control.BrickManager;
import controller.paddle_control.PaddleVari;
import javafx.scene.input.KeyCode;
import controller.ball_control.Ball;
import controller.paddle_control.BasicPaddle;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Launch extends GameApplication implements InitVari {

    public enum EntityType {
        BALL,
        PADDLE,
        BRICK,
        POWERUP
    }

    private Ball ball;
    private BasicPaddle paddle;
    private BrickManager bricks;


    @Override
    public void initSettings(GameSettings settings) {
        GeneralInit.initScreenSettings(settings);
    }

    @Override
    protected void initGame() {
        getGameScene().addGameView(BACKGROUND);
        ball = new Ball((double) SCREEN_WIDTH / 2, SCREEN_HEIGHT - PaddleVari.PADDLE_HEIGHT - 50, 4, 1, -1, Ball.BallType.NORMAL);
        getGameWorld().addEntity(ball);
        ball.setType(EntityType.BALL);


        paddle = new BasicPaddle(SCREEN_WIDTH / 2, SCREEN_HEIGHT - PaddleVari.PADDLE_HEIGHT);
        // What is this ??
        getGameWorld().addEntity(paddle);
        paddle.setType(EntityType.PADDLE);
        ball.startFalling();

        bricks = BrickManager.getInstance();
        bricks.spamBrick(4);
    }

    @Override
    protected void onUpdate(double tpf) {
        // Mấy cái này nen add Component vào Entity
        ball.update(tpf, paddle, bricks);
        paddle.update();
        ball.IncreaseBallSpeed();
    }

    protected void initInput() {
        onKey(KeyCode.RIGHT, () ->   paddle.moveRight());
        onKey(KeyCode.LEFT, ()  ->   paddle.moveLeft() );
        onKey(KeyCode.D, () -> bricks.clearAll());
        onKey(KeyCode.R, () -> bricks.spamBrick(4));
    }
}