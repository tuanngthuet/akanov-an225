package controller;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;

public class Ball extends Entity {
    public enum BallType {
        NORMAL, HARD, POWERUP
    }

    private double speed;
    private double directionX;
    private double directionY;
    private BallType type;

    public BallType getBallType() {
        return type;
    }

    public Ball(double x, double y, double speed, double dirX, double dirY, BallType type) {
        this.setPosition(x, y);
        this.type = type;

        Color color = switch (type) {
            case NORMAL -> Color.RED;
            case HARD -> Color.GREEN;
            case POWERUP -> Color.BLUE;
        };

        this.speed = speed;

        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        this.directionX = dirX / len;
        this.directionY = dirY / len;

        this.getViewComponent().addChild(new Circle(12, color));
        this.addComponent(new BoundingBoxComponent());
        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(10, 10)));
        this.addComponent(new CollidableComponent(true));
    }

    public void startFalling() {
        directionY = 1;
        directionX = 0;
    }

    public void adjustDirectionAfterPaddleHit(Entity paddle) {
        double ballCenterX = getX() + getWidth() / 2;
        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;

        double gap = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2);

        double bounceAngle = gap * Math.toRadians(80);

        directionX = Math.sin(bounceAngle);
        directionY = -Math.cos(bounceAngle);
    }

    public void update(double tpf, Entity paddle,Entity brick) {
        double dx = directionX * speed * tpf * 60;
        double dy = directionY * speed * tpf * 60;
        this.translate(dx, dy);
        if (getX() <= 0) {
            setX(0);
            directionX *= -1;
        } else if (getRightX() >= InitVari.width) {
            setX(InitVari.width - getWidth());
            directionX *= -Math.sin(Math.toRadians(60));
        } else if (getY() <= 0) {
            setY(0);
            directionY *= -1;
        }
        if(Check_PaddleHit(paddle)) {
            setY(paddle.getY() - getHeight());
            adjustDirectionAfterPaddleHit(paddle);
        }
        if(Check_BrickHitUp(brick)) {
            setY(brick.getY() - getHeight());
            adjustDirectionAfterBrickHit(brick);
        }
        if (getY() > InitVari.height) {
            setPosition(400, 50);
        }
    }

    public void IncreaseBallSpeed() {
        if (speed == 6) return;
        InitVari.Clock++;
        if (InitVari.Clock % 10 == 0) speed = speed + 0.01;
    }

    public void adjustDirectionAfterBrickHit(Entity brick) {
        double Ball_CenterX = getX() + getWidth() / 2;
        double Brick_CenterX = brick.getX() + brick.getWidth() / 2;

        double gap = (Ball_CenterX - Brick_CenterX) / (brick.getWidth() / 2);
        double bounce_angle = gap * Math.toRadians(80);

        directionX = Math.sin(bounce_angle);
        directionY = -Math.cos(bounce_angle);
    }
    public boolean Check_PaddleHit(Entity paddle) {
        if (getBottomY() >= paddle.getY() &&
                getRightX() >= paddle.getX() &&
                getX() <= paddle.getRightX() &&
                getY() < paddle.getY()) {
            return true;
        }
        return false;
    }
    public boolean Check_BrickHitUp(Entity brick) {
        if(getRightX() >= brick.getX() && getX() <= brick.getRightX() && getY() >=
                brick.getY() && getY() <= brick.getBottomY()) {
            return true;
        }
        return false;
    }
}
