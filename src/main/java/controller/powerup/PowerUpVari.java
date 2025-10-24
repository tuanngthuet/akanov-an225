package controller.powerup;

public interface PowerUpVari {
    enum PowerType {
        LASER,
        EXPAND,
        SHRINK,
        MOREBALL
    }

    int POWER_SIZE = 30;

    int POWER_FALLING_SPEED = 2 * 60;
}
