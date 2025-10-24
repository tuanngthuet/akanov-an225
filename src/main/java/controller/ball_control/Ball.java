package controller.ball_control;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import controller.InitVari;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.almasb.fxgl.entity.components.CollidableComponent;

public class Ball extends Entity implements InitVari {
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
        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(24, 24)));
        this.addComponent(new CollidableComponent(true));
    }
    public void startFalling() {
//        directionX = 0;
//        directionY = GRAVITY;
        double Ball_y = getY();
        double Ball_Velocity = 0;
        Ball_y += Ball_Velocity;
        Ball_Velocity += GRAVITY;
    }
    public void adjustDirectionAfterPaddleHit(Entity paddle) {
        double ballCenterX = this.getX() + this.getWidth() / 2;
        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
        double paddleWidth = paddle.getWidth();

        double gap = (ballCenterX - paddleCenterX) / (paddleWidth / 2);
//       gap = Math.max(-1, Math.min(1, gap));

        double maxBounceAngle = Math.toRadians(80);
        double bounceAngle = gap * maxBounceAngle;

        directionX = Math.sin(bounceAngle);
        directionY = -Math.cos(bounceAngle);
    }

    public void update(double tpf, Entity paddle) {
        double dx = directionX * speed * tpf * 60;
        double dy = directionY * speed * tpf * 60;
        this.translate(dx, dy);

        if (getX() <= 0) {
            setX(0);
            directionX *= Math.sin(Math.toRadians(60));
        } else if (getRightX() >= SCREEN_WIDTH) {
            setX(SCREEN_WIDTH - getWidth());
            directionX *= Math.sin(Math.toRadians(60));
        }
        if (getY() <= 0) {
            setY(0);
            directionY *= -1;
        }

        if (directionY > 0 && getBottomY() >= paddle.getY() &&
                getRightX() >= paddle.getX() &&
                getX() <= paddle.getRightX() &&
                getY() < paddle.getY()) {
                setY(paddle.getY() - getHeight());
                adjustDirectionAfterPaddleHit(paddle);
        }

        if (getY() > SCREEN_HEIGHT) {
            setPosition(400, 50);
            startFalling();
        }
    }
}
