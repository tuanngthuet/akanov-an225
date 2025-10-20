package view;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import controller.InitVari;
import controller.paddle_control.Paddle;
import controller.paddle_control.PaddleVari;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import controller.ball_control.Ball;
import controller.paddle_control.BasicPaddle;
import javafx.scene.paint.Color;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Launch extends GameApplication {


    public enum EntityType {
        BALL,
        PADDLE
    }

    private Ball ball;
    private BasicPaddle paddle;


    @Override
    public void initSettings(GameSettings settings) {
        GeneralInit.initScreenSettings(settings);
    }



    private Entity player;

    @Override
    protected void initGame() {

        ball = new Ball(600, 50, 4, 1, -1, Ball.BallType.NORMAL);
        ball.setType(EntityType.BALL);
        //What is this ??
        getGameWorld().addEntity(ball);

        paddle = new BasicPaddle(InitVari.width / 2, InitVari.height - PaddleVari.PADDLE_HEIGHT);
        paddle.setType(EntityType.PADDLE);
        // What is this ??
        getGameWorld().addEntity(paddle);
//        ball.startFalling();

    }

    @Override
    protected void onUpdate(double tpf) {
        ball.update(tpf, paddle);
        ball.startFalling();
        paddle.update();

    }

    protected void initInput() {
        onKey(KeyCode.RIGHT, () ->   paddle.moveRight());
        onKey(KeyCode.LEFT, ()  ->   paddle.moveLeft() );

    }


}