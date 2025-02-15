package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class showStage {
    public static void showWindows(String path, String title) {
        try {
            // Tải file fxml -> hiển thị giao diện stage
            Parent root = FXMLLoader.load(showStage.class.getResource(path));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle(title);
            stage.setScene(scene);
            
            // Ẩn title bar mặc định của stages
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
