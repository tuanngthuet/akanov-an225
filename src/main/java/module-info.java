module view {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.almasb.fxgl.all;
    requires com.almasb.fxgl.entity;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;
    requires annotations;
    requires java.sql;
    requires javafx.base;
    requires javafx.media;
    requires java.desktop;
//    requires view;

    opens view to javafx.fxml;
    opens controller to javafx.fxml;
    exports view;
    exports controller to javafx.fxml;
    exports controller.ball_control to javafx.fxml;
    opens controller.ball_control to javafx.fxml;
    exports controller.ScoreControl to javafx.fxml;
    opens controller.ScoreControl to javafx.fxml;
}
