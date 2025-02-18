package components;

import javafx.scene.control.Tooltip;
import javafx.util.Duration;

public class CustomTooltip {
    public static Tooltip createTooltip(String text) {
        Tooltip tooltip = new Tooltip(text);
        // Độ trễ hiển thị tooltip
        tooltip.setShowDelay(javafx.util.Duration.millis(300));
        // Độ trễ ẩn tooltip
        tooltip.setHideDelay(javafx.util.Duration.millis(300));
        // Thời gian hiển thị tối đa cho tooltip
        tooltip.setShowDuration(Duration.seconds(10));

        tooltip.setStyle("-fx-background-color: #282828; -fx-text-fill: #ffffff; " +
                "-fx-font-size: 12px; -fx-padding: 8px; -fx-background-radius: 8;");
        ;

        return tooltip;
    }
}
