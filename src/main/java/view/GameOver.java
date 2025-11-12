package view;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import controller.InitVari;
import controller.user.User;
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

import java.sql.SQLException;
import java.sql.Timestamp;

import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static javafx.beans.binding.Bindings.when;
import static view.MainMenu.connector;
import static view.MainMenu.login_status;

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

        LinearGradient verticalGradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.MAGENTA),
                new Stop(1, Color.CYAN)
        );
        title.setFill(verticalGradient);

        DropShadow ds = new DropShadow();
        ds.setRadius(10);
        ds.setOffsetX(2);
        ds.setOffsetY(2);
        ds.setColor(Color.color(0, 0, 0, 0.7));
        title.setEffect(ds);

        title.setLayoutX((getAppWidth() - title.getLayoutBounds().getWidth()) / 2);
        title.setLayoutY(230);

        Node btn1 = createActionButton("NEW GAME", this::fireNewGame);
        Node btn2 = createActionButton("EXIT", this::fireExitToMainMenu);
        Group group = new Group(btn1, btn2);

        int CurrentY = 350;
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
        bg.setArcWidth(15);
        bg.setArcHeight(15);
        bg.setFill(Color.web("#2b1b3f"));

        var text = new Text(name);
        text.setFill(Color.WHITE);
        text.setFont(TEXT_FONT);

        var btn = new StackPane(bg, text);
        btn.setPickOnBounds(false);
        btn.setAlignment(Pos.CENTER);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#ff4fd8"));
        glow.setRadius(0);
        glow.setSpread(0.4);

        bg.setEffect(glow);

        btn.setOnMouseEntered(e -> {
            glow.setRadius(12);
            bg.setFill(Color.web("#ff4fd8"));
            text.setFill(Color.web("#ffffff"));
        });

        btn.setOnMouseExited(e -> {
            glow.setRadius(0);
            bg.setFill(Color.web("#2b1b3f"));
            text.setFill(Color.WHITE);
        });

        btn.setOnMouseClicked(e -> {
            if (name.equals("NEW GAME") && login_status) {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                System.out.println(now + "Time create a new game session on PauseMenu!");
                User.user_new_end = now.toString();

                try {
                    connector.createNewSession(User.user_new_start, User.user_new_end, User.user_update_score, User.user_update_lives);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                User.user_new_start = now.toString();
                User.user_new_end = null;

                User.user_init_score = 0;
            }
            action.run();
        });


        return btn;
    }
}
