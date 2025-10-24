package controller.brick_control;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Brick extends Entity implements BrickVari{
    private BrickType type;
    private int rowInSpriteSheet;
    private int columnInSpriteSheet;
    private ImageView texture;

    public BrickType getBrickType() {
        return type;
    }

    public Brick(int x, int y, BrickType type) {
        this.type = type;
//        rowInSpriteSheet = FXGL.random(0, switch (type) {
//                    case POWERUP -> POWERUP_SPRITE_ROWS;
//                    case NORMAL -> NORMAL_SPRITE_ROWS;
//                    case HARD -> HARD_SPRITE_ROWS;
//                } - 1);
        rowInSpriteSheet = FXGL.random(0, NORMAL_SPRITE_ROWS - 1);
//        columnInSpriteSheet = switch (type) {
//            case POWERUP -> POWERUP_SPRITE;
//            case NORMAL -> NORMAL_SPRITE_COLUMNS;
//            case HARD -> HARD_SPRITE;
//        }
        columnInSpriteSheet = NORMAL_SPRITE_COLUMNS;
        setPosition(x, y);

//        ImageView texture = new ImageView(switch (type) {
//            case POWERUP -> POWERUP_SPRITE;
//            case NORMAL -> NORMAL_SPRITE;
//            case HARD -> HARD_SPRITE;
//        });
        texture = new ImageView(NORMAL_SPRITE);
        // Do size của cái sprite sheet là 32x16 còn size của brick là 64x32 nên set scale mới hiện đúng được
        texture.setScaleX(2);
        texture.setScaleY(2);

        texture.setViewport(new Rectangle2D(
                0,
                rowInSpriteSheet * BRICK_HEIGHT / 2,
                BRICK_WIDTH / 2,
                BRICK_HEIGHT / 2
        ));
        getViewComponent().addChild(texture);
        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(BRICK_WIDTH, BRICK_HEIGHT)));
        addComponent(new CollidableComponent(true));
    }

    public void breakAnimation(Runnable onFinished) {
//        // Mới có normal brick th :v
//        if (type != BrickType.NORMAL)
//            return;

        Timeline timeline = new Timeline();

        // Bỏ frame 0 (nguyên vẹn)
        for (int frame = 1; frame < columnInSpriteSheet; frame++) {
            int frameIndex = frame;

            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis(50 * frame),
                    e -> texture.setViewport(new Rectangle2D(
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
