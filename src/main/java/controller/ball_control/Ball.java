package controller.ball_control;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import controller.InitVari;
import controller.brick_control.Brick;
import controller.brick_control.BrickManager;
import controller.brick_control.BrickVari;
import controller.paddle_control.PaddleVari;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;


import java.util.ArrayList;
import java.util.List;

import static controller.paddle_control.PaddleVari.BASIC_PAD_WIDTH;


public class Ball extends Entity implements InitVari, BrickVari, BallVari{
    public enum BallType {
        NORMAL, HARD, POWERUP
    }

    private double speed;
    private double directionX;
    private double directionY;
    private BallType type;
    private int Clock;

    public BallType getBallType() {
        return type;
    }

    public Ball(double dirX, double dirY, BallType type) {
        this.setX((double) SCREEN_WIDTH / 2);
        this.setY(SCREEN_HEIGHT - PaddleVari.PADDLE_HEIGHT - 50);
        this.type = type;

        Color color = switch (type) {
            case NORMAL -> Color.RED;
            case HARD -> Color.GREEN;
            case POWERUP -> Color.BLUE;
        };

        this.speed = 4;

        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        this.directionX = dirX / len;
        this.directionY = dirY / len;

        this.getViewComponent().addChild(new Circle(BALL_RADIUS, color));
        this.addComponent(new BoundingBoxComponent());
        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(BALL_HITBOX, BALL_HITBOX)));
        this.addComponent(new CollidableComponent(true));
    }

    public void startFalling() {
        directionY = 1;
        directionX = 0;
    }

    public void adjustDirectionAfterPaddleHit(Entity paddle) {
        double ballCenterX = getX() + BALL_RADIUS / 2;
        double paddleCenterX = paddle.getX() + (double) BASIC_PAD_WIDTH / 2;

        double gap = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2);

        double bounceAngle = gap * Math.toRadians(MAX_ANGLE);

        directionX = Math.sin(bounceAngle);
        directionY = -Math.cos(bounceAngle);
    }

    public void update(double tpf, Entity paddle, BrickManager bricks) {
        double dx = directionX * speed * tpf * 60;
        double dy = directionY * speed * tpf * 60;
        this.translate(dx, dy);
        if (getX() <= 0) {
            setX(0);
            directionX *= -1;
        } else if (getRightX() >= SCREEN_WIDTH) {
            setX(SCREEN_WIDTH - getWidth());
            directionX *= -Math.sin(Math.toRadians(MAX_ANGLE));
        } else if (getY() <= 0) {
            setY(0);
            directionY *= -1;
        }
        if(Check_PaddleHit(paddle)) {
            setY(paddle.getY() - getHeight());
            adjustDirectionAfterPaddleHit(paddle);
        }
        List<Brick> toRemove = new ArrayList<>();

        for (Brick brick : bricks.getBrickList()) {
            if (Check_BrickHit(brick)) {
                toRemove.add(brick);
                adjustDirectionAfterBrickHit(brick);
            }
        }

        for (Brick brick : toRemove) {
            bricks.removeBrick(brick);
        }

        if (getY() > SCREEN_HEIGHT) {
            setPosition(400, 50);
        }
    }

    public void IncreaseBallSpeed() {
        if (speed == 6) return;
        Clock++;
        if (Clock % 10 == 0) speed = speed + 0.01;
    }

    // tham khảo - axis aligned bounding box
    public void adjustDirectionAfterBrickHit(Entity brick) {
        double overlapLeft   = getRightX() - brick.getX(); // va chạm phần gạch bên trái
        double overlapRight  = brick.getRightX() - getX(); // va chạm phần gạch bên phải
        double overlapTop    = getBottomY() - brick.getY(); // va chạm phần gạch bên trên
        double overlapBottom = brick.getBottomY() - getY(); // va chạm phần gạch bên dưới

        // xem là phần va chạm nào là nhỏ nhất để đổi chiều đi của quá bóng
        double minOverlap = Math.min(Math.min(overlapLeft, overlapRight),
                Math.min(overlapTop, overlapBottom));

        if (minOverlap == overlapLeft || minOverlap == overlapRight) {
            directionX *= -1;
        }
        else {
            directionY *= -1;
        }
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
    // tham khảo Circle - Rectangle collision detection
    public boolean Check_BrickHit(Entity brick) {
        double center_ballX = getX() + BALL_RADIUS;
        // nearestX - the closest coordinates of BrickX to BallCenterX
        double nearestX = Math.max(brick.getX(), Math.min(center_ballX, brick.getX() + BRICK_WIDTH));
        double center_ballY = getY() + BALL_RADIUS;
        // nearestY - the closest coordinates of BrickY to BallCenterY
        double nearestY = Math.max(brick.getY(), Math.min(center_ballY, brick.getY() + BRICK_HEIGHT));

        // caculate the distance between two points
        double deltaX = nearestX - center_ballX;
        double deltaY = nearestY - center_ballY;

        double distance = (deltaX * deltaX) + (deltaY * deltaY);
        return distance <= Math.pow(BALL_RADIUS, 2);
    }
}