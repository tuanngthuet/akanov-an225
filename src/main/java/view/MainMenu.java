package view;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import controller.InitVari;
import controller.user.User;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
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

import static com.almasb.fxgl.dsl.FXGL.getSettings;
import static javafx.beans.binding.Bindings.when;

public class MainMenu extends FXGLMenu implements InitVari {
    private int CurrentY = 200;
    private Text title;
    private SQL_connector connector = new SQL_connector();
    private ArrayList<Integer> session_list = new ArrayList<>();
    public MainMenu() {
        super(MenuType.MAIN_MENU);

        title = new Text(getSettings().getTitle());
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

        getContentRoot().getChildren().addAll(title, createLoginBox());
    }

    private Node createBody() {
        Node btn1 = createActionButton("NEW GAME", this::fireNewGame);
        Node btn2 = createActionButton("LOAD", this::fireNewGame);
        Node btn3 = createActionButton("EXIT", this::fireExit);

        Group  btn_group = new Group(btn1, btn2, btn3);

        for (Node n : btn_group.getChildren()) {
            Rectangle bg = (Rectangle) ((StackPane) n).getChildren().getFirst();
            n.setLayoutX((InitVari.SCREEN_WIDTH - bg.getWidth()) / 2);
            n.setLayoutY(CurrentY);
            CurrentY += (int) (1.75 * bg.getHeight());
        }

        return btn_group;
    }

    private Node createLoginBox() {
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefWidth(250);
        usernameField.setPrefHeight(40);
        usernameField.setTranslateX(SCREEN_WIDTH / 2 - 250 / 2);
        usernameField.setTranslateY(SCREEN_HEIGHT / 2 - 40);

        usernameField.setStyle("""
                    -fx-background-color: rgba(255,255,255,0.8);
                    -fx-border-color: black;
                    -fx-border-width: 2;
                    -fx-border-radius: 10;
                    -fx-background-radius: 10;
                    -fx-font-size: 16px;
                    -fx-text-fill: #222;
                    -fx-prompt-text-fill: #888;
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

        passwordField.setStyle("""
                -fx-background-color: rgba(255,255,255,0.8);
                -fx-border-color: black;
                -fx-border-width: 2;
                -fx-border-radius: 10;
                -fx-background-radius: 10;
                -fx-font-size: 16px;
                -fx-text-fill: #222;
                -fx-prompt-text-fill: #888;
                """
        );

        Node guest_play = createActionButton("GUEST", () -> {
            getContentRoot().getChildren().clear();

            getContentRoot().getChildren().addAll(title, createBody());
        });
        guest_play.setTranslateX(SCREEN_WIDTH / 2 - 250 / 2);
        guest_play.setTranslateY(SCREEN_HEIGHT / 2);

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
