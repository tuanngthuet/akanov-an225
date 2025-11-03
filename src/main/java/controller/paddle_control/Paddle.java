package controller.paddle_control;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import controller.InitVari;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Paddle extends Entity implements InitVari, PaddleVari {

    private int PADDLE_X, PADDLE_Y;
    private int PADDLE_WIDTH, PADDLE_HEIGHT;
    private int PADDLE_SPEED;
    private PaddleType type;

    public Paddle(int x, int y, int width, int height, int speed, PaddleType type) {
        this.PADDLE_X = x;
        this.PADDLE_Y = y;
        this.PADDLE_WIDTH = width;
        this.PADDLE_HEIGHT = height;
        this.PADDLE_SPEED = speed;
        this.type = type;

        // Gán hình ảnh theo loại paddle
        Image image = getImageByType(type);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        getViewComponent().clearChildren();
        getViewComponent().addChild(imageView);

        getBoundingBoxComponent().clearHitBoxes();
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

    public int getPADDLE_HEIGHT() {
        return this.PADDLE_HEIGHT;
    }

    public void update() {
        if (getX() < 0) setX(0);
        if (getX() + PADDLE_WIDTH > SCREEN_WIDTH) setX(SCREEN_WIDTH - PADDLE_WIDTH);
    }

    public void setSpeed(int speed) {
        this.PADDLE_SPEED = speed;
    }

    public void setType(PaddleType type) {
        this.type = type;
        updateTexture();
    }

    public void setSize(int width, int height) {
        this.PADDLE_WIDTH = width;
        this.PADDLE_HEIGHT = height;
        updateTexture();

        // Cập nhật hitbox
        getBoundingBoxComponent().clearHitBoxes();
        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(width, height)));
    }

    private void updateTexture() {
        Image image = getImageByType(this.type);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(PADDLE_WIDTH);
        imageView.setFitHeight(PADDLE_HEIGHT);

        getViewComponent().clearChildren();
        getViewComponent().addChild(imageView);
    }

    public Image getImageByType(PaddleType type) {
        return switch (type) {
            case BUFF -> BUFF_PADDLE;
            case NERF -> NERF_PADDLE;
            case FROZEN -> FROZEN_PADDLE;
            default -> BASIC_PADDLE;
        };
    }

    public void resetToBasic() {
        setType(PaddleType.BASIC);
        setSize(BASIC_PAD_WIDTH, PADDLE_HEIGHT);
        setSpeed(BASIC_PAD_SPEED);
    }
}
