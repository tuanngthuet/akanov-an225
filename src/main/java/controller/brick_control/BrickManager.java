package controller.brick_control;

import com.almasb.fxgl.dsl.FXGL;
import controller.ScoreControl.Score_control;
import controller.powerup.PowerUp;
import javafx.scene.image.ImageView;
import view.Launch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.random;
import static controller.brick_control.BrickVari.BrickType.*;

public class BrickManager implements BrickVari{
    //Singleton pattern
    private static BrickManager singleton;
    private static Score_control scoreControl;
    private final List<Brick> brickList = new ArrayList<>();
    private boolean spawning = false;

    public List<Brick> getBrickList() {
        return brickList;
    }

    public void createBrick(int x, int y, BrickType brickType) {
        Brick brick = new Brick(x, y, brickType);
        brickList.add(brick);
        FXGL.getGameWorld().addEntity(brick);
        brick.setType(Launch.EntityType.BRICK);
    }

    public void removeBrick(Brick brick) {
        System.out.println("y");
        if (brick.getBrickType() == HARD) {
            brick.getViewComponent().removeChild(brick.getViewComponent().getChild(1, ImageView.class));
            brick.setType(NORMAL);
            return;
        }
        if (brick.getBrickType() == POWERUP) {
            getGameWorld().addEntity(new PowerUp(brick.getPosition()));
        }
        brick.breakAnimation();
        getGameWorld().removeEntity(brick);
        brickList.remove(brick);
        if (brickList.isEmpty()) {
            spawnBrick(scoreControl);
        }
    }

    public static BrickType getRandomBrickType() {
        int score = scoreControl.getCurrent_score_int();
        int r = FXGL.random(1, 100);

        int normalPercentage;
        int hardPercentage;

        if (score < 500) {
            normalPercentage = 70;
            hardPercentage = 15;
        } else if (score < 1000) {
            normalPercentage = 80;
            hardPercentage = 15;
        } else {
            normalPercentage = 70;
            hardPercentage = 25;
        }

        if (r < normalPercentage) return NORMAL;
        else if (r < normalPercentage + hardPercentage) return HARD;
        else return POWERUP;
    }

    private BrickManager() {
    }

    public static BrickManager getInstance() {
        if (singleton == null)
            singleton = new BrickManager();
        return singleton;
    }


    public static boolean isSpawn() {
        return random(0, 100) < 95;
    }

    //
    //áp dụng design pattern factory đề tạo ra nhiều kiểu spam khác nhau
    //
    public void spawnBrick(Score_control scoreControl) {
        this.scoreControl = scoreControl;
        if (!brickList.isEmpty()) return;
        System.out.println("y");
        BrickSpawner b = BrickSpawnerFactory.createSpawner(scoreControl);
        b.spawn(brickList);
    }
}