package components;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertComponent {

    // Phương thức hiển thị Alert thông báo với kiểu tùy chọn
    public static void showAlert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    // Phương thức hiển thị Alert thông báo với kiểu ERROR
    public static void showError(String title, String headerText, String contentText) {
        showAlert(AlertType.ERROR, title, headerText, contentText);
    }

    // Phương thức hiển thị Alert thông báo với kiểu WARNING
    public static void showWarning(String title, String headerText, String contentText) {
        showAlert(AlertType.WARNING, title, headerText, contentText);
    }

    // Phương thức hiển thị Alert thông báo với kiểu INFORMATION
    public static void showInformation(String title, String headerText, String contentText) {
        showAlert(AlertType.INFORMATION, title, headerText, contentText);
    }

    // Phương thức hiển thị Alert thông báo với kiểu CONFIRMATION (dùng để hỏi người dùng)
    public static boolean showConfirmation(String title, String headerText, String contentText) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        // Kiểm tra xem người dùng đã chọn "OK" hay "Cancel"
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
