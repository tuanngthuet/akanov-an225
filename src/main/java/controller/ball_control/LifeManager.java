package controller.ball_control;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import view.GameOver;

import java.util.ArrayList;
import java.util.List;

public class LifeManager implements BallVari{
    private List<ImageView> hearts = new ArrayList<>();
    private HBox heartsBox = new HBox(10);

    private int life = DEFAULT_LIFE;

    public void init() {
        heartsBox.setAlignment(Pos.BOTTOM_LEFT);
        heartsBox.setTranslateX(FIRST_HEART_X);
        heartsBox.setTranslateY(FIRST_HEART_Y);

        for(int i = 0; i < MAX_LIFE; i++) {
            ImageView heart = new ImageView(HEART_IMG);
            heart.setFitWidth(HEART_SIZE);
            heart.setFitHeight(HEART_SIZE);
            heart.setVisible(i < life);
            hearts.add(heart);
            heartsBox.getChildren().add(heart);
        }

        FXGL.addUINode(heartsBox);
    }

    public void gainHeart() {
        if(life < MAX_LIFE) {
            ImageView heart = hearts.get(life);
            heart.setVisible(true);
            FadeTransition fade = new FadeTransition(Duration.seconds(0.8),heart);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.setInterpolator(Interpolator.EASE_OUT);
            fade.play();
            life++;
        }
    }

    public void loseHeart() {
        if(life > 1) {
            life--;
            ImageView heart = hearts.get(life);
            FadeTransition fade = new FadeTransition(Duration.seconds(0.8),heart);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setInterpolator(Interpolator.EASE_IN);
            fade.play();
            heart.setVisible(false);
        }
        else {
            GameOver.showGameOverMenu();
        }
    }
}
