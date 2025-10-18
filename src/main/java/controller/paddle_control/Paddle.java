package controller.paddle_control;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public abstract class Paddle extends Entity {

    public enum PaddleType {
        BASIC, BUFF, NERF, FROZEN;
    }

    private int PADDLE_X, PADDLE_Y;
    private int PADDLE_WIDTH, PADDLE_HEIGHT;
    private int PADDLE_SPEED;
    private PaddleType type;


    public Paddle(int x, int y, int width, int height,int speed, PaddleType type, Color color) {
        this.PADDLE_X = x;
        this.PADDLE_Y = y;
        this.PADDLE_WIDTH = width;
        this.PADDLE_HEIGHT = height;
        this.PADDLE_SPEED = speed;
        this.type = type;

        getViewComponent().addChild(new Rectangle(width, height, color));

        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(width, height)));

        addComponent(new CollidableComponent(true));

        setPosition(x, y);
    }

    public void moveLeft() {
        translateX(-PADDLE_SPEED);
    }

    public void moveRight() {
        translateX(PADDLE_SPEED);
    }

    public double getSpeed() {
        return this.PADDLE_SPEED;
    }

    public PaddleType getPaddleType() {
        return this.type;
    }

    public int getPADDLE_WIDTH() {
        return this.PADDLE_WIDTH;
    }
}
