package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import util.showStage;
import javafx.scene.control.TextField;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import database.database;
import components.AlertComponent;

public class loginController {
    @FXML
    private Button closeBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private AnchorPane mainForm;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userNameField;

    // Database tools
    private Connection connect;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @FXML
    public void initialize() {
        closeBtn.setOnAction(e -> {
            System.exit(0);
        });

        loginBtn.setOnAction(e -> handleLogin());
        userNameField.setOnAction(e -> handleLogin());
        passwordField.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";

        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, userNameField.getText());
            preparedStatement.setString(2, passwordField.getText());

            resultSet = preparedStatement.executeQuery();

            // Kiểm tra xem có dữ liệu trả về không
            if (userNameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                AlertComponent.showError("Đăng nhập thất bại", null, "Vui lòng điền đầy đủ thông tin!");
            } else {
                if (resultSet.next()) {
                    AlertComponent.showInformation("Đăng nhập thành công", null, "Đăng nhập thành công!");

                    // Ẩn giao diện đăng nhập
                    loginBtn.getScene().getWindow().hide();

                    // Bật giao diện dashboard
                    showStage.showWindows("../view/dashboard.fxml", "");
                } else {
                    AlertComponent.showError("Đăng nhập thất bại", null, "Sai tên đăng nhập hoặc mật khẩu!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.closeConnection(connect);
        }
    }
}
