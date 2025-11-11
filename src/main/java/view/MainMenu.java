package view;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
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

        getContentRoot().getChildren().addFirst(background());
        getContentRoot().getChildren().addAll(title, createLoginBox());
    }

    private Node createBody() {
        int CurrentY = 240;
        Node btn1 = createActionButton("NEW GAME", this::fireNewGame);
        Node btn2 = createActionButton("LOAD LASTEST SESSION", this::loadLatestSession);
        Node btn3 = createActionButton("VIEW SESSION", this::showSessionList);
        Node btn4 = createActionButton("EXIT", this::fireExit);
        Node logout_btn = createActionButton("LOG OUT", () -> {
            getContentRoot().getChildren().clear();
            title.setText(getSettings().getTitle());
            getContentRoot().getChildren().addAll(background(), title, createLoginBox());
        });

        Group group = new Group(btn1, btn2, btn3, btn4, logout_btn);

        for (Node n : group.getChildren()) {
            Rectangle bg = (Rectangle) ((StackPane) n).getChildren().getFirst();
            n.setLayoutX((InitVari.SCREEN_WIDTH - bg.getWidth()) / 2 + 175);
            n.setLayoutY(CurrentY);
            CurrentY += (int) (1.75 * bg.getHeight());
        }
        return group;
    }

    private void showSessionList() {
        if (User.user_session.isEmpty()) {
            FXGL.getDialogService().showMessageBox("No saved sessions available!");
            return;
        }

        User.sortSessionsByScore();
        VBox sessionBox = new VBox(10);
        sessionBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < User.user_session.size(); i++) {
            final int index = i;

            String sessionId = User.user_session.get(index);
            int score = User.user_init_score_by_session.get(index);
            int level = User.user_level_by_sessions.get(index);
            String lives = User.user_lives_left_by_sessions.get(index);

            String text = String.format("Session %s \n Score: %d | Level: %d | Lives: %s",
                    sessionId, score, level, lives);

            Node btn = createActionButton(text, () -> {
                if (User.canContinueSession(lives)) {
                    User.selectSession(index);
                    FXGL.getDialogService().showMessageBox("Loading session: " + sessionId, this::fireNewGame);
                } else {
                    FXGL.getDialogService().showMessageBox("This session has no lives left!");
                }
            }, 570, 50);

            sessionBox.getChildren().add(btn);
        }
        FXGL.getDialogService().showBox("Select Session", sessionBox);
    }

    private void loadLatestSession() {
        if (User.user_session.isEmpty()) {
            FXGL.getDialogService().showMessageBox("No session found!");
            return;
        }

        User.selectLatestSession();
        int lastIndex = User.user_session.size() - 1;
        String livesLeft = User.user_lives_left_by_sessions.get(lastIndex);

        if (!User.canContinueSession(livesLeft)) {
            FXGL.getDialogService().showMessageBox("The latest session has no remaining lives!");
            return;
        }

        FXGL.getDialogService().showMessageBox("Loading session: " + User.current_session, this::fireNewGame);
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
            title.setText("Hello Guest!");
            getContentRoot().getChildren().addAll(bgafter());
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

                title.setText("Hello " + User.user_name + "!");
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

        if (name.equals("LOAD LASTEST SESSION")) text.setFont(SMALL_TEXT_FONT);

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

    private Node createActionButton(String name, Runnable action, double width, double height) {
        Rectangle bg = new Rectangle(width, height);
        bg.setArcWidth(15);
        bg.setArcHeight(15);
        bg.setFill(Color.web("#2b1b3f"));

        var text = new Text(name);
        text.setFill(Color.WHITE);
        text.setFont(TEXT_FONT);

        var btn = new StackPane(bg, text);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#ff4fd8"));
        glow.setRadius(2);
        glow.setSpread(0.4);

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

        btn.setAlignment(Pos.CENTER);
        btn.setOnMouseClicked(e -> action.run());

        return btn;
    }

    private Node background() {
        title.setFont(TITLE_FONT);
        title.setLayoutX((getAppWidth() - title.getLayoutBounds().getWidth()) / 2);
        title.setLayoutY(230);
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

    private Node bgafter() {
        title.setFont(SMALL_TITLE);
        title.setLayoutX(185);
        title.setLayoutY(110);
        try {
            ImageView bg = new ImageView(
                    new Image(
                            InitVari.class.getResource("/assets/textures/background/bg2.png").toExternalForm()
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
