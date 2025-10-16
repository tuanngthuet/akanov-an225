module view {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens view to javafx.fxml;
    exports view;
}