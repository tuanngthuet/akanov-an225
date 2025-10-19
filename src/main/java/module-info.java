module view {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.almasb.fxgl.all;
    requires com.almasb.fxgl.entity;

    opens view to javafx.fxml;
    opens controller to javafx.fxml;
    exports view;
    exports controller to javafx.fxml;
}
