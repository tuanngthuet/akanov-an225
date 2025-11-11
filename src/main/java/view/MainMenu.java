package view;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import controller.InitVari;
import controller.sound_control.AudioManager;
import controller.user.User;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.SQL_connector;


import java.util.ArrayList;
import java.util.Objects;

import static com.almasb.fxgl.dsl.FXGL.getSettings;
import static javafx.beans.binding.Bindings.when;

public class MainMenu extends FXGLMenu implements InitVari {
    private Text title;
    private SQL_connector connector = new SQL_connector();
    private ArrayList<Integer> session_list = new ArrayList<>();

    public MainMenu() {
        super(MenuType.MAIN_MENU);

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

        getContentRoot().getChildren().addFirst(background());
        getContentRoot().getChildren().addAll(title, createLoginBox());
    }


    private Node createBody() {
        int CurrentY = 300;
        Node btn1 = createActionButton("NEW GAME", this::fireNewGame);
        Node btn2 = createActionButton("EXIT", this::fireExit);
        Node logout_btn = createActionButton("LOG OUT", () -> {
            getContentRoot().getChildren().clear();
            getContentRoot().getChildren().addAll(background());
            getContentRoot().getChildren().addAll(title, createLoginBox());
        });
        Node volume_btn = createActionButton("SETTINGS", () -> {
            getContentRoot().getChildren().clear();
            getContentRoot().getChildren().addAll(background());
            getContentRoot().getChildren().addAll(title, createVolumeBox());
        });


        Group group = new Group(btn1, volume_btn, btn2, logout_btn);

        for (Node n : group.getChildren()) {
            Rectangle bg = (Rectangle) ((StackPane) n).getChildren().getFirst();
            n.setLayoutX((InitVari.SCREEN_WIDTH - bg.getWidth()) / 2);
            n.setLayoutY(CurrentY);
            CurrentY += (int) (1.5 * bg.getHeight());
        }
        return group;
    }

    private Node createLoginBox() {
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefWidth(250);
        usernameField.setPrefHeight(40);
        usernameField.setTranslateX(SCREEN_WIDTH / 2 - 250 / 2);
        usernameField.setTranslateY(SCREEN_HEIGHT / 2 - 40);

        usernameField.setStyle("""
        -fx-background-color: rgba(30, 30, 60, 0.6);
        -fx-border-color: #ff77ff; 
        -fx-border-width: 2;
        -fx-border-radius: 10;
        -fx-background-radius: 10;
        -fx-font-size: 16px;
        -fx-text-fill: #e0e0ff; 
        -fx-prompt-text-fill: #aa88ff; 
        """);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefWidth(250);
        passwordField.setPrefHeight(40);
        passwordField.setTranslateX(SCREEN_WIDTH / 2 - 250 / 2);
        passwordField.setTranslateY(SCREEN_HEIGHT / 2 - 30);

        Text wrong_pass = new Text("Invalid username or password!");
        wrong_pass.setTranslateX(SCREEN_WIDTH / 2);
        wrong_pass.setTranslateY(SCREEN_HEIGHT - 30);
        wrong_pass.setFont(TEXT_FONT);
        wrong_pass.setFill(Color.LIGHTGRAY);
        Rectangle blurBg = new Rectangle(wrong_pass.getLayoutBounds().getWidth() + 20, wrong_pass.getLayoutBounds().getHeight() + 10);
        blurBg.setFill(Color.rgb(30, 30, 30, 0.5));
        blurBg.setEffect(new GaussianBlur(100));
        passwordField.setStyle("""
        -fx-background-color: rgba(30, 30, 60, 0.6);
        -fx-border-color: #ff77ff; 
        -fx-border-width: 2;
        -fx-border-radius: 10;
        -fx-background-radius: 10;
        -fx-font-size: 16px;
        -fx-text-fill: #e0e0ff; 
        -fx-prompt-text-fill: #aa88ff; 
        """);

        Node guest_play = createActionButton("GUEST", () -> {
            getContentRoot().getChildren().clear();
            getContentRoot().getChildren().addAll(background());
            getContentRoot().getChildren().addAll(title, createBody());
        });
        guest_play.setTranslateX(SCREEN_WIDTH / 2 - 250 / 2);
        guest_play.setTranslateY(SCREEN_HEIGHT / 2 + 15);

        Node loginBtn = createActionButton("LOGIN", () -> {
            String user = usernameField.getText();
            String pass = passwordField.getText();

            boolean login_status = false;
            login_status = connector.authenticator(user, pass);

            if (login_status) {
                getContentRoot().getChildren().clear();

                User.user_name = connector.getUsername();
                User.user_session = connector.getUserSession();
                User.user_init_score_by_session = connector.getUserScoreBySessions();
                User.user_level_by_sessions = connector.getUserLevelBySessions();
                User.user_lives_left_by_sessions = connector.getUserLivesLeftBySessions();

                User.printOutUserInfo();

                connector.closeSQLConnection();

                getContentRoot().getChildren().addAll(title, createBody());
            } else {
                if (!getContentRoot().getChildren().contains(wrong_pass)) {
                    getContentRoot().getChildren().add(wrong_pass);
                }
            }
        });

        loginBtn.setTranslateX(SCREEN_WIDTH / 2 - 250 / 2);
        loginBtn.setTranslateY(SCREEN_HEIGHT / 2);


        VBox vbox = new VBox(10, usernameField, passwordField, loginBtn, guest_play);
        vbox.setAlignment(Pos.CENTER);

        StackPane box = new StackPane(vbox);
        box.setAlignment(Pos.CENTER);
        return box;
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

        // Hiệu ứng viền sáng (neon)
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

        btn.setOnMouseClicked(e -> action.run());

        return btn;
    }

