package view;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import controller.InitVari;

public class GeneralInit implements InitVari{

    public static void initScreenSettings(GameSettings settings) {
        settings.setWidth(SCREEN_WIDTH);
        settings.setHeight(SCREEN_HEIGHT);
        settings.setTitle(GAME_NAME);
        settings.setVersion(VERSION);
        settings.setGameMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory() {
            @Override
            public FXGLMenu newGameMenu() {
                //return new SimpleGameMenu();
                return new PauseMenu();
            }
        });
    }



}
