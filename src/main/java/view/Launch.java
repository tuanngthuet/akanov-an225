package view;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import controller.InitVari;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import com.almasb.fxgl.physics.CollisionHandler;
import model.Ball;
import model.Paddle;
import java.util.Map;
import com.almasb.fxgl.physics.PhysicsComponent;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Launch extends GameApplication {
    public enum EntityType {
        BALL,
        PADDLE
    }
    private Paddle paddle;
    private Ball ball;
    @Override
    public void initSettings(GameSettings settings) {
        settings.setWidth(InitVari.width);
        settings.setHeight(InitVari.height);
        settings.setTitle(InitVari.GameName);
        settings.setVersion(InitVari.Version);
    }
    @Override
    protected void initInput() {
        onKey(KeyCode.LEFT, () -> paddle.translateX(-5));
        onKey(KeyCode.RIGHT, () -> paddle.translateX(5));
    }
    protected void onUpdate(double tpf) {
        ball.onUpdate(tpf, paddle);
    }
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    private Entity player;

    @Override
    protected void initGame() {
        paddle = new Paddle(300, 550, 500,80,30);  // speed=5, tọa độ (300, 550)
        paddle.setType(Launch.EntityType.PADDLE);
        getGameWorld().addEntity(paddle);

        ball = new Ball(600, 50, 4, 1, -1, Ball.BallType.NORMAL);
        ball.setType(EntityType.BALL);
        getGameWorld().addEntity(ball);

        ball.startFalling();
    }
    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50); // x = 50
        textPixels.setTranslateY(100); // y = 100

        textPixels.textProperty().bind(getWorldProperties().intProperty("pixelsMoved").asString());

        getGameScene().addUINode(textPixels); // add to the scene graph
    }
}