module view {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.almasb.fxgl.all;
    requires com.almasb.fxgl.entity;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;
    requires annotations;
    requires java.sql;
//    requires view;
//    requires view;

    opens view to javafx.fxml;
    opens controller to javafx.fxml;
    exports view;
    exports controller to javafx.fxml;
    exports controller.ball_control to javafx.fxml;
    opens controller.ball_control to javafx.fxml;
}
