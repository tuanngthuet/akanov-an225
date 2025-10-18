package model;

import com.almasb.fxgl.entity.Entity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;

public class Paddle extends Entity {
    private double speed;

    public Paddle(double speed, double x, double y, double width, double height) {
        this.speed = speed;
        this.setPosition(x, y);
        Rectangle rect = new Rectangle(width, height, Color.GOLD);
        this.getViewComponent().addChild(rect);
        this.addComponent(new BoundingBoxComponent());
        this.addComponent(new CollidableComponent(true));
    }

    public void moveLeft() {
        this.translateX(-speed);
        if (getX() < 0) setX(0);
    }

    public void moveRight() {
        this.translateX(speed);
        if (getRightX() > 800) setX(800 - getWidth());
    }
}
