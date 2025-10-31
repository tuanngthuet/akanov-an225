package view;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import controller.InitVari;
import controller.ball_control.*;
import controller.brick_control.BrickManager;
import controller.paddle_control.PaddleVari;
import javafx.scene.input.KeyCode;
import controller.paddle_control.BasicPaddle;

import java.util.ArrayList;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Launch extends GameApplication implements InitVari {

    public enum EntityType {
        BALL,
        PADDLE,
        BRICK,
        POWERUP
    }

    public static Ball ball;
    public BasicPaddle paddle;
    public BrickManager bricks;
    public LifeManager lifeManager;
    public static PowerUpHandler powerHandler;
    public BallManager ballManager;



    @Override
    public void initSettings(GameSettings settings) {
        GeneralInit.initScreenSettings(settings);
    }

    @Override
    protected void initGame() {
        getGameScene().addGameView(BACKGROUND);

        lifeManager = new LifeManager();
        lifeManager.init();

        ballManager = new BallManager();

        powerHandler = new PowerUpHandler(ballManager, lifeManager);

        ball = new Ball( BallVari.DEFAULT_DirectionX, BallVari.DEFAULT_DirectionY, Ball.BallType.NORMAL);
        ball = ballManager.spawn_InitBall();
//        ball.setType(EntityType.BALL);
//        getGameWorld().addEntity(ball);

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
        for (Ball b : new ArrayList<>(BallManager.getInstance().getBalls())) {
            b.update(tpf, paddle, bricks, lifeManager);
            b.IncreaseBallSpeed();
            paddle.update();
        }
    }

    protected void initInput() {
        onKey(KeyCode.RIGHT, () ->   paddle.moveRight());
        onKey(KeyCode.LEFT, ()  ->   paddle.moveLeft() );
        onKey(KeyCode.D, () -> bricks.clearAll());
        onKey(KeyCode.R, () -> bricks.spamBrick(4));
    }
}