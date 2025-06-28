package util;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class AnimasiHover {

    public static void to(Button button){
        DropShadow glow = new DropShadow();

        glow.setRadius(10);
        glow.setSpread(0.1);

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e ->{
            button.setEffect(glow);
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, e ->{
            button.setEffect(null);
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
        });
    }
}