    private Node createVolumeBox() {
        Text musicText = new Text("MUSIC VOLUME");
        musicText.setFont(TEXT_FONT);
        musicText.setFill(Color.MAGENTA);
        musicText.setEffect(new DropShadow(4, Color.MAGENTA));

        Slider musicslider  = new Slider(0, 1, AudioManager.MUSIC.getVolume());
        musicslider.setShowTickLabels(true);
        musicslider.setShowTickMarks(true);
        musicslider.setMajorTickUnit(0.25);
        musicslider.setBlockIncrement(0.1);
        musicslider.setPrefWidth(300);

        musicslider.valueProperty().addListener((obs, oldVal, newVal) -> {
            float v = newVal.floatValue();
            AudioManager.MUSIC.setVolume(v);
        });

        Text sfxText = new Text("SFX VOLUME");
        sfxText.setFill(Color.MAGENTA);
        sfxText.setFont(TEXT_FONT);
        sfxText.setEffect(new DropShadow(4, Color.MAGENTA));

        Slider sfxSlider = new Slider(0, 1, AudioManager.SFX.getVolume());
        sfxSlider.setShowTickLabels(true);
        sfxSlider.setShowTickMarks(true);
        sfxSlider.setMajorTickUnit(0.25);
        sfxSlider.setBlockIncrement(0.1);
        sfxSlider.setPrefWidth(300);

        Node back_btn = createActionButton("BACK", () -> {
            getContentRoot().getChildren().clear();
            getContentRoot().getChildren().addAll(background());
            getContentRoot().getChildren().addAll(title,createBody());
        });

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(musicText, musicslider, sfxText, sfxSlider, back_btn);
        vbox.setSpacing(15);
        vbox.setLayoutX((SCREEN_WIDTH - 300 ) / 2.0);
        vbox.setLayoutY(SCREEN_HEIGHT / 2.0 - 50);

        return vbox;
    }

    private Node background() {
        try {
            ImageView bg = new ImageView(
                    new Image(
                            InitVari.class.getResource("/assets/textures/background/background.png").toExternalForm()
                    )
            );
            return bg;
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            Rectangle fallback = new Rectangle(SCREEN_WIDTH, SCREEN_HEIGHT, Color.BLACK);
            return fallback;
        }
    }

}
