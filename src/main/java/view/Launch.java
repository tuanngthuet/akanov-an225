package view;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import controller.InitVari;
import controller.ScoreControl.Score_control;
import controller.ball_control.*;
import controller.brick_control.BrickManager;
import controller.paddle_control.*;
import controller.powerup.PowerUp;
import controller.sound_control.AudioManager;
import controller.sound_control.SoundVari;
import controller.user.User;
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
        getGameScene().addGameView(new GameView(BACKGROUND, -1000));

        AudioManager.MUSIC.setVolume(SoundVari.DEFAULT_VOLUME);
        AudioManager.MUSIC.playSound(SoundVari.THEME_SOUND,true);

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

        BrickManager.getInstance().getBrickList().clear();
        BrickManager.getInstance().spawnBrick(scoreControl);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().addCollisionHandler(new CollisionHandler(
                Launch.EntityType.POWERUP, Launch.EntityType.PADDLE) {
            @Override
            protected void onCollisionBegin(Entity pow, Entity paddle) {
                PowerUp power = (PowerUp) pow;
                if (!power.isActive())
                    return;

                PowerUp.textAnimation(power);
                power.activated();
                AudioManager.SFX.playSound(SoundVari.SOUND_POWER_UP);
                power.removeFromWorld();
            }
        });
    }

    @Override
    protected void onUpdate(double tpf) {
        for (Ball b : ballManager.getBalls()) {
            b.update(tpf, paddle, BrickManager.getInstance(), lifeManager, scoreControl);
            b.IncreaseBallSpeed();
        }
        paddle.update();
    }

    @Override
    protected void initInput() {
        onKey(KeyCode.RIGHT, () -> paddle.moveRight());
        onKey(KeyCode.LEFT, () -> paddle.moveLeft());
    }
}
