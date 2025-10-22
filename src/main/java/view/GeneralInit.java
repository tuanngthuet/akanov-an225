package view;

import com.almasb.fxgl.app.GameSettings;
import controller.InitVari;

public class GeneralInit {

    public static void initScreenSettings(GameSettings settings) {
        settings.setWidth(InitVari.width);
        settings.setHeight(InitVari.height);
        settings.setTitle(InitVari.GameName);
        settings.setVersion(InitVari.Version);
    }



}
