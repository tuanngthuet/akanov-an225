package controller.brick_control;

import com.almasb.fxgl.entity.Entity;
import controller.InitVari;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.random;

public class BrickManager extends Entity {
    private final List<Brick> brickList = new ArrayList<>();

    public List<Brick> getBrickList() {
        return brickList;
    }

    public void createBrick(int x, int y, Brick.BrickType brickType) {
        Brick brick = new Brick(x, y, brickType);
        brickList.add(brick);
    }

    public void removeBrick(Brick brick) {
        brick.breakAnimation(() -> {
            getGameWorld().removeEntity(brick);
            brickList.remove(brick);
        });
    }

    public void clearAll() {
        for (Brick brick : brickList) {
            brick.breakAnimation(() -> {
                getGameWorld().removeEntity(brick);
            });
        }
        brickList.clear();
    }

    private Brick.BrickType getRandomBrickType() {
        double r = random(0, 100);

        if (r < 80) return Brick.BrickType.NORMAL; // 80% chance of Normal Brick
        else if (r < 95) return Brick.BrickType.HARD; // 15% Hard
        else return Brick.BrickType.POWERUP; // 5% PowerUp
    }

    public BrickManager(int numRows) {

        int bricksPerRow = (InitVari.width - BrickVari.BRICK_GAP) / (BrickVari.BRICK_WIDTH + BrickVari.BRICK_GAP);

        double totalWidth = bricksPerRow * BrickVari.BRICK_WIDTH + (bricksPerRow - 1) * BrickVari.BRICK_GAP;
        double startX = (InitVari.width - totalWidth) / 2;

        double startY = BrickVari.BRICK_GAP * 2;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < bricksPerRow; col++) {
                double x = startX + col * (BrickVari.BRICK_WIDTH + BrickVari.BRICK_GAP); // ngang
                double y = startY + row * (BrickVari.BRICK_HEIGHT + BrickVari.BRICK_GAP); // dọc
                Brick.BrickType type = getRandomBrickType();

                createBrick((int) x, (int) y, type);
            }
        }
    }
}

