package controller.powerup;

import controller.InitVari;
import javafx.scene.image.Image;

import java.util.Objects;

public interface PowerUpVari {
    enum PowerType {
        MULTIBALL,
        HARDBALL,
        SPEEDUPBALL,
        EXTRALIFE,
        // viết thêm
    }

    int POWER_SIZE = 30;

    int POWER_FALLING_SPEED = 80;

    Image POWERUP = new Image(
            Objects.requireNonNull(InitVari.class.getResource("/assets/textures/powerup/powerup.png")).toExternalForm());
}
