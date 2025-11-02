package view;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import controller.InitVari;
import controller.ScoreControl.Score_control;
import controller.ball_control.*;
import controller.brick_control.BrickManager;
import controller.paddle_control.*;
import javafx.scene.input.KeyCode;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Launch extends GameApplication implements InitVari {

    public enum EntityType {
        BALL,
        PADDLE,
        BRICK,
        POWERUP
    }

    public static Ball ball;
    public static Paddle paddle;
    public BrickManager bricks;
    public LifeManager lifeManager;
    public static PowerUpHandler powerHandler;
    public BallManager ballManager;
    public Score_control scoreControl;


    @Override
    public void initSettings(GameSettings settings) {
        GeneralInit.initScreenSettings(settings);
    }

    @Override
    protected void initGame() {
        getGameScene().addGameView(BACKGROUND);

        lifeManager = new LifeManager();
        lifeManager.init();

        scoreControl = new Score_control(0);
        scoreControl.initScore();

        ballManager = new BallManager();

        PaddleManager.initPaddle(SCREEN_WIDTH / 2, SCREEN_HEIGHT - PaddleVari.PADDLE_HEIGHT);
        paddle = PaddleManager.getCurrentPaddle();
        paddle.setType(EntityType.PADDLE);
        powerHandler = new PowerUpHandler(ballManager, lifeManager, paddle);

        ball = ballManager.spawn_InitBall();
        ball.startFalling();

        bricks = BrickManager.getInstance();
        bricks.spamBrick(4);
    }

    @Override
    protected void onUpdate(double tpf) {
        for (Ball b : ballManager.getBalls()) {
            b.update(tpf, paddle, bricks, lifeManager, scoreControl);
            b.IncreaseBallSpeed();

        }
        paddle.update();

    }

    @Override
    protected void initInput() {
        onKey(KeyCode.RIGHT, () -> paddle.moveRight());
        onKey(KeyCode.LEFT, () -> paddle.moveLeft());
        onKey(KeyCode.D, () -> bricks.clearAll());
        onKey(KeyCode.R, () -> bricks.spamBrick(4));
    }
}
