package controller.powerup;

import controller.InitVari;
import javafx.scene.image.Image;

import java.util.Objects;

public interface PowerUpVari {
    enum PowerType {
        LASER,
        EXPAND,
        SHRINK,
        MOREBALL
    }

    int POWER_SIZE = 30;

    int POWER_FALLING_SPEED = 70;

    Image POWERUP = new Image(Objects.requireNonNull(InitVari.class.getResource("/assets/textures/powerup/powerup.png")).toExternalForm());
}
