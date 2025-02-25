package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.StudentData;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import database.database;
import components.AlertComponent;


public class studentController {
    @FXML
    private TextField txtMSSV, txtLastName, txtFirstName;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private ComboBox<String> cbGender, cbSchoolYear, cbMajor, cbStatus, cbSubject;
    @FXML
    private ImageView imgStudentPhoto;
    @FXML
    private Button btnUploadImage, btnSaveStudent, btnClearForm, btnCloseForm;
    @FXML
    private AnchorPane studentMainForm;

    private File selectedImageFile; // Biến lưu trữ ảnh sinh viên

    private double xOffset = 0;
    private double yOffset = 0;

    // Database tools
    private Connection connect;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;

    // Kiểm tra có được edit không?
    private boolean isEditMode = false;

    @FXML
    public void initialize() {
        loadSubjectsIntoComboBox();

        // Khởi tạo dữ liệu cho ComboBox
        cbGender.getItems().addAll("Chọn", "Nam", "Nữ");
        cbSchoolYear.getItems().addAll("Chọn", "Năm 1", "Năm 2", "Năm 3", "Năm 4");
        cbMajor.getItems().addAll("Chọn", "Công nghệ thông tin", "Kinh tế", "Kỹ thuật", "Y khoa");
        cbStatus.getItems().addAll("Chọn", "Đang học", "Bảo lưu", "Nghỉ học", "Tốt nghiệp");

        // Sự kiện chọn ảnh sinh viên
        btnUploadImage.setOnAction(e -> chooseImage());

        // Sự kiện lưu sinh viên
        btnSaveStudent.setOnAction(e -> saveOrUpdateStudent());

        // Sự kiện làm sạch form
        btnClearForm.setOnAction(e -> clearForm());

        btnCloseForm.setOnAction(e -> handleCloseForm());

        // Drag window
        studentMainForm.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        studentMainForm.setOnMouseDragged(e -> {
            Stage currentStage = (Stage) studentMainForm.getScene().getWindow();
            currentStage.setX(e.getScreenX() - xOffset);
            currentStage.setY(e.getScreenY() - yOffset);

            // Set opacity to 0.8 when dragging
            currentStage.setOpacity(0.8);
        });

        studentMainForm.setOnMouseReleased(e -> {
            Stage currentStage = (Stage) studentMainForm.getScene().getWindow();
            currentStage.setOpacity(1.0);
        });
    }

    // Làm sạch form
    private void clearForm() {
        txtMSSV.clear();
        txtLastName.clear();
        txtFirstName.clear();
        dpBirthDate.setValue(null);
        cbGender.setValue("Chọn");
        cbSchoolYear.setValue("Chọn");
        cbMajor.setValue("Chọn");
        cbSubject.setValue("Chọn");
        cbStatus.setValue("Chọn");
        imgStudentPhoto.setImage(null);
    }

    // Đóng form nhập liệu
    private void handleCloseForm() {
        Stage currentStage = (Stage) btnCloseForm.getScene().getWindow();
        currentStage.close();
    }

