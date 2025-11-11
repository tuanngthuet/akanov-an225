package controller.ScoreControl;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.audio.Audio;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getAudioPlayer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.play;
import static controller.InitVari.*;

public class Score_control implements ScoreVari {

    private int current_score_int;
    private String current_score_string;


    public Score_control(int initScore){
        current_score_int = initScore;
        current_score_string = Integer.toString(initScore);
    }

    private Text score_text;
    private Text score_number;

    public void initScore() {
        score_text = new Text("SCORE: ");
        score_number = new Text(current_score_string);

        score_number.setFont(SCORE_TEXT_FONT);
        score_text.setFont(SCORE_TEXT_FONT);

        score_text.setStyle("""
                    -fx-font-size: 45px;
                    -fx-font-weight: bold;
                    -fx-stroke: CYAN;
                    -fx-stroke-width: 2;
                    -fx-arc-width: 30;
                """);

        score_number.setStyle("""
                    -fx-font-size: 45px;
                    -fx-font-weight: bold;
                    -fx-stroke: CYAN;
                    -fx-stroke-width: 2;
                    -fx-arc-width: 30;
                """);

        score_text.setTranslateX(SCORE_TEXT_POS_X);
        score_text.setTranslateY(SCORE_TEXT_POS_Y);

        score_number.setTranslateX(SCORE_NUMBER_POS_X);
        score_number.setTranslateY(SCORE_NUMBER_POS_Y);


        FXGL.addUINode(score_text);
        FXGL.addUINode(score_number);
    }

    private void showFloatingText(String text, double x, double y) {
        Text popup = new Text(text);
        popup.setFont(SCORE_TEXT_FONT);
        popup.setStyle("""
                    -fx-font-size: 45px;
                    -fx-font-weight: bold;
                    -fx-stroke: CYAN;
                    -fx-stroke-width: 2;
                    -fx-arc-width: 30;
                """);

        popup.setTranslateX(x);
        popup.setTranslateY(y);

        FXGL.addUINode(popup);

        animationBuilder()
                .duration(Duration.seconds(1.2))
                .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                .onFinished(() -> getGameScene().removeUINode(popup))
                .translate(popup)
                .from(new Point2D(popup.getTranslateX(), popup.getTranslateY()))
                .to(new Point2D(popup.getTranslateX(), popup.getTranslateY() - 50)) // bay lên 50px
                .buildAndPlay();

        animationBuilder()
                .duration(Duration.seconds(1.2))
                .fadeOut(popup)
                .buildAndPlay();
    }

    public void update_score(int amount){
        current_score_int += amount;
        current_score_string = Integer.toString(current_score_int);

        one_score_up_sound.setVolume(0.4);
        one_score_up_sound.play();

        score_number.setText(current_score_string);
        showFloatingText("+ " + amount, SCORE_NUMBER_POS_X, SCORE_TEXT_POS_Y);
    }

    public int getCurrent_score_int() {
        return current_score_int;
    }
}
