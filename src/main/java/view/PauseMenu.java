package view;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import controller.InitVari;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.beans.binding.Bindings.when;

public class PauseMenu extends FXGLMenu implements InitVari {
    private ImageView bgBlur;
    private int CurrentY = 200;

    private final List<Node> buttons = new ArrayList<>();

    private int animIndex = 0;

    public PauseMenu() {
        super(MenuType.GAME_MENU);

        Text title = new Text(getSettings().getTitle());
        title.setFont(TITLE_FONT);

        LinearGradient gradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.RED),
                new Stop(1, Color.YELLOW)
        );
        title.setFill(gradient);

        DropShadow ds = new DropShadow();
        ds.setOffsetX(2);
        ds.setOffsetY(2);
        ds.setColor(Color.color(0, 0, 0, 0.7));
        title.setEffect(ds);

        title.setLayoutX((getAppWidth() - title.getLayoutBounds().getWidth()) / 2);
        title.setLayoutY(CurrentY);

        CurrentY += 70;

        var body = createBody();

        getContentRoot().getChildren().addAll(title, body);
    }

    @Override
    public void onCreate() {

        if (bgBlur != null) getContentRoot().getChildren().remove(bgBlur);

        WritableImage snapshot = new WritableImage(getAppWidth(), getAppHeight());
        getGameScene().getRoot().snapshot(null, snapshot);
        bgBlur = new ImageView(snapshot);
        bgBlur.setFitWidth(getAppWidth());
        bgBlur.setFitHeight(getAppHeight());
        bgBlur.setEffect(new BoxBlur(10, 10, 7));
        bgBlur.setPreserveRatio(false);
        bgBlur.setSmooth(true);
        bgBlur.setLayoutX(0);
        bgBlur.setLayoutY(0);

        getContentRoot().getChildren().addFirst(bgBlur);

        animIndex = 0;

        buttons.forEach(btn -> {
            btn.setOpacity(0);

            animationBuilder(this)
                    .delay(Duration.seconds(animIndex * 0.1))
                    .interpolator(Interpolators.BACK.EASE_OUT())
                    .translate(btn)
                    .from(new Point2D(-200, 0))
                    .to(new Point2D(0, 0))
                    .buildAndPlay();

            animationBuilder(this)
                    .delay(Duration.seconds(animIndex * 0.01))
                    .fadeIn(btn)
                    .buildAndPlay();

            animIndex++;
        });
    }

    private Node createBody() {
        Node btnContinue = createActionButton("CONTINUE", this::fireContinue);
        Node btn1 = createActionButton("NEW GAME", this::fireNewGame);
        Node btn2 = createActionButton("SAVE", this::fireSave);
        Node btn3 = createActionButton("EXIT", this::fireExitToMainMenu);

        Group group = new Group(btnContinue, btn1, btn2, btn3);

        for (Node n : group.getChildren()) {
            Rectangle bg = (Rectangle)((StackPane)n).getChildren().getFirst();
            n.setLayoutX((InitVari.SCREEN_WIDTH - bg.getWidth()) / 2);
            n.setLayoutY(CurrentY);
            CurrentY += (int) (1.75 * bg.getHeight());
        }
        return group;
    }

    private Node createActionButton(String name, Runnable action) {
        var bg = new Rectangle(200, 50);
        bg.setEffect(new BoxBlur());

        var text = new Text(name);
        text.setFill(Color.BLACK);
        text.setFont(TEXT_FONT);

        var btn = new StackPane(bg, text);

        bg.fillProperty().bind(when(btn.hoverProperty())
                .then(Color.LIGHTGREEN)
                .otherwise(Color.DARKGRAY)
        );


        btn.setAlignment(Pos.CENTER);
        btn.setOnMouseClicked(e -> action.run());

        // clipping
        buttons.add(btn);

        Rectangle clip = new Rectangle(200, 50);
        clip.translateXProperty().bind(btn.translateXProperty().negate());

        btn.setTranslateX(-200);
        btn.setClip(clip);
        btn.setCache(true);
        btn.setCacheHint(CacheHint.SPEED);

        return btn;
    }
}