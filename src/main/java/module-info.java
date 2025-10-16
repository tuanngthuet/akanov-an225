module gameplay {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens gameplay to javafx.fxml;
    exports gameplay;
}