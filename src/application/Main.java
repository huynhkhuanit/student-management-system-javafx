package application;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;

public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Tải file FXML và tạo Scene
            Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
            Scene scene = new Scene(root);

            // Kéo thả cửa sổ
            scene.setOnMousePressed(e -> {
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
            });

            scene.setOnMouseDragged(e -> {
                primaryStage.setX(e.getScreenX() - xOffset);
                primaryStage.setY(e.getScreenY() - yOffset);

                // Lúc đang kéo -> opacity: 0.8
                primaryStage.setOpacity(0.8);
            });

            // Lúc đang kéo -> opacity: 1
            scene.setOnMouseReleased(e -> {
                primaryStage.setOpacity(1.0);
            });

            // Favicons app
            // primaryStage.getIcons().add(new Image(getClass().getResource("../assets/img/favicon-app.png").toExternalForm()));

            // Ẩn title bar
            primaryStage.initStyle(StageStyle.TRANSPARENT);

            primaryStage.setScene(scene);

            // Show stage -> Hiện giao diện
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}