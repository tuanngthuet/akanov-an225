package controller;

import com.almasb.fxgl.app.scene.GameView;
import com.almasb.fxgl.dsl.FXGL;
import controller.brick_control.BrickVari;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.util.Objects;

public interface InitVari {
    /**
     * Set up Screen size variables
     */
    int SCREEN_HEIGHT = 720;
    int SCREEN_WIDTH = 960;
    //  Define Game name: version 0.1
    String GAME_NAME = "Akanov-an225";
    String VERSION = "0.2";

//    /**
//     * Init position of the paddle
//     */
//    int INIT_PADDLE_X = 300;
//    int INIT_PADDLE_Y = 550;

    GameView BACKGROUND = new GameView(
            new ImageView( new Image(Objects.requireNonNull(InitVari.class.getResource("/assets/textures/background/background.png").toExternalForm()))),
            -1000
    );

    Font TITLE_FONT =
            Font.loadFont(Objects.requireNonNull(InitVari.class
                    .getResource("/assets/fonts/font.ttf")).toExternalForm(), 60);
    Font TEXT_FONT =
            Font.loadFont(Objects.requireNonNull(InitVari.class
                    .getResource("/assets/fonts/font.ttf")).toExternalForm(), 20);
}