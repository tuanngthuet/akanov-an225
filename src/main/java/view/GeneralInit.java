package view;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import controller.InitVari;

public class GeneralInit {

    public static void initScreenSettings(GameSettings settings) {
        settings.setWidth(InitVari.width);
        settings.setHeight(InitVari.height);
        settings.setTitle(InitVari.GameName);
        settings.setVersion(InitVari.Version);
    }



}
