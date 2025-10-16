package Model;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import controller.InitVari;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;
import static com.almasb.fxgl.dsl.FXGLForKtKt.random;

public class BrickManager extends Entity {
    private final List<Brick> brickList = new ArrayList<>();

    public void createBrick(int x, int y, Brick.BrickType brickType) {
        Brick brick = new Brick(x, y, brickType);
        getGameWorld().addEntity(brick);
        brickList.add(brick);
    }

    public void clearAll() {
        for (Brick brick : brickList) {
            getGameWorld().removeEntity(brick);
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

        int bricksPerRow = (InitVari.width - InitVari.BRICK_GAP) / (InitVari.BRICK_WIDTH + InitVari.BRICK_GAP);

        double totalWidth = bricksPerRow * InitVari.BRICK_WIDTH + (bricksPerRow - 1) * InitVari.BRICK_GAP;
        double startX = (InitVari.width - totalWidth) / 2;

        double startY = InitVari.BRICK_GAP;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < bricksPerRow; col++) {
                double x = startX + col * (InitVari.BRICK_WIDTH + InitVari.BRICK_GAP); // ngang
                double y = startY + row * (InitVari.BRICK_HEIGHT + InitVari.BRICK_GAP); // dọc
                Brick.BrickType type = getRandomBrickType();

                createBrick((int) x, (int) y, type);
            }
        }
    }
}

class Brick extends Entity {

    public enum BrickType {
        NORMAL, HARD, POWERUP
    }

    private BrickType type;

    public BrickType getBrickType() {
        return type;
    }

    public Brick(int x, int y, BrickType type) {
        this.type = type;
        Color color = switch (type) {
            case HARD -> Color.BLACK;
            case NORMAL -> Color.BROWN;
            case POWERUP -> Color.YELLOW;
        };
        setPosition(x, y);
        getViewComponent().addChild(
                new Rectangle(InitVari.BRICK_WIDTH, InitVari.BRICK_HEIGHT, color)
        );
        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(InitVari.BRICK_WIDTH, InitVari.BRICK_HEIGHT)));
        addComponent(new CollidableComponent(true));
    }
}
