package controller.brick_control;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.sql.Time;
import java.text.SimpleDateFormat;

public class Brick extends Entity implements BrickVari{
    private BrickType type;
    private int rowInSpriteSheet = FXGL.random(0, NORMAL_SPRITE_ROWS - 1);;
    private ImageView normaltexture;

    public BrickType getBrickType() {
        return type;
    }

    public void setType(BrickType type) {
        this.type = type;
    }

    public int getRowInSpriteSheet() {
        return rowInSpriteSheet;
    }

    public Brick(int x, int y, BrickType type) {
        this.type = type;
        setPosition(x, y);

        normaltexture = new ImageView(NORMAL_SPRITE);
        // Do size của cái sprite sheet là 32x16 còn size của brick là 64x32 nên set scale mới hiện đúng được
        normaltexture.setScaleX(2);
        normaltexture.setScaleY(2);

        normaltexture.setViewport(new Rectangle2D(
                0,
                rowInSpriteSheet * BRICK_HEIGHT / 2,
                BRICK_WIDTH / 2,
                BRICK_HEIGHT / 2
        ));
        getViewComponent().addChild(normaltexture);
        if (type == BrickType.POWERUP) powerUpBrickTexture();
        else if (type == BrickType.HARD) hardBricktexture();
        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(BRICK_WIDTH, BRICK_HEIGHT)));
        addComponent(new CollidableComponent(true));
    }

    public void powerUpBrickTexture() {
        Timeline timeline = new Timeline();
        ImageView img = new ImageView(POWERUP_SPRITE);
        img.setScaleY(2);
        img.setScaleX(2);

        img.setViewport(new Rectangle2D(
                0,
                0,
                (double) BRICK_WIDTH / 2,
                (double) BRICK_HEIGHT / 2
        ));

        getViewComponent().addChild(img);
        for (int frame = 0; frame < POWERUP_SPRITE_COLUMNS; frame++) {
            int frameIndex = frame;

            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis(130 * frame),
                    e -> {
                        img.setViewport(new Rectangle2D(
                                (double) (frameIndex * BRICK_WIDTH) / 2,
                                0,
                                (double) BRICK_WIDTH / 2,
                                (double) BRICK_HEIGHT / 2
                        ));
                    }
            ));
        }
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void hardBricktexture() {
        ImageView img = new ImageView(HARD_SPRITE);
        img.setScaleY(2);
        img.setScaleX(2);
        img.setTranslateX(-3);
        img.setTranslateY(-3);
        img.setViewport(new Rectangle2D( 0, 0, (double) (BRICK_WIDTH + 12) /2, (double) (BRICK_HEIGHT + 12) /2));
        getViewComponent().addChild(img);
    }

    public void breakAnimation(Runnable onFinished) {
//        // Mới có normal brick th :v
//        if (type != BrickType.NORMAL)
//            return;

        Timeline timeline = new Timeline();

        // Bỏ frame 0 (nguyên vẹn)
        for (int frame = 1; frame < NORMAL_SPRITE_COLUMNS; frame++) {
            int frameIndex = frame;

            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis(50 * frame),
                    e -> normaltexture.setViewport(new Rectangle2D(
                            frameIndex * BRICK_WIDTH / 2,
                            rowInSpriteSheet * BRICK_HEIGHT / 2,
                            BRICK_WIDTH / 2,
                            BRICK_HEIGHT / 2
                    ))
            ));
        }

        timeline.setOnFinished(e -> {
            if (onFinished != null)
                onFinished.run();
        });

        timeline.play();
    }
}