package controller;

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

    double GRAVITY = 1;

    Font TITLE_FONT =
            Font.loadFont(Objects.requireNonNull(InitVari.class
                    .getResource("/assets/fonts/font.ttf")).toExternalForm(), 60);
    Font TEXT_FONT =
            Font.loadFont(Objects.requireNonNull(InitVari.class
                    .getResource("/assets/fonts/font.ttf")).toExternalForm(), 20);
}