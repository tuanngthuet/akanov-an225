module view {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.almasb.fxgl.all;

    opens view to javafx.fxml;
    opens controller to javafx.fxml;
    exports view;
    exports controller to javafx.fxml;
}
