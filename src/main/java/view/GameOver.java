package view;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import controller.InitVari;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static javafx.beans.binding.Bindings.when;

public class GameOver extends FXGLMenu implements InitVari {
    public GameOver() {
        super(MenuType.GAME_MENU);
        getContentRoot().getChildren().addAll(createBody());
    }

    public static void showGameOverMenu() {
        FXGL.getGameController().pauseEngine();
        FXGL.getSceneService().pushSubScene(new GameOver());
        FXGL.getGameController().resumeEngine();
    }

    private Node createBody() {
        WritableImage snapshot = new WritableImage(getAppWidth(), getAppHeight());
        getGameScene().getRoot().snapshot(null, snapshot);
        ImageView bgBlur = new ImageView(snapshot);
        bgBlur.setFitWidth(getAppWidth());
        bgBlur.setFitHeight(getAppHeight());
        bgBlur.setEffect(new BoxBlur(10, 10, 7));
        bgBlur.setPreserveRatio(false);
        bgBlur.setSmooth(true);
        bgBlur.setLayoutX(0);
        bgBlur.setLayoutY(0);

        getContentRoot().getChildren().addFirst(bgBlur);

        Text title = new Text("Game Over !!!");
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
        title.setLayoutY(200);

        Node btn1 = createActionButton("NEW GAME", this::fireNewGame);
        Node btn2 = createActionButton("EXIT", this::fireExitToMainMenu);
        Group group = new Group(btn1, btn2);

        int CurrentY = 300;
        for (Node n : group.getChildren()) {
            Rectangle bg = (Rectangle) ((StackPane) n).getChildren().getFirst();
            n.setLayoutX((InitVari.SCREEN_WIDTH - bg.getWidth()) / 2);
            n.setLayoutY(CurrentY);
            CurrentY += (int) (1.75 * bg.getHeight());
        }
        group.getChildren().add(title);
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

        return btn;
    }
}
