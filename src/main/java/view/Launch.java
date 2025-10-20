package view;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import controller.InitVari;
import controller.paddle_control.Paddle;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import controller.Ball;
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
        settings.setWidth(InitVari.width);
        settings.setHeight(InitVari.height);
        settings.setTitle(InitVari.GameName);
        settings.setVersion(InitVari.Version);
    }
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    private Entity player;
    @Override
    protected void initGame() {

        ball = new Ball(600, 50, 4, 1, -1, Ball.BallType.NORMAL);
        ball.setType(EntityType.BALL);
        getGameWorld().addEntity(ball);

        paddle = new BasicPaddle(550,550);
        paddle.setType(EntityType.PADDLE);
        getGameWorld().addEntity(paddle);
//        ball.startFalling();
    }
    @Override
    protected void onUpdate(double tpf) {
        ball.update(tpf, paddle);
        ball.startFalling();
        paddle.update();
        ball.IncreaseBallSpeed();
    }
    protected void initInput() {
        onKey(KeyCode.LEFT, () -> paddle.moveLeft());
        onKey(KeyCode.RIGHT, () -> paddle.moveRight());
    }
//    @Override
//    protected void initUI() {
//        Text textPixels = new Text();
//        textPixels.setTranslateX(50); // x = 50
//        textPixels.setTranslateY(100); // y = 100
//
//        textPixels.textProperty().bind(getWorldProperties().intProperty("pixelsMoved").asString());
//
//        getGameScene().addUINode(textPixels); // add to the scene graph
//    }
}