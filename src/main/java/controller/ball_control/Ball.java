package controller.ball_control;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import controller.InitVari;
import controller.brick_control.Brick;
import controller.brick_control.BrickManager;
import controller.brick_control.BrickVari;
import controller.paddle_control.PaddleVari;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;

import java.util.ArrayList;
import java.util.List;

public class Ball extends Entity implements InitVari, BrickVari, BallVari, PaddleVari{
    public enum BallType {
        NORMAL, HARDBALL, SPEEDUPBALL, MULTIBALL;
    }

    private boolean isHardBall = false;
    private double speed;
    private double directionX;
    private double directionY;
    private BallType type;
    private int Clock;
    private ImageView imageView;

    public Ball(double dirX, double dirY, BallType type) {
        this.type = type;
        this.speed = DEFAULT_SPEED;

        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        this.directionX = dirX / len;
        this.directionY = dirY / len;

        imageView = new ImageView(getImageByType(type));
        imageView.setFitWidth(BALL_RADIUS * 2);
        imageView.setFitHeight(BALL_RADIUS * 2);
        getViewComponent().addChild(imageView);

        this.addComponent(new BoundingBoxComponent());
        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(BALL_HITBOX, BALL_HITBOX)));
        this.addComponent(new CollidableComponent(true));
    }

    public void setType(BallType type) {
        if (imageView != null) {
            imageView.setImage(getImageByType(type));
        }
    }
    public void setHardBall(boolean hardBall) {
        isHardBall = hardBall;
    }

    public double getDirectionX() {
        return directionX;
    }

    public double getDirectionY() {
        return directionY;
    }
    public double getSpeed() {
        return speed;
    }

    public void setDirection(double dirX, double dirY) {
        double len = Math.sqrt(dirX * dirX + dirY * dirY);
        this.directionX = dirX / len;
        this.directionY = dirY / len;
    }

    public Image getImageByType(BallType type) {
        return switch (type) {
            case SPEEDUPBALL -> SPEEDUP_BALL;
            case MULTIBALL -> MULTI_BALL;
            case HARDBALL -> HARD_BALL;
            default -> NORMAL_BALL;
        };
    }

    public void setSpeed(double speed) {
        this.speed = speed;
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

    public void update(double tpf, Entity paddle, BrickManager bricks, LifeManager lifeManager) {
        double dx = directionX * speed * tpf * ADJUST_BALL_SPEED;
        double dy = directionY * speed * tpf * ADJUST_BALL_SPEED;
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
                if(!isHardBall) {
                    adjustDirectionAfterBrickHit(brick);
                }
            }
        }

        for (Brick brick : toRemove) {
            bricks.removeBrick(brick);
        }

        if (getY() > SCREEN_HEIGHT) {
            BallManager.handleBall_OutScreen(this,lifeManager,paddle);
            directionY = 1;
        }
    }

    public void IncreaseBallSpeed() {
        if (speed == MAX_SPEED) return;
        Clock++;
        if (Clock % CLOCK_TIME == 0) speed = speed + SPEED_RATE;
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
        return getBottomY() >= paddle.getY() &&
                getRightX() >= paddle.getX() &&
                getX() <= paddle.getRightX() &&
                getY() < paddle.getY();
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