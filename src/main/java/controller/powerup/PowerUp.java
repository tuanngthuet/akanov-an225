package controller.powerup;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import controller.ball_control.BallVari;
import controller.ball_control.Ball;
import controller.ball_control.PowerUpHandler;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.Launch;

import static com.almasb.fxgl.core.math.FXGLMath.random;

public class PowerUp extends Entity implements PowerUpVari, BallVari {
    private final PowerType powerType = PowerType.values()[FXGL.random(0, PowerType.values().length - 1)];

    public PowerUp(Point2D position) {
        setPosition(position);

        getViewComponent().addChild(new ImageView(POWERUP));
        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(POWER_SIZE, POWER_SIZE)));
        addComponent(new PhysicComponent());
        addComponent(new CollidableComponent(true));
        setType(Launch.EntityType.POWERUP);
    }

    // viết function vo
    public void activated() {
        if(Launch.powerHandler != null) {
            Launch.powerHandler.Pick_Up(powerType);
        }
    }

    public static void textAnimation(Entity po) {
        PowerUp power = (PowerUp) po;
        Text text = getPowerUpText(power);
        text.setTranslateX(power.getX() - text.getLayoutBounds().getWidth() / 1.5);
        text.setTranslateY(power.getY() - random(70, 200));
        FXGL.getGameScene().addUINode(text);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.1), text);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.1), text);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(0.2));

        SequentialTransition seq = new SequentialTransition(fadeIn, fadeOut);
        seq.setCycleCount(3);

        seq.setOnFinished(e -> FXGL.getGameScene().removeUINode(text));

        seq.play();
    }

    private static Text getPowerUpText(PowerUp power) {
        Text text = new Text(
                switch (power.powerType) {
                    case MULTIBALL -> "MORE BALL !!!";
                    case HARDBALL -> "SUPER BALL !!!";
                    case SPEEDUPBALL -> "SPEED UP !!!";
                    case EXTRALIFE -> "+1 LIFE";
                    case BUFF -> "BUFF";
                    case NERF -> "NERF";
                    case FROZEN -> "FROZEN";
                }
        );
        text.setFill(Color.web("#DAA520")); // Goldenrod
        text.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#708090"));
        shadow.setRadius(20);
        text.setEffect(shadow);
        text.setOpacity(0);
        return text;
    }
}

class PhysicComponent extends Component implements PowerUpVari{
    private static boolean hasCollision = false;

    @Override
    public void onAdded() {
        if (!hasCollision) {
            FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(
                    Launch.EntityType.POWERUP, Launch.EntityType.PADDLE) {
                @Override
                protected void onCollisionBegin(Entity pow, Entity paddle) {
                    PowerUp power = (PowerUp) pow;
                    if (!power.isActive())
                        return;
                    PowerUp.textAnimation(power);
                    power.activated();
                    power.removeFromWorld();
                }
            });
            hasCollision = true;
        }
    }

    @Override
    public void onUpdate(double tpf) {
        PowerUp power = (PowerUp) entity;
        power.translateY(tpf * POWER_FALLING_SPEED);
        if (power.isActive() && power.getY() > FXGL.getAppHeight()) {
            power.removeFromWorld();
        }
    }
}
