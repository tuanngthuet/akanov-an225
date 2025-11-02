package view;

import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import controller.InitVari;
import org.jetbrains.annotations.NotNull;

public class GeneralInit implements InitVari{

    public static void initScreenSettings(GameSettings settings) {
        settings.setWidth(SCREEN_WIDTH);
        settings.setHeight(SCREEN_HEIGHT);
        settings.setTitle(GAME_NAME);
        settings.setVersion(VERSION);
        settings.setGameMenuEnabled(true);
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new SceneFactory() {
            @NotNull
            @Override
            public FXGLMenu newGameMenu() {
                return new PauseMenu();
            }
            @NotNull
            public FXGLMenu newMainMenu() {
                return new MainMenu();
            }
        });
    }

    public static int getCurrentScore(){
        return 0;
    }

}
