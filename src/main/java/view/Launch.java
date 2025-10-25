package view;
//Routing import

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import controller.InitVari;
import controller.ball_control.Ball;
import controller.brick_control.BrickManager;
import controller.paddle_control.BasicPaddle;
import controller.paddle_control.BuffPaddle;
import controller.paddle_control.Paddle;
import controller.paddle_control.PaddleVari;
//Java import
import javafx.scene.input.KeyCode;
//Game library import
import java.time.Duration;
import java.time.LocalTime;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static com.almasb.fxgl.dsl.FXGL.onKey;

public class Launch extends GameApplication {


    public enum EntityType {
        BALL,
        PADDLE
    }

    private Ball ball;
    private Paddle paddle;
    private LocalTime GameStartTime;

    private int time_second_counter = 0;
    private boolean paddle_switch_flag = false;


    @Override
    public void initSettings(GameSettings settings) {
        GeneralInit.initScreenSettings(settings);
    }


    private Entity player;

    @Override
    protected void initGame() {

        GameStartTime = LocalTime.now();

        ball = new Ball(600, 50, 4, 1, -1, Ball.BallType.NORMAL);
        ball.setType(EntityType.BALL);
        getGameWorld().addEntity(ball);

        paddle = new BasicPaddle(InitVari.width / 2, InitVari.height - PaddleVari.PADDLE_HEIGHT);
        paddle.setType(EntityType.PADDLE);
        getGameWorld().addEntity(paddle);

        BrickManager bricks = new BrickManager(3);

    }

    @Override
    protected void onUpdate(double tpf) {

            ball.update(tpf, paddle);
            ball.startFalling();
            paddle.update();


        time_second_counter = LocalTime.now().toSecondOfDay() - GameStartTime.toSecondOfDay();

        if (time_second_counter == 2 && !paddle_switch_flag) {
            paddle_switch_flag = true;

            int current_paddle_x = (int) paddle.getX();
            int current_paddle_y = (int) paddle.getY();

            getGameWorld().removeEntity(paddle);

            paddle = (BuffPaddle) new BuffPaddle(current_paddle_x, current_paddle_y);


            getGameWorld().addEntity(paddle);
            System.out.println("Paddle switch success !");

        }


    }

    protected void initInput() {
        onKey(KeyCode.RIGHT, () -> paddle.moveRight());
        onKey(KeyCode.LEFT, () -> paddle.moveLeft());


    }


}