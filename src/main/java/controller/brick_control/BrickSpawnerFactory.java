package controller.brick_control;

import controller.ScoreControl.Score_control;

import static com.almasb.fxgl.core.math.FXGLMath.random;

public class BrickSpawnerFactory {
    public static BrickSpawner createSpawner(Score_control scoreControl) {
         int score = scoreControl.getCurrent_score_int();
         if (score < 500) return new RectangleLayout();
         else if (score < 1000) return new TriangleLayout();
         return new CircleLayout();
    }
}
