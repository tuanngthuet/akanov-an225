package view;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import controller.InitVari;
import controller.paddle_control.BasicPaddle;
import controller.paddle_control.Paddle;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

public class Launch extends GameApplication {
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(InitVari.width);
        settings.setHeight(InitVari.height);
        settings.setTitle(InitVari.GameName);
        settings.setVersion(InitVari.Version);
    }

    @Override
    protected void initInput() {
    }

    private void initPaddleInput() {
        onKey(KeyCode.A, () -> {
            if (paddle.getX() > 0) {
                paddle.moveLeft();
                inc("pixelsMoved", -paddle.getSpeed());
            }
        });

        onKey(KeyCode.D, () -> {
            if (paddle.getX() + paddle.getPADDLE_WIDTH() < InitVari.width) {
                paddle.moveRight();
                inc("pixelsMoved", paddle.getSpeed());
            }
        });

        onKey(KeyCode.LEFT, () -> {
            if (paddle.getX() > 0) {
                paddle.moveLeft();
            }
        });

        onKey(KeyCode.RIGHT, () -> {
            if (paddle.getX() + paddle.getPADDLE_WIDTH() < InitVari.width) {
                paddle.moveRight();
            }
        });
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("pixelsMoved", 0);
    }

    private Paddle paddle;

    @Override
    protected void initGame() {
        paddle = new BasicPaddle(InitVari.INIT_PADDLE_X, InitVari.INIT_PADDLE_Y);
        getGameWorld().addEntity(paddle);
        initInput();
        initPaddleInput();
    }

    @Override
    protected void initUI() {
        Text textPixels = new Text();
        textPixels.setTranslateX(50);
        textPixels.setTranslateY(100);
        textPixels.textProperty().bind(getWorldProperties().intProperty("pixelsMoved").asString("Pixels moved: %d"));
        getGameScene().addUINode(textPixels);
    }
}
