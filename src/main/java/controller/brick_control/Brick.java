package controller.brick_control;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import controller.InitVari;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Brick extends Entity {

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
