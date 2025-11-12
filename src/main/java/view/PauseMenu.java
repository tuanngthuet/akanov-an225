package view;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import controller.InitVari;
import controller.sound_control.AudioManager;
import controller.sound_control.MusicManager;
import controller.user.User;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGL.*;
import static javafx.beans.binding.Bindings.when;
import static view.MainMenu.connector;
import static view.MainMenu.login_status;

public class PauseMenu extends FXGLMenu implements InitVari {
    private ImageView bgBlur;
    private int CurrentY = 200;
    private Text title;
    private Node body;
    private Node volumebox;

    private final List<Node> buttons = new ArrayList<>();

    private int animIndex = 0;

    public PauseMenu() {
        super(MenuType.GAME_MENU);

        title = new Text(getSettings().getTitle());
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

        this.body = createBody();
        this.volumebox = VolumeSettings();
        volumebox.setVisible(false);

        getContentRoot().getChildren().addAll(title, body, volumebox);
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
        CurrentY = 300;

        Node btnContinue = createActionButton("CONTINUE", this::fireResume);
        Node btn1 = createActionButton("NEW GAME", this::fireNewGame);
        Node volume_btn = createActionButton("VOLUME OPTION", () -> {
            body.setVisible(false);
            volumebox.setVisible(true);
        });
        Node exitbtn = createActionButton("EXIT", this::fireExitToMainMenu);

        Group group = new Group(btnContinue, btn1, volume_btn, exitbtn);

        for (Node n : group.getChildren()) {
            Rectangle bg = (Rectangle)((StackPane)n).getChildren().getFirst();
            n.setLayoutX((InitVari.SCREEN_WIDTH - bg.getWidth()) / 2);
            n.setLayoutY(CurrentY);
            CurrentY += (int) (1.5 * bg.getHeight());
        }
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
        glow.setRadius(2);
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

    private Node VolumeSettings() {
        Text musicText = new Text("MUSIC VOLUME");
        musicText.setFont(TEXT_FONT);
        musicText.setEffect(new DropShadow(4, Color.MAGENTA));
        musicText.setFill(Color.MAGENTA);

        Slider musicSlider = new Slider(0, 1, AudioManager.MUSIC.getVolume());
        styleSlider(musicSlider);

        musicSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            float v = newVal.floatValue();
            AudioManager.MUSIC.setVolume(v);
        });

        Text sfxText = new Text("SFX VOLUME");
        sfxText.setFont(TEXT_FONT);
        sfxText.setEffect(new DropShadow(4, Color.MAGENTA));
        sfxText.setFill(Color.MAGENTA);

        Slider sfxSlider = new Slider(0, 1, AudioManager.SFX.getVolume());
        styleSlider(sfxSlider);

        sfxSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            float v = newVal.floatValue();
            AudioManager.SFX.setVolume(v);
        });

        Node back_btn = createBackButton("BACK", () -> {
            body.setVisible(true);
            volumebox.setVisible(false);
        });

        VBox vbox = new VBox(20, musicText, musicSlider, sfxText, sfxSlider, back_btn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setLayoutX((SCREEN_WIDTH - 300 ) / 2.0);
        vbox.setSpacing(20);
        vbox.setLayoutY(270);

        return vbox;
    }
    private void styleSlider(Slider slider) {
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(0.25);
        slider.setBlockIncrement(0.1);
        slider.setPrefWidth(300);
    }

    private Node createBackButton(String name, Runnable action) {
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

        // Hiệu ứng viền sáng (neon)
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

        btn.setOnMouseClicked(e -> action.run());

        return btn;
    }


}