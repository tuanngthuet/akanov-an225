package view;

import Model.Score;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import controller.InitVari;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.getSettings;
import static javafx.beans.binding.Bindings.when;

public class MainMenu extends FXGLMenu implements InitVari {
    private int CurrentY = 200;
    private final List<Node> buttons = new ArrayList<>();

    public MainMenu() {
        super(MenuType.MAIN_MENU);

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


    private Node createBody() {
        Node btn1 = createActionButton("NEW GAME", this::fireNewGame);
        Node btn2 = createActionButton("LOAD", Score::fireLoadMenu);
        Node btn3 = createActionButton("EXIT", this::fireExit);

        Group group = new Group( btn1, btn2, btn3);

        for (Node n : group.getChildren()) {
            Rectangle bg = (Rectangle)((StackPane)n).getChildren().getFirst();
            n.setLayoutX((InitVari.SCREEN_WIDTH - bg.getWidth()) / 2);
            n.setLayoutY(CurrentY);
            CurrentY += (int) (1.75 * bg.getHeight());
        }
        return group;
    }

    private void doNothing(){}

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

        buttons.add(btn);

        return btn;
    }
}