    public void setStudentData(StudentData student) {
        txtMSSV.setText(student.getStudentID());
        txtLastName.setText(student.getLastName());
        txtFirstName.setText(student.getFirstName());

        if (student.getBirthDate() != null) {
            dpBirthDate.setValue(((java.sql.Date) student.getBirthDate()).toLocalDate());
        } else {
            dpBirthDate.setValue(null);
        }

        cbGender.setValue(student.getGender());
        cbSchoolYear.setValue(student.getSchoolYear());
        cbMajor.setValue(student.getMajor());
        cbSubject.setValue(student.getSubject());
        cbStatus.setValue(student.getStatus());

        if (student.getPhotoPath() != null && !student.getPhotoPath().isEmpty()) {
            File imageFile = new File(student.getPhotoPath());
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString(), 160, 160, false, true);
                imgStudentPhoto.setImage(image);
                imgStudentPhoto.setFitWidth(160);
                imgStudentPhoto.setFitHeight(160);
                imgStudentPhoto.setPreserveRatio(true);
                imgStudentPhoto.setSmooth(true);
                imgStudentPhoto.setCache(true);
            }
        }

        // Vô hiệu hóa tất cả các input khi mở form
        setInputsDisabled(true);
    }

    // Phương thức vô hiệu hóa hoặc kích hoạt các input
    private void setInputsDisabled(boolean disable) {
        txtMSSV.setDisable(disable);
        txtLastName.setDisable(disable);
        txtFirstName.setDisable(disable);
        dpBirthDate.setDisable(disable);
        cbGender.setDisable(disable);
        cbSchoolYear.setDisable(disable);
        cbMajor.setDisable(disable);
        cbSubject.setDisable(disable);
        cbStatus.setDisable(disable);

        btnUploadImage.setDisable(disable);
        btnUploadImage.setVisible(!disable); // Ẩn hiện nút tải ảnh

        // Vô hiệu xoá thêm sinh viên và xoá thông tin
        btnClearForm.setDisable(disable);
        btnSaveStudent.setDisable(disable);
    }

    // Phương thức chọn và hiển thị hình ảnh
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh sinh viên");

        // Chỉ cho phép chọn các định dạng ảnh hợp lệ
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Ảnh (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg"));

        Stage stage = (Stage) studentMainForm.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            selectedImageFile = file;
            Image image = new Image(file.toURI().toString(), 160, 160, false, true);
            imgStudentPhoto.setImage(image);
            imgStudentPhoto.setFitWidth(160);
            imgStudentPhoto.setFitHeight(160);
            imgStudentPhoto.setPreserveRatio(true);
            imgStudentPhoto.setSmooth(true);
            imgStudentPhoto.setCache(true);
        }
    }

    // Phương thức thêm/cập nhật sinh viên
    private void saveOrUpdateStudent() {
        connect = database.connectDB();

        try {
            // Kiểm tra input trống
            if (txtMSSV.getText().isEmpty() || txtLastName.getText().isEmpty() || txtFirstName.getText().isEmpty()
                    || dpBirthDate.getValue() == null || cbGender.getValue().equals("Chọn")
                    || cbSchoolYear.getValue().equals("Chọn") || cbMajor.getValue().equals("Chọn")
                    || cbSubject.getValue().equals("Chọn") || cbStatus.getValue().equals("Chọn")) {
                AlertComponent.showWarning("Lỗi nhập dữ liệu", null, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            // Lấy course_id từ course_name để lưu vào database
            String selectedCourseName = cbSubject.getValue();
            String courseID = getCourseIDByName(selectedCourseName);

            if (courseID == null) {
                AlertComponent.showError("Lỗi", null, "Không tìm thấy mã môn học trong hệ thống!");
                return;
            }

            String sql;
            boolean isUpdating = isEditMode;

            if (isUpdating) {
                // Câu lệnh UPDATE
                sql = "UPDATE students SET first_name=?, last_name=?, birth_date=?, gender=?, " +
                        "school_year=?, major=?, subject=?, status=?, photo_path=? WHERE student_id=?";
                preparedStatement = connect.prepareStatement(sql);

                preparedStatement.setString(1, txtFirstName.getText());
                preparedStatement.setString(2, txtLastName.getText());
                preparedStatement.setDate(3, java.sql.Date.valueOf(dpBirthDate.getValue()));
                preparedStatement.setString(4, cbGender.getValue());
                preparedStatement.setString(5, cbSchoolYear.getValue());
                preparedStatement.setString(6, cbMajor.getValue());
                preparedStatement.setString(7, courseID); // Lưu course_id thay vì course_name
                preparedStatement.setString(8, cbStatus.getValue());

                // Nếu có ảnh thì lưu đường dẫn
                String imagePath = (selectedImageFile != null) ? selectedImageFile.getAbsolutePath() : "";
                preparedStatement.setString(9, imagePath);

                preparedStatement.setString(10, txtMSSV.getText()); // WHERE student_id = ?
            } else {
                // Kiểm tra nếu MSSV đã tồn tại trước khi thêm
                String checkSQL = "SELECT COUNT(*) FROM students WHERE student_id = ?";
                PreparedStatement checkStatement = connect.prepareStatement(checkSQL);
                checkStatement.setString(1, txtMSSV.getText());
                ResultSet resultSet = checkStatement.executeQuery();
                resultSet.next();

                if (resultSet.getInt(1) > 0) { // Nếu MSSV đã tồn tại
                    AlertComponent.showWarning("Lỗi nhập dữ liệu", null,
                            "Mã số sinh viên đã tồn tại! Vui lòng nhập lại.");
                    return;
                }

                // Câu lệnh INSERT
                sql = "INSERT INTO students (student_id, first_name, last_name, birth_date, gender, " +
                        "school_year, major, subject, status, photo_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = connect.prepareStatement(sql);

                preparedStatement.setString(1, txtMSSV.getText()); // MSSV
                preparedStatement.setString(2, txtFirstName.getText());
                preparedStatement.setString(3, txtLastName.getText());
                preparedStatement.setDate(4, java.sql.Date.valueOf(dpBirthDate.getValue()));
                preparedStatement.setString(5, cbGender.getValue());
                preparedStatement.setString(6, cbSchoolYear.getValue());
                preparedStatement.setString(7, cbMajor.getValue());
                preparedStatement.setString(8, courseID); // Lưu course_id thay vì course_name
                preparedStatement.setString(9, cbStatus.getValue());

                // Nếu có ảnh thì lưu đường dẫn
                String imagePath = (selectedImageFile != null) ? selectedImageFile.getAbsolutePath() : "";
                preparedStatement.setString(10, imagePath);
            }

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                AlertComponent.showInformation("Thành công", null,
                        isUpdating ? "Thông tin sinh viên đã được cập nhật!" : "Đã thêm sinh viên vào hệ thống!");

                // Cập nhật danh sách sinh viên ngay lập tức
                dashboardController.getInstance().addStudentsShowList();
                
                // Nếu đang cập nhật thông tin sinh viên
                dashboardController.getInstance().refreshDashboard();

                // iểm tra nếu sinh viên thay đổi môn học hoặc năm học -> cập nhật bảng điểm
                if (isUpdating) {
                    updateGradesSubject(txtMSSV.getText(), courseID);
                    dashboardController.getInstance().showGradesList();
                }

                closeStudentForm(); // Đóng form sau khi cập nhật thành công
            } else {
                AlertComponent.showError("Lỗi", null, "Không thể cập nhật sinh viên!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connect != null)
                    connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Cập nhật môn học trong bảng điểm
    private void updateGradesSubject(String studentID, String newCourseID) {
        String sql = "UPDATE grades SET course_id = ? WHERE student_id = ?";

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            preparedStatement.setString(1, newCourseID);
            preparedStatement.setString(2, studentID);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                // Alert thông báo
                AlertComponent.showInformation("Thành công", null, "Đã cập nhật môn học trong bảng điểm!");
            } else {
                AlertComponent.showError("Lỗi", null, "Không có bản ghi nào cần cập nhật!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Đóng fom
    private void closeStudentForm() {
        Stage stage = (Stage) studentMainForm.getScene().getWindow();
        stage.close();
    }

    // Kích hoạt chế độ chỉnh sửa
    public void enableEditMode() {
        isEditMode = true;
        setInputsDisabled(false); // Bật các input để chỉnh sửa
        txtMSSV.setDisable(true); // Không cho chỉnh sửa MSSV
        btnSaveStudent.setText("Lưu sinh viên"); // Đổi chữ nút thành "Lưu"
    }

    // Load danh sách môn học vào ComboBox
    public void loadSubjectsIntoComboBox() {
        ObservableList<String> subjectList = FXCollections.observableArrayList();
        String sql = "SELECT course_name FROM courses WHERE status = 'Đang mở'"; // Chỉ lấy môn học "Đang mở"

        connect = database.connectDB();
        try {
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                subjectList.add(resultSet.getString("course_name"));
            }
            cbSubject.setItems(subjectList); // Cập nhật ComboBox chỉ với môn học "Đang mở"
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
                if (connect != null)
                    connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Lấy course_id từ course_name
    private String getCourseIDByName(String courseName) {
        String sql = "SELECT course_id FROM courses WHERE course_name = ?";
        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("course_id"); // Trả về mã môn học
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy môn học
    }

}
