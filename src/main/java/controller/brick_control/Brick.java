package controller.brick_control;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class Brick extends Entity implements BrickVari{
    private BrickType type;
    private final int rowInSpriteSheet = FXGL.random(0, NORMAL_SPRITE_ROWS - 1);

    public BrickType getBrickType() {
        return type;
    }

    public void setType(BrickType type) {
        this.type = type;
    }

    public Brick(int x, int y, BrickType type) {
        this.type = type;
        setPosition(x, y);

        ImageView normaltexture = new ImageView(NORMAL_SPRITE);
        normaltexture.setFitHeight(BRICK_HEIGHT);
        normaltexture.setFitWidth(BRICK_WIDTH);
        normaltexture.setViewport(new Rectangle2D(
                0,
                (double) (rowInSpriteSheet * BRICK_HEIGHT) / 2,
                (double) BRICK_WIDTH / 2,
                (double) BRICK_HEIGHT / 2
        ));
        getViewComponent().addChild(normaltexture);
        if (type == BrickType.POWERUP) powerUpBrickTexture();
        else if (type == BrickType.HARD) hardBricktexture();
        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(BRICK_WIDTH, BRICK_HEIGHT)));
//        addComponent(new CollidableComponent(true));
    }

    public void powerUpBrickTexture() {
        Timeline timeline = new Timeline();
        ImageView img = new ImageView(POWERUP_SPRITE);
        img.setFitWidth(BRICK_WIDTH);
        img.setFitHeight(BRICK_HEIGHT);

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
        img.setFitWidth(BRICK_WIDTH + 6);
        img.setFitHeight(BRICK_HEIGHT + 6);
        img.setTranslateX(-3);
        img.setTranslateY(-3);
        img.setViewport(new Rectangle2D( 0, 0, (double) (BRICK_WIDTH + 12) /2, (double) (BRICK_HEIGHT + 12) /2));
        getViewComponent().addChild(img);
    }

    public void breakAnimation() {
        ImageView tx = new ImageView(NORMAL_SPRITE);
        tx.setFitWidth(BRICK_WIDTH);
        tx.setFitHeight(BRICK_HEIGHT);

        tx.setViewport(new Rectangle2D(
                0,
                (double) (rowInSpriteSheet * BRICK_HEIGHT) / 2,
                (double) BRICK_WIDTH / 2,
                (double) BRICK_HEIGHT / 2
        ));
        Entity breaked = FXGL.entityBuilder()
                .at(getPosition())
                .view(tx)
                .zIndex(100)
                .buildAndAttach();
        Timeline timeline = new Timeline();

        for (int frame = 1; frame < NORMAL_SPRITE_COLUMNS; frame++) {
            int frameIndex = frame;

            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis(50 * frame),
                    e -> tx.setViewport(new Rectangle2D(
                            (frameIndex * BRICK_WIDTH) / 2.0,
                            (rowInSpriteSheet * BRICK_HEIGHT) / 2.0,
                            BRICK_WIDTH / 2.0,
                            BRICK_HEIGHT / 2.0
                    ))
            ));
        }
        timeline.setOnFinished(e -> {
            FXGL.runOnce(breaked::removeFromWorld, Duration.ZERO);
        });

        timeline.play();
    }
}