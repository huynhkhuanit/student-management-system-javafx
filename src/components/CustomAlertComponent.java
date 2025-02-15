package components;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class CustomAlertComponent {

    public void showAlert(String alertFXML, String title, String message, String labelId, Stage parentStage) {
        try {
            // Tải file FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(alertFXML));
            AnchorPane alert = loader.load();

            // Tìm kiếm Label bằng fx:id trong FXML
            Label messageLabel = (Label) alert.lookup("#" + labelId);

            // Kiểm tra nếu label được tìm thấy, sau đó thay đổi text
            if (messageLabel != null) {
                messageLabel.setText(message); // Cập nhật nội dung của label
            } else {
                System.err.println("Không tìm thấy Label với fx:id=\"" + labelId + "\"");
            }

            // Tạo cửa sổ mới và hiển thị alert
            Stage alertStage = new Stage();
            Scene scene = new Scene(alert);
            alertStage.setTitle(title);
            alertStage.setScene(scene);

            // Ẩn title bar
            alertStage.initStyle(StageStyle.TRANSPARENT);

            // Lấy tọa độ của cửa sổ chính (parentStage)
            double x = parentStage.getX(); // X của cửa sổ chính
            double y = parentStage.getY(); // Y của cửa sổ chính

            // Đặt alertStage ở góc trên bên trái của cửa sổ chính
            alertStage.setX(x); // Vị trí X (cửa sổ chính)
            alertStage.setY(y); // Vị trí Y (cửa sổ chính)

            // Hiển thị alert
            alertStage.show();

            // Tạo độ trễ 5 giây rồi đóng cửa sổ
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> alertStage.close()); // Đóng cửa sổ khi hết thời gian
            pause.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
