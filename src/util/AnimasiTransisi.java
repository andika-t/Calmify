package util;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimasiTransisi {

    public static void slideOutLeft(Node node, Runnable after) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), node);
        tt.setToX(-625);
        tt.setOnFinished(e -> after.run());
        tt.play();
    }

    public static void slideOutRight(Node node, Runnable after) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(300), node);
        tt.setToX(625);
        tt.setOnFinished(e -> after.run());
        tt.play();
    }

    public static void fadeIn(Node node) {
        node.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(400), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }

    public static void fadeOut(Node node, Runnable after) {
        FadeTransition fade = new FadeTransition(Duration.millis(400), node);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> after.run());
        fade.play();
    }
}
