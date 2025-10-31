package controller.brick_control;

import com.almasb.fxgl.dsl.FXGL;
import controller.InitVari;
import controller.powerup.PowerUp;
import javafx.scene.image.ImageView;
import view.Launch;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.random;
import static controller.brick_control.BrickVari.BrickType.*;

public class BrickManager implements BrickVari{
    //Singleton pattern
    private static BrickManager singleton;
    private final List<Brick> brickList = new ArrayList<>();

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
    }

    public void clearAll() {
        if (brickList.isEmpty()) return;
        for (Brick brick : new ArrayList<>(brickList)) {
            removeBrick(brick);
        }
    }

    private BrickType getRandomBrickType() {
        double r = random(0, 100);

        if (r < 80) return NORMAL; // 80% chance of Normal Brick
        else if (r < 95) return HARD; // 15% Hard
        else return POWERUP; // 5% PowerUp
    }

    private BrickManager() {
    }

    public static BrickManager getInstance() {
        if (singleton == null)
            singleton = new BrickManager();
        return singleton;
    }


    public boolean randomBrick() {
        return random(0, 100) < 80;
    }

    //
    //áp dụng design pattern factory đề tạo ra nhiều kiểu spam khác nhau
    //
    public void spamBrick(int numRows) {
        if (!brickList.isEmpty()) return;
        int bricksPerRow = (InitVari.SCREEN_WIDTH - BRICK_GAP) / (BRICK_WIDTH + BRICK_GAP);

        double totalWidth = bricksPerRow * BRICK_WIDTH + (bricksPerRow - 1) * BRICK_GAP;
        double startX = (InitVari.SCREEN_WIDTH - totalWidth) / 1.25;

        double startY = BRICK_GAP * 2;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < bricksPerRow; col++) {
                double x = startX + col * (BRICK_WIDTH + BRICK_GAP); // ngang
                double y = startY + row * (BRICK_HEIGHT + BRICK_GAP * 1.5); // dọc
                Brick.BrickType type = getRandomBrickType();

                if (randomBrick()) createBrick((int) x, (int) y, type);
            }
        }
    }

}

