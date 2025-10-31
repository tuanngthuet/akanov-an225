package controller.brick_control;

import static com.almasb.fxgl.core.math.FXGLMath.random;

public class BrickSpawnerFactory {
    public static BrickSpawner createSpawner() {
        int random = random(0, 1);
         return switch (random) {
            case 0 -> new CircleLayout();
            case 1 -> new RectangleLayout();
            default -> new TriangleLayout();
         };
    }
}
