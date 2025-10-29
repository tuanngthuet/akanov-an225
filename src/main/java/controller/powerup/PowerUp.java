package controller.powerup;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import view.Launch;

public class PowerUp extends Entity implements PowerUpVari {
    private PowerType powerType;

    public PowerUp(Point2D position) {
        setPosition(position);
        powerType = PowerType.values()[FXGL.random(0, PowerType.values().length - 1)];

        getViewComponent().addChild(new ImageView(POWERUP));
        getBoundingBoxComponent().addHitBox(new HitBox(BoundingShape.box(POWER_SIZE, POWER_SIZE)));
        addComponent(new CollidableComponent(true));
        addComponent(new FallingComponent());
        setType(Launch.EntityType.POWERUP);
    }
}

class FallingComponent extends Component implements PowerUpVari{
    @Override
    public void onUpdate(double tpf) {
        entity.translateY(tpf * POWER_FALLING_SPEED);
        if (entity.getY() > FXGL.getAppHeight()) {
            entity.removeFromWorld();
        }
    }
}
