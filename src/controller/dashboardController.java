package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

// Import thư viện

// Database tools
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.Date;
import java.text.SimpleDateFormat;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

// JavaFX tools
import components.AlertComponent;
import components.CustomTooltip;
import javafx.fxml.FXML;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.showStage;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.stage.StageStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.fasterxml.jackson.databind.ObjectMapper;

// Model data
import model.StudentData;
import model.ColumnHeader;
import model.CourseData;
import model.CreditData;
import model.GradesData;
import model.ImprovementData;
import model.LecturerData;
import database.database;

public class dashboardController {
    // Database tools
    private Connection connect;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    // Dashboard controller
    private static dashboardController instance;

    // Gán this của Dashboard controller
    public dashboardController() {
        instance = this;
    }

    // get Dashboard controller
    public static dashboardController getInstance() {
        return instance;
    }

    // Main
    @FXML
    private Button closeBtn, dashboardBtn, minimizeBtn, logoutBtn;

    @FXML
    private AnchorPane dashboardForm;

    @FXML
    private StackPane mainDashboard;

    @FXML
    private ResourceBundle resources;

    @FXML
    private Label mainUsername;

    // ========== DASHBOARD PROPERTY ==========

    @FXML
    private Label totalStudentsLabel, totalMaleStudentsLabel, totalFemaleStudentsLabel;
    @FXML
    private AreaChart<String, Number> avgScoreChart;
    @FXML
    private LineChart<String, Number> yearlyStatsChart;
    @FXML
    private BarChart<String, Number> totalStudentsChart;

    // Làm mới dashboard cho thống kê
    @FXML
    public void refreshDashboard() {
        updateStudentStatistics();
        loadTotalStudentsChart();
        loadAvgScoreChart();
        loadYearlyStatsChart();
    }

    // ========== DASHBOARD PROPERTY ==========

    // ========== SINH VIÊN PROPERTY ==========
    @FXML
    private AnchorPane studentManageForm;
    @FXML
    private Button studentManageBtn, btnImportStudents, btnDownloadStudents, btnExportActiveStudents;

    // Bảng dữ liệu sinh viên
    @FXML
    private TableView<StudentData> studentTable;

    // Các cột trong bảng sinh viên
    @FXML
    private TableColumn<StudentData, String> student_colStudentID; // Cột MSSV
    @FXML
    private TableColumn<StudentData, String> student_colLastName; // Cột Tên
    @FXML
    private TableColumn<StudentData, String> student_colFirstName; // Cột Họ
    @FXML
    private TableColumn<StudentData, String> student_colBirthYear; // Cột Năm sinh
    @FXML
    private TableColumn<StudentData, String> student_colGender; // Cột Giới tính
    @FXML
    private TableColumn<StudentData, String> student_colSchoolYear; // Cột Năm học
    @FXML
    private TableColumn<StudentData, String> student_colMajor; // Cột Chuyên ngành
    @FXML
    private TableColumn<StudentData, String> student_colSubject; // Cột Môn học
    @FXML
    private TableColumn<StudentData, String> student_colStatus; // Cột Trạng thái

    // - Khu vực chứa các nút điều khiển
    @FXML
    private HBox controlButtonsBox;

    // Các nút điều khiển của sinh viên
    @FXML
    private Button btnAdd, btnClearAll, btnDelete, btnRefresh, btnUpdate;

    // Ô tìm kiếm
    @FXML
    private TextField studentManageSearch;

    // ========== SINH VIÊN PROPERTY ==========

    // ========== MÔN HỌC PROPERTY ==========

    @FXML
    private Button courseManageBtn, btnUploadSubjects, btnDownloadSubjects;
    @FXML
    private AnchorPane courseManageForm;
    @FXML
    private TextField txtSubjectID, txtSubjectName, txtCredits;
    @FXML
    private ComboBox<String> cbSemester, cbStatus, cbLecturer;
    @FXML
    private TableView<CourseData> tblSubjects;
    @FXML
    private TableColumn<CourseData, String> colSubjectID, colSubjectName, colLecturer, colSemester, colStatus;
    @FXML
    private TableColumn<CourseData, Integer> colCredits;
    @FXML
    private Button btnAddSubject, btnDeleteSubject, btnClearAllSubjects, btnUpdateSubject,
            btnClearFormSubject;

    // ========== MÔN HỌC PROPERTY ==========

    // =========== QUẢN LÝ GIẢNG VIÊN ===========
    @FXML
    private Button lecturerManageBtn, btnUploadLecturer, btnDownloadLecturer;
    @FXML
    private AnchorPane lecturerManageForm;
    @FXML
    private TextField txtLecturerID, txtLecturerName, txtLecturerPhone;
    @FXML
    private ComboBox<String> cbLecturerGender, cbLecturerDegree, cbLecturerStatus;
    @FXML
    private TableView<LecturerData> tableLecturer;
    @FXML
    private TableColumn<LecturerData, String> colLecturerID, colLecturerName, colLecturerGender, colLecturerDegree,
            colLecturerPhone,
            colLecturerStatus;
    @FXML
    private Button btnAddLecturer, btnDeleteLecturer, btnClearAllLecturer, btnUpdateLecturer,
            btnClearFormLecturer;

    // =========== QUẢN LÝ GIẢNG VIÊN ===========

    // =========== QUẢN LÝ ĐIỂM ===========

    @FXML
    private AnchorPane gradesManageForm;
    @FXML
    private TextField txtStudentIDGrades, txtMidtermGrade, txtFinalGrade, txtTotalGrade, txtSearchGrades;

    @FXML
    private Label lblSchoolYearGrades, lblSubjectGrades;

    @FXML
    private Button btnAddGrades, btnDeleteGrades, btnClearAllGrades, btnUpdateGrades,
            btnClearFormGrades, gradesManageBtn, btnUploadGrades, btnDownloadGrades, btnSuggestImprovement,
            btnAccumulateCredits;

    @FXML
    private TableView<GradesData> tableGrades;

    @FXML
    private TableColumn<GradesData, String> colStudentIDGrades, colSchoolYearGrades, colSubjectGrades, colMidtermGrade,
            colFinalGrade, colTotalGrade;

    // =========== QUẢN LÝ ĐIỂM ===========

    // =========== THÔNG TIN ===========

    @FXML
    private Button infoManageBtn, versionBtn, teamBtn, technologyBtn, clauseBtn, questionBtn;
    @FXML
    private AnchorPane infoManageForm, versionForm, teamForm, technologyForm, clauseForm, questionForm;

    // =========== THÔNG TIN ===========

    // Toạ độ x và y để di chuyển cửa sổ
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        // Kiểm tra xem admin đã đăng nhập chưa
        if (loginController.loggedInUsername != null) {
            mainUsername.setText(loginController.loggedInUsername);
        } else {
            mainUsername.setText("Admin");
        }

        // ========== Load dữ liệu từ database cho các bảng ==========

        addStudentsShowList(); // Load dữ liệu sinh viên
        showCoursesList(); // Load dữ liệu môn học
        showLecturersList(); // Load dữ liệu giảng viên
        showGradesList(); // Load dữ liệu điểm

        updateLecturersInCourse(); // Cập nhật danh sách giảng viên trong ComboBox

        // ========== Start - Thống kê dashboard ==========

        updateStudentStatistics();
        loadTotalStudentsChart();
        loadAvgScoreChart();
        loadYearlyStatsChart();

        dashboardForm.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                refreshDashboard(); // Cập nhật Dashboard khi hiển thị
            }
        });

        // ========== End - Thống kê dashboard ==========

        // ========== Load dữ liệu từ database cho các bảng ==========

        // Ẩn các form quản lý
        dashboardForm.setVisible(true);
        studentManageForm.setVisible(false);
        courseManageForm.setVisible(false);
        lecturerManageForm.setVisible(false);
        gradesManageForm.setVisible(false);
        infoManageForm.setVisible(false);

        // Drag window
        mainDashboard.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        mainDashboard.setOnMouseDragged(e -> {
            Stage currentStage = (Stage) mainDashboard.getScene().getWindow();
            currentStage.setX(e.getScreenX() - xOffset);
            currentStage.setY(e.getScreenY() - yOffset);

            // Set opacity to 0.8 when dragging
            currentStage.setOpacity(0.8);
        });

        mainDashboard.setOnMouseReleased(e -> {
            Stage currentStage = (Stage) mainDashboard.getScene().getWindow();
            currentStage.setOpacity(1.0);
        });

        // Close windows
        closeBtn.setOnAction(e -> {
            System.exit(0);
        });

        // Minimize windows
        minimizeBtn.setOnAction(e -> {
            Stage currentStage = (Stage) minimizeBtn.getScene().getWindow();
            currentStage.setIconified(true);
        });

        // Logout
        logoutBtn.setOnAction(e -> logoutHandle());

        // ========== Start - SHOW FORMS ==========

        dashboardBtn.setOnAction(e -> switchForm(dashboardForm, dashboardBtn)); // Mở form Dashboard
        studentManageBtn.setOnMouseClicked(e -> switchForm(studentManageForm, studentManageBtn)); // Mở form quản lý
                                                                                                  // sinh viên
        courseManageBtn.setOnMouseClicked(e -> switchForm(courseManageForm, courseManageBtn)); // Mở form quản lý môn
                                                                                               // học
        lecturerManageBtn.setOnMouseClicked(e -> switchForm(lecturerManageForm, lecturerManageBtn)); // Mở form quản lý
                                                                                                     // giảng viên

        // Mở form quản lý điểm
        gradesManageBtn.setOnMouseClicked(e -> {
            switchForm(gradesManageForm, gradesManageBtn);
            txtTotalGrade.setEditable(false);
            txtTotalGrade.setOpacity(0.5);
            txtTotalGrade
                    .setTooltip(CustomTooltip.createTooltip("Tự động cập nhật dựa vào điểm giữa kỳ và cuối kỳ!"));
        });

        // Mở form quản lý thông tin
        infoManageBtn.setOnMouseClicked(e -> switchForm(infoManageForm, infoManageBtn));

        // ========== End - SHOW FORMS ==========

        // ========== QUẢN LÝ SINH VIÊN ==========

        // Sự kiện nhấp đúp chuột vào một dòng trong bảng sinh viên -> Mở form chỉnh sửa
        studentTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) { // Nếu nhấp đúp chuột vào một dòng
                openStudentForm();
            }
        });
        btnAdd.setOnAction(e -> handleAddBtn()); // Phương thức thêm sinh viên
        btnRefresh.setOnAction(e -> {
            studentManageSearch.clear(); // Xoá nội dung ô tìm kiếm khi làm mới

            // Cập nhật danh sách điểm để hiển thị lại môn học vừa cập nhật
            dashboardController.getInstance().showGradesList();
            addStudentsShowList();
        }); // Phương thức làm mới bảng sinh viên
        btnUpdate.setOnAction(e -> handleUpdateStudent()); // Phương thức cập nhật sinh viên
        btnDelete.setOnAction(e -> handleDeleteStudent()); // Phương thức xóa sinh viên
        btnClearAll.setOnAction(e -> handleDeleteAllStudents()); // Phương thức xóa toàn bộ sinh viên
        studentManageSearch.textProperty().addListener((observable, oldValue, newValue) -> searchStudent());
        // Tìm kiếm sinh viên

        // Sự kiện mở form import sinh viên
        btnImportStudents.setOnAction(event -> openFileChooser());

        btnDownloadStudents.setOnAction(event -> downloadStudentsFile());

        // Thêm sự kiện cho nút xuất file xác nhận trạng thái
        btnExportActiveStudents.setOnAction(event -> exportActiveStudents());

        // ========== QUẢN LÝ SINH VIÊN ==========

        // ========== QUẢN LÝ MÔN HỌC ==========

        cbSemester.getItems().addAll("Chọn", "Học kỳ 1", "Học kỳ 2", "Học kỳ 3");
        cbStatus.getItems().addAll("Chọn", "Đang mở", "Đóng");

        tblSubjects.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) { // Kiểm tra nếu nhấp đúp chuột
                loadSelectedCourseData();
                btnAddSubject.setDisable(true); // Khóa nút thêm
            }
        });

        // Phương thức thêm môn học
        btnAddSubject.setOnAction(e -> handleAddCourse()); // Thêm môn học
        btnDeleteSubject.setOnAction(e -> handleDeleteCourse()); // Xóa môn học
        btnClearAllSubjects.setOnAction(e -> handleDeleteAllCourses()); // Xóa toàn bộ môn học
        btnUpdateSubject.setOnAction(e -> handleUpdateCourse()); // Cập nhật môn học
        btnClearFormSubject.setOnAction(e -> clearFormSubject()); // Xoá form nhập liệu

        // Đặt tooltip cho btnUploadSubjects
        btnUploadSubjects.setTooltip(CustomTooltip.createTooltip(("Uploads danh sách môn học từ files!")));
        btnUploadSubjects.setOnAction(event -> openSubjectFileChooser());
        btnDownloadSubjects.setOnAction(event -> downloadSubjectsFile());

        // ========== QUẢN LÝ MÔN HỌC ==========

        // ========== QUẢN LÝ GIẢNG VIÊN ==========

        tableLecturer.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) { // Kiểm tra nếu nhấp đúp chuột
                loadSelectedLecturerData();
                btnAddLecturer.setDisable(true); // Khóa nút thêm
            }
        });

        // Khởi tạo dữ liệu cho ComboBox
        cbLecturerGender.getItems().addAll("Chọn", "Nam", "Nữ");
        cbLecturerDegree.getItems().addAll("Chọn", "Cử nhân", "Thạc sĩ", "Tiến sĩ", "Phó giáo sư", "Giáo sư");
        cbLecturerStatus.getItems().addAll("Chọn", "Đang giảng dạy", "Nghỉ phép", "Đã nghỉ hưu");

        // Thêm sự kiện cho các Button
        btnAddLecturer.setOnAction(e -> handleAddLecturer());
        btnUpdateLecturer.setOnAction(e -> handleUpdateLecturer());
        btnDeleteLecturer.setOnAction(e -> handleDeleteLecturer());
        btnClearFormLecturer.setOnAction(e -> handleClearFormLecturer());
        btnClearAllLecturer.setOnAction(e -> handleClearAllLecturer());

        // Gán sự kiện cho nút Upload giảng viên
        btnUploadLecturer.setOnAction(e -> openLecturerFileChooser());

        // Tạo tooltip cho nút Upload giảng viên
        btnUploadLecturer.setTooltip(CustomTooltip.createTooltip(
                "- Đối với file excel hãy tuân thủ theo cấu trúc của quản lý đặt ra!\n- Cấu trúc file excel:\n \t| LecturerID | LecturerName | Gender | Degree | Phone | Status |"));

        btnDownloadLecturer.setOnAction(event -> downloadLecturerFile());

        // ========== QUẢN LÝ GIẢNG VIÊN ==========

        // ========== QUẢN LÝ ĐIỂM ==========
        txtStudentIDGrades.textProperty().addListener((observable, oldValue, newValue) -> loadStudentInfo());

        // Sự kiện nút thêm điểm
        btnAddGrades.setOnAction(e -> handleAddGrades());

        // Sự kiện nút cập nhật điểm
        btnUpdateGrades.setOnAction(e -> handleUpdateGrades());

        // Sự kiện nút xóa điểm
        btnDeleteGrades.setOnAction(e -> handleDeleteGrades());

        // Sự kiện nút xóa toàn bộ điểm
        btnClearAllGrades.setOnAction(e -> handleClearAllGrades());

        // Sự kiện nút xóa thông tin nhập liệu
        btnClearFormGrades.setOnAction(e -> clearGradesForm());

        tableGrades.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Nhấp đôi - đúp chuột
                fillFormWithSelectedGrade();
            }
        });

        // Gọi phương thức tìm kiếm khi người dùng nhập MSSV
        txtSearchGrades.textProperty().addListener((observable, oldValue, newValue) -> {
            searchGradesByStudentID(newValue.trim());
        });

        btnUploadGrades.setOnAction(event -> openGradesFileChooser());
        btnDownloadGrades.setOnAction(event -> downloadGradesFile());
        btnSuggestImprovement.setOnAction(event -> suggestImprovement()); // Gợi ý cải thiện điểm
        btnAccumulateCredits.setOnAction(event -> accumulateCredits()); // Tính tổng số tín chỉ

        // ========== QUẢN LÝ ĐIỂM ==========

        // ========== THÔNG TIN ==========

        versionBtn.setOnAction(e -> versionBtnHandle());
        teamBtn.setOnAction(e -> teamBtnHandle());
        technologyBtn.setOnAction(e -> technologyBtnHandle());
        clauseBtn.setOnAction(e -> clauseBtnHandle());
        questionBtn.setOnAction(e -> questionBtnHandle());

        // ========== THÔNG TIN ==========
    }

    // ---> CÁC HÀM XỬ LÝ <---

    // - Tong quan -

    // Phuong thuc hien thi form duoc chon va an cac form con lai
    private void switchForm(AnchorPane selectedForm, Button selectedButton) {
        // Danh sách tất cả các form
        AnchorPane[] allForms = { dashboardForm, studentManageForm, courseManageForm, lecturerManageForm,
                gradesManageForm, infoManageForm };

        // Danh sách các nút tương ứng
        Button[] allButtons = { dashboardBtn, studentManageBtn, courseManageBtn, lecturerManageBtn, gradesManageBtn,
                infoManageBtn };

        // Xử lý hiển thị form
        for (AnchorPane form : allForms) {
            form.setVisible(form == selectedForm);
        }

        // Đặt màu nền cho nút được chọn và reset các nút khác
        for (Button button : allButtons) {
            if (button == selectedButton) {
                button.setStyle("-fx-background-color: #0093e9;");
            } else {
                button.setStyle("-fx-background-color: transparent;");
            }
        }
    }

    // Xử lý nút đăng xuất
    private void logoutHandle() {
        try {
            // Thong bao xac nhan dang xuat
            if (AlertComponent.showConfirmation("Thông báo xác nhận", null, "Bạn có muốn đăng xuất không?")) {
                logoutBtn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                // Drag window
                root.setOnMousePressed(e -> {
                    xOffset = e.getSceneX();
                    yOffset = e.getSceneY();
                });

                root.setOnMouseDragged(e -> {
                    Stage currentStage = (Stage) root.getScene().getWindow();
                    currentStage.setX(e.getScreenX() - xOffset);
                    currentStage.setY(e.getScreenY() - yOffset);

                    // Set opacity to 0.8 when dragging
                    currentStage.setOpacity(0.8);
                });

                root.setOnMouseReleased(e -> {
                    Stage currentStage = (Stage) root.getScene().getWindow();
                    currentStage.setOpacity(1.0);
                });

                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            } else
                return;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // - Tong quan -

    // ========== THỐNG KÊ DỮ LIỆU ==========

    // Cập nhật thống kê sinh viên
    private void updateStudentStatistics() {
        String totalQuery = "SELECT COUNT(*) AS total FROM students";
        String maleQuery = "SELECT COUNT(*) AS total FROM students WHERE gender = 'Nam'";
        String femaleQuery = "SELECT COUNT(*) AS total FROM students WHERE gender = 'Nữ'";

        try (Connection connect = database.connectDB();
                PreparedStatement totalStmt = connect.prepareStatement(totalQuery);
                PreparedStatement maleStmt = connect.prepareStatement(maleQuery);
                PreparedStatement femaleStmt = connect.prepareStatement(femaleQuery)) {

            ResultSet totalResult = totalStmt.executeQuery();
            if (totalResult.next()) {
                totalStudentsLabel.setText(String.valueOf(totalResult.getInt("total")));
            }

            ResultSet maleResult = maleStmt.executeQuery();
            if (maleResult.next()) {
                totalMaleStudentsLabel.setText(String.valueOf(maleResult.getInt("total")));
            }

            ResultSet femaleResult = femaleStmt.executeQuery();
            if (femaleResult.next()) {
                totalFemaleStudentsLabel.setText(String.valueOf(femaleResult.getInt("total")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load biểu đồ tổng sinh viên theo ngành
    private void loadTotalStudentsChart() {
        String query = "SELECT major, COUNT(*) AS total FROM students GROUP BY major";

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Tổng sinh viên theo ngành");

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String major = resultSet.getString("major");
                int total = resultSet.getInt("total");
                series.getData().add(new XYChart.Data<>(major, total));
            }

            totalStudentsChart.getData().clear();
            totalStudentsChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load biểu đồ điểm rung bình của sinh viên
    private void loadAvgScoreChart() {
        String query = "SELECT school_year, AVG(total_grade) AS avg_score FROM grades GROUP BY school_year";

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Điểm trung bình theo năm");

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String year = resultSet.getString("school_year");
                double avgScore = resultSet.getDouble("avg_score");
                series.getData().add(new XYChart.Data<>(year, avgScore));
            }

            avgScoreChart.getData().clear();
            avgScoreChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Biểu đồ thống kê số lượng sinh viên theo năm học
    private void loadYearlyStatsChart() {
        String query = "SELECT school_year, COUNT(*) AS total FROM students GROUP BY school_year";

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số lượng sinh viên theo năm học");

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String year = resultSet.getString("school_year");
                int totalStudents = resultSet.getInt("total");
                series.getData().add(new XYChart.Data<>(year, totalStudents));
            }

            yearlyStatsChart.getData().clear();
            yearlyStatsChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== THỐNG KÊ DỮ LIỆU ==========

    // ========== QUẢN LÝ SINH VIÊN ==========

    // Phương thức lấy dữ liệu từ database -> ánh xạ vào table view
    public ObservableList<StudentData> addStudentsListData() {
        ObservableList<StudentData> listStudents = FXCollections.observableArrayList();
        String sql = """
                    SELECT s.student_id, s.first_name, s.last_name, s.birth_date,
                           s.gender, s.school_year, s.major,
                           c.course_name AS subject, s.status, s.photo_path
                    FROM students s
                    LEFT JOIN courses c ON s.subject = c.course_id
                """;

        connect = database.connectDB();

        try {
            StudentData studentData;
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                studentData = new StudentData(
                        resultSet.getString("student_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getDate("birth_date"),
                        resultSet.getString("gender"),
                        resultSet.getString("school_year"),
                        resultSet.getString("major"),
                        resultSet.getString("subject"),
                        resultSet.getString("status"),
                        resultSet.getString("photo_path"));

                listStudents.add(studentData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listStudents;
    }

    // Hiển thị danh sách sinh viên
    private ObservableList<StudentData> addStudentsListD;

    // Phương thức hiển thị danh sách sinh viên
    public void addStudentsShowList() {
        addStudentsListD = addStudentsListData();

        student_colStudentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        student_colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        student_colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        student_colBirthYear.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        student_colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        student_colSchoolYear.setCellValueFactory(new PropertyValueFactory<>("schoolYear"));
        student_colMajor.setCellValueFactory(new PropertyValueFactory<>("major"));
        student_colSubject.setCellValueFactory(new PropertyValueFactory<>("subject")); // Hiển thị course_name
        student_colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        studentTable.setItems(addStudentsListD);
    }

    // Phương thức mở form sinh viên
    private void openStudentForm() {
        // Lấy sinh viên được chọn từ TableView
        StudentData selectedStudent = studentTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một sinh viên!");
            return;
        }

        try {
            // Load student_form.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/student_form.fxml"));
            Parent root = loader.load();

            // Lấy controller của student_form.fxml
            studentController controller = loader.getController();

            // Gửi dữ liệu sinh viên vào form
            controller.setStudentData(selectedStudent);

            // Tạo một Stage mới để hiển thị form
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức cập nhật sinh viên
    private void handleUpdateStudent() {
        StudentData selectedStudent = studentTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một sinh viên!");
            return;
        }

        try {
            // Load student_form.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/student_form.fxml"));
            Parent root = loader.load();

            // Lấy controller của student_form.fxml
            studentController controller = loader.getController();

            // Gửi dữ liệu sinh viên vào form nhưng cho phép chỉnh sửa
            controller.setStudentData(selectedStudent);
            controller.enableEditMode(); // Bật chế độ chỉnh sửa

            // Tạo một Stage mới để hiển thị form
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức xoá sinh viên (1 sinh viên)
    private void handleDeleteStudent() {
        StudentData selectedStudent = studentTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một sinh viên để xóa!");
            return;
        }

        // Hiển thị xác nhận xóa
        boolean confirm = AlertComponent.showConfirmation("Xác nhận", null,
                "Bạn có chắc chắn muốn xóa sinh viên có MSSV: " + selectedStudent.getStudentID() + " không?");

        if (!confirm)
            return;

        String sql = "DELETE FROM students WHERE student_id = ?";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, selectedStudent.getStudentID());
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                AlertComponent.showInformation("Thành công", null, "Đã xóa sinh viên thành công!");
                addStudentsShowList(); // Cập nhật lại danh sách sinh viên trong bảng
            } else {
                AlertComponent.showError("Lỗi", null, "Không thể xóa sinh viên!");
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

    // Phương thức xoá toàn bộ sinh viên trong bảng
    private void handleDeleteAllStudents() {
        // Hiển thị cảnh báo trước khi xóa toàn bộ dữ liệu
        boolean confirm = AlertComponent.showConfirmation("Xác nhận", null,
                "Bạn có chắc chắn muốn xóa toàn bộ sinh viên trong hệ thống không? Hành động này không thể hoàn tác!");

        if (!confirm)
            return;

        String sql = "DELETE FROM students";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                AlertComponent.showInformation("Thành công", null, "Đã xóa toàn bộ sinh viên thành công!");
                addStudentsShowList(); // Cập nhật lại danh sách sinh viên trong bảng
            } else {
                AlertComponent.showWarning("Thông báo", null, "Không có sinh viên nào trong hệ thống để xóa!");
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

    // Phương thức tìm kiếm sinh viên
    private void searchStudent() {
        String keyword = studentManageSearch.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            studentTable.setItems(addStudentsListD); // Nếu ô tìm kiếm trống, hiển thị lại toàn bộ dữ liệu
            return;
        }

        ObservableList<StudentData> filteredList = FXCollections.observableArrayList();
        for (StudentData student : addStudentsListD) {
            if (student.getStudentID().toLowerCase().contains(keyword) ||
                    student.getFirstName().toLowerCase().contains(keyword) ||
                    student.getLastName().toLowerCase().contains(keyword)) {
                filteredList.add(student);
            }
        }

        studentTable.setItems(filteredList);
    }

    // Show add student form
    private void handleAddBtn() {
        showStage.showWindows("../view/student_form.fxml", "");
    }

    // Mở cửa sổ chọn file để nhập sinh viên
    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file sinh viên");

        // Thêm các bộ lọc cho JSON, Excel, CSV, và All Files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"), // Hiển thị tất cả file
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Đặt "All Files" làm bộ lọc mặc định để hiển thị tất cả file
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            processFile(file);
        }
    }

    // Thêm sinh viên từ file JSON
    private void importFromJson(File file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<StudentData> students = objectMapper.readValue(Paths.get(file.getAbsolutePath()).toFile(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, StudentData.class));

            int duplicateCount = 0; // Đếm số sinh viên trùng MSSV
            int successCount = 0; // Đếm số sinh viên thêm thành công
            int errorCount = 0; // Đếm số lỗi khác

            for (StudentData student : students) {
                if (insertStudentIntoDatabase(student)) {
                    successCount++;
                } else {
                    if (student.getStudentID() != null && isStudentIDExists(student.getStudentID())) {
                        duplicateCount++;
                    } else {
                        errorCount++;
                    }
                }
            }

            // Tạo thông báo dựa trên kết quả
            String message = "Đã thêm " + successCount + " sinh viên thành công từ JSON!";
            if (duplicateCount > 0) {
                message += "\nCó " + duplicateCount + " sinh viên trùng MSSV đã bị bỏ qua.";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " lỗi khác xảy ra.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            addStudentsShowList(); // Cập nhật lại TableView
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập sinh viên từ JSON: " + e.getMessage());
        }
    }

    // Xử lý cell trong file Excel
    private String getCellStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("MM/dd/yyyy").format(cell.getDateCellValue());
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BLANK:
                return null;
            default:
                return null;
        }
    }

    // Thêm sinh viên từ file Excel
    private void importFromExcel(File file) {
        try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis);
                Connection connect = database.connectDB()) {

            PreparedStatement preparedStatement = connect.prepareStatement(
                    "INSERT INTO students (student_id, first_name, last_name, birth_date, gender, " +
                            "school_year, major, subject, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            Sheet sheet = workbook.getSheetAt(0);
            int successCount = 0;
            int duplicateCount = 0;
            int errorCount = 0;

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                try {
                    String studentID = getCellStringValue(row.getCell(0));
                    String firstName = getCellStringValue(row.getCell(1));
                    String lastName = getCellStringValue(row.getCell(2));
                    String birthDateStr = getCellStringValue(row.getCell(3));
                    String gender = getCellStringValue(row.getCell(4));
                    String schoolYear = getCellStringValue(row.getCell(5));
                    String major = getCellStringValue(row.getCell(6));
                    String subject = getCellStringValue(row.getCell(7));
                    String status = getCellStringValue(row.getCell(8));

                    if (studentID == null || firstName == null || lastName == null || birthDateStr == null ||
                            gender == null || schoolYear == null || major == null || subject == null
                            || status == null) {
                        throw new IllegalArgumentException("Dữ liệu trống tại dòng " + (row.getRowNum() + 1));
                    }

                    java.sql.Date birthDate = new java.sql.Date(dateFormat.parse(birthDateStr).getTime());
                    StudentData student = new StudentData(studentID, firstName, lastName, birthDate, gender,
                            schoolYear, major, subject, status, null);

                    if (insertStudentIntoDatabase(student)) {
                        successCount++;
                    } else {
                        if (isStudentIDExists(studentID)) {
                            duplicateCount++;
                            AlertComponent.showWarning("Lỗi", null, "Sinh viên trùng MSSV: " + studentID);
                        } else {
                            errorCount++;
                        }
                    }
                } catch (Exception e) {
                    if (e instanceof IllegalArgumentException) {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null,
                                "Dòng lỗi tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                    } else {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null,
                                "Dòng lỗi tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                    }
                }
            }

            if (successCount > 0) {
                preparedStatement.executeBatch(); // Chỉ gọi nếu có bản ghi thành công
            }

            String message = "Đã nhập " + successCount + " sinh viên thành công từ Excel!";
            if (duplicateCount > 0) {
                message += "\nCó " + duplicateCount + " sinh viên trùng MSSV bị bỏ qua.";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " dòng lỗi không thể nhập.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            addStudentsShowList();
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập sinh viên từ Excel: " + e.getMessage());
        }
    }

    // Xử lý file CSV
    private void importFromCsv(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            int successCount = 0;
            int duplicateCount = 0;
            int errorCount = 0;

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    String[] data = line.split(",");
                    if (data.length < 9) {
                        throw new IllegalArgumentException("Dữ liệu không đủ tại dòng hiện tại");
                    }

                    String studentID = data[0].trim();
                    String firstName = data[1].trim();
                    String lastName = data[2].trim();
                    String birthDateStr = data[3].trim();
                    String gender = data[4].trim();
                    String schoolYear = data[5].trim();
                    String major = data[6].trim();
                    String subject = data[7].trim();
                    String status = data[8].trim();

                    if (studentID.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || birthDateStr.isEmpty() ||
                            gender.isEmpty() || schoolYear.isEmpty() || major.isEmpty() || subject.isEmpty()
                            || status.isEmpty()) {
                        throw new IllegalArgumentException("Dữ liệu trống tại dòng hiện tại");
                    }

                    java.sql.Date birthDate;
                    try {
                        java.util.Date utilDate = dateFormat.parse(birthDateStr);
                        birthDate = new java.sql.Date(utilDate.getTime());
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("Định dạng ngày không hợp lệ tại dòng hiện tại");
                    }

                    StudentData student = new StudentData(studentID, firstName, lastName, birthDate,
                            gender, schoolYear, major, subject, status, null);

                    if (insertStudentIntoDatabase(student)) {
                        successCount++;
                    } else {
                        if (isStudentIDExists(studentID)) {
                            duplicateCount++;
                            AlertComponent.showWarning("Lỗi", null, "Sinh viên trùng MSSV: " + studentID);
                        } else {
                            errorCount++;
                        }
                    }
                } catch (Exception e) {
                    if (e instanceof IllegalArgumentException) {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null, "Dòng lỗi tại dòng hiện tại: " + e.getMessage());
                    } else {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null, "Dòng lỗi tại dòng hiện tại: " + e.getMessage());
                    }
                }
            }

            String message = "Đã nhập " + successCount + " sinh viên thành công từ CSV!";
            if (duplicateCount > 0) {
                message += "\nCó " + duplicateCount + " sinh viên trùng MSSV bị bỏ qua.";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " dòng lỗi không thể nhập.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            addStudentsShowList();
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập sinh viên từ CSV: " + e.getMessage());
        }
    }

    // Kiểm tra xem MSSV đã tồn tại chưa
    private boolean isStudentIDExists(String studentID) {
        String sql = "SELECT COUNT(*) FROM students WHERE student_id = ?";
        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            preparedStatement.setString(1, studentID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            AlertComponent.showError("Lỗi", null, "Không thể kiểm tra MSSV: " + e.getMessage());
            return false;
        }
        return false;
    }

    // Xử lý thêm sinh viên vào database
    private boolean insertStudentIntoDatabase(StudentData student) {
        String sql = "INSERT INTO students (student_id, first_name, last_name, birth_date, gender, " +
                "school_year, major, subject, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            String courseID = getCourseIDByName(student.getSubject());
            if (courseID == null) {
                AlertComponent.showError("Lỗi", null, "Không tìm thấy course_id cho môn học: " + student.getSubject());
                return false;
            }

            if (isStudentIDExists(student.getStudentID())) {
                AlertComponent.showWarning("Lỗi", null, "Sinh viên trùng MSSV: " + student.getStudentID());
                return false;
            }

            preparedStatement.setString(1, student.getStudentID());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getLastName());
            preparedStatement.setDate(4, new java.sql.Date(student.getBirthDate().getTime()));
            preparedStatement.setString(5, student.getGender());
            preparedStatement.setString(6, student.getSchoolYear());
            preparedStatement.setString(7, student.getMajor());
            preparedStatement.setString(8, courseID);
            preparedStatement.setString(9, student.getStatus());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                AlertComponent.showError("Lỗi", null, "Không thể thêm sinh viên vào database.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            AlertComponent.showError("Lỗi", null, "Lỗi database khi thêm sinh viên: " + e.getMessage());
            return false;
        }
    }

    // Phương thức xử lý file nhập vào (JSON, Excel, CSV)
    private void processFile(File file) {
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".json")) {
            importFromJson(file);
        } else if (fileName.endsWith(".xlsx")) {
            importFromExcel(file);
        } else if (fileName.endsWith(".csv")) {
            importFromCsv(file);
        } else {
            // Nếu file không hợp lệ, hiển thị cảnh báo
            AlertComponent.showWarning("Lỗi", null, "Định dạng file không hợp lệ! Vui lòng chọn JSON, Excel hoặc CSV.");
        }
    }

    // Xuất file sinh viên
    private void downloadStudentsFile() {
        // Hiển thị dialog để người dùng chọn định dạng file
        ChoiceDialog<String> dialog = new ChoiceDialog<>("JSON", "JSON", "Excel", "CSV");
        dialog.setTitle("Chọn định dạng file");
        dialog.setHeaderText("Vui lòng chọn định dạng file để xuất:");
        dialog.setContentText("Định dạng:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Lưu file sinh viên");

            // Thêm các bộ lọc tương ứng với định dạng được chọn
            switch (result.get()) {
                case "JSON":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
                    fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
                    break;
                case "Excel":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                    fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
                    break;
                case "CSV":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                    fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
                    break;
            }

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    // Xuất file chính (JSON, Excel, hoặc CSV)
                    switch (result.get()) {
                        case "JSON":
                            exportToJson(file);
                            break;
                        case "Excel":
                            exportToExcel(file);
                            break;
                        case "CSV":
                            exportToCsv(file);
                            break;
                    }
                    AlertComponent.showInformation("Thành công", null, "Đã xuất file thành công: " + file.getName());

                    // Tạo tên file .txt dựa trên tên file chính (thay .json/.xlsx/.csv bằng .txt)
                    String txtFileName = file.getAbsolutePath().replaceAll("\\.(json|xlsx|csv)$", ".txt");
                    File instructionFile = new File(txtFileName);

                    // Kiểm tra nếu file .txt chưa tồn tại, thì xuất hướng dẫn
                    if (!instructionFile.exists()) {
                        exportInstructionsToTxt(instructionFile);
                        AlertComponent.showInformation("Thành công", null,
                                "Đã xuất file hướng dẫn: " + instructionFile.getName());
                    }

                    AlertComponent.showWarning("Lưu ý", null, "Vui lòng đọc file hướng dẫn để điền đúng!");
                } catch (Exception e) {
                    AlertComponent.showError("Lỗi", null, "Không thể xuất file: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    // Xuất file hướng dẫn .txt
    private void exportInstructionsToTxt(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String[] instructions = {
                    "HƯỚNG DẪN NHẬP DỮ LIỆU CHO FILE JSON, EXCEL, HOẶC CSV:",
                    "1. Ngày sinh (birth_date): Nhập theo định dạng 'MM/dd/yyyy', ví dụ: '06/25/2004'",
                    "2. Năm học (school_year): Chỉ nhập từ 'Năm 1' đến 'Năm 4'",
                    "3. Môn học (subject): Nhập theo danh sách trong Quản lý môn học (xem bảng Courses)",
                    "4. Trạng thái (status) đối với sinh viên: Chỉ nhập: 'Đang học', 'Bảo lưu', hoặc 'Nghỉ học'",
                    "5. Trạng thái (status) đối với môn học: Chỉ nhập: 'Đang mở', 'Đóng'",
                    "6. Trạng thái (status) đối với giảng viên: Chỉ nhập: 'Đang giảng dạy', 'Nghỉ phép', 'Đã nghỉ hưu'",
                    "7. Giảng viên phụ trách môn học: Chính xác theo bảng giảng viên (Xem Lecturers)",
                    "8. Học kỳ: Chỉ nhập: 'Học kỳ 1', 'Học kỳ 2', 'Học kỳ 3'",
                    "9. Học hàm / học vị: Chỉ nhập: 'Cử nhân', 'Thạc sĩ', 'Tiến sĩ', 'Phó giáo sư', 'Giáo sư'",
                    "10. Giới tính (gender): Chỉ nhập 'Nam' hoặc 'Nữ'"
            };

            for (String instruction : instructions) {
                writer.write(instruction);
                writer.newLine();
            }
        }
    }

    // Xuất danh sách sinh viên sang file JSON
    private void exportToJson(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("MM/dd/yyyy")); // Định dạng ngày cho JSON

        ObservableList<StudentData> students = studentTable.getItems();
        if (students == null || students.isEmpty()) {
            // Nếu TableView trống, kiểm tra database
            students = addStudentsListData(); // Lấy dữ liệu từ database
            if (students == null || students.isEmpty()) {
                // Nếu database trống, xuất chỉ tiêu đề cột
                List<ColumnHeader> headers = new ArrayList<>();
                headers.add(new ColumnHeader(
                        "student_id", "first_name", "last_name", "birth_date", "gender", "school_year", "major",
                        "subject", "status", ""));
                objectMapper.writeValue(file, headers);
            } else {
                objectMapper.writeValue(file, students);
            }
        } else {
            // Nếu TableView có dữ liệu, xuất trực tiếp
            objectMapper.writeValue(file, students);
        }
    }

    // Xuất danh sách sinh viên sang file Excel
    private void exportToExcel(File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        ObservableList<StudentData> students = studentTable.getItems();
        int rowNum = 0;

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = { "student_id", "first_name", "last_name", "birth_date", "gender", "school_year", "major",
                "subject", "status" };

        // Tạo tiêu đề cột
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        if (students == null || students.isEmpty()) {
            // Nếu TableView trống, kiểm tra database
            students = addStudentsListData(); // Lấy dữ liệu từ database
            if (students == null || students.isEmpty()) {
                // Nếu database trống, chỉ xuất tiêu đề
            } else {
                // Nếu database có dữ liệu, xuất danh sách
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                for (StudentData student : students) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(student.getStudentID());
                    row.createCell(1).setCellValue(student.getFirstName());
                    row.createCell(2).setCellValue(student.getLastName());
                    row.createCell(3).setCellValue(dateFormat.format(student.getBirthDate()));
                    row.createCell(4).setCellValue(student.getGender());
                    row.createCell(5).setCellValue(student.getSchoolYear());
                    row.createCell(6).setCellValue(student.getMajor());
                    row.createCell(7).setCellValue(student.getSubject());
                    row.createCell(8).setCellValue(student.getStatus());
                }
            }
        } else {
            // Nếu TableView có dữ liệu, xuất danh sách
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            for (StudentData student : students) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getStudentID());
                row.createCell(1).setCellValue(student.getFirstName());
                row.createCell(2).setCellValue(student.getLastName());
                row.createCell(3).setCellValue(dateFormat.format(student.getBirthDate()));
                row.createCell(4).setCellValue(student.getGender());
                row.createCell(5).setCellValue(student.getSchoolYear());
                row.createCell(6).setCellValue(student.getMajor());
                row.createCell(7).setCellValue(student.getSubject());
                row.createCell(8).setCellValue(student.getStatus());
            }
        }

        // Tự động điều chỉnh chiều rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    // Xuất danh sách sinh viên sang file CSV
    private void exportToCsv(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String[] headers = { "student_id", "first_name", "last_name", "birth_date", "gender", "school_year",
                    "major", "subject", "status" };
            StringBuilder line = new StringBuilder();

            // Ghi tiêu đề
            for (int i = 0; i < headers.length; i++) {
                line.append(headers[i]);
                if (i < headers.length - 1)
                    line.append(",");
            }
            writer.write(line.toString());
            writer.newLine();

            ObservableList<StudentData> students = studentTable.getItems();
            if (students != null && !students.isEmpty()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                for (StudentData student : students) {
                    line.setLength(0); // Xóa nội dung cũ
                    line.append(student.getStudentID()).append(",")
                            .append(student.getFirstName()).append(",")
                            .append(student.getLastName()).append(",")
                            .append(dateFormat.format(student.getBirthDate())).append(",")
                            .append(student.getGender()).append(",")
                            .append(student.getSchoolYear()).append(",")
                            .append(student.getMajor()).append(",")
                            .append(student.getSubject()).append(",")
                            .append(student.getStatus());
                    writer.write(line.toString());
                    writer.newLine();
                }
            } else {
                // Nếu TableView trống, kiểm tra database
                students = addStudentsListData(); // Lấy dữ liệu từ database
                if (students != null && !students.isEmpty()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    for (StudentData student : students) {
                        line.setLength(0); // Xóa nội dung cũ
                        line.append(student.getStudentID()).append(",")
                                .append(student.getFirstName()).append(",")
                                .append(student.getLastName()).append(",")
                                .append(dateFormat.format(student.getBirthDate())).append(",")
                                .append(student.getGender()).append(",")
                                .append(student.getSchoolYear()).append(",")
                                .append(student.getMajor()).append(",")
                                .append(student.getSubject()).append(",")
                                .append(student.getStatus());
                        writer.write(line.toString());
                        writer.newLine();
                    }
                }
            }
        }
    }

    // Xuất danh sách sinh viên đang học
    private void exportActiveStudents() {
        // Chọn định dạng file
        ChoiceDialog<String> formatDialog = new ChoiceDialog<>("JSON", "JSON", "Excel", "CSV");
        formatDialog.setTitle("Chọn định dạng file");
        formatDialog.setHeaderText("Vui lòng chọn định dạng file để xuất danh sách sinh viên đang học:");
        formatDialog.setContentText("Định dạng:");

        Optional<String> formatResult = formatDialog.showAndWait();
        if (!formatResult.isPresent())
            return; // Người dùng hủy dialog

        // Chọn vị trí lưu file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file xác nhận trạng thái đi học");
        fileChooser
                .setInitialFileName("DanhSachSinhVienDangHoc_" + new SimpleDateFormat("yyyyMMdd").format(new Date()));
        String selectedFormat = formatResult.get();

        switch (selectedFormat) {
            case "JSON":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
                break;
            case "Excel":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                break;
            case "CSV":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                break;
        }
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                // Lấy danh sách sinh viên đang học trong khối try
                ObservableList<StudentData> activeStudents = getActiveStudents();
                if (activeStudents.isEmpty()) {
                    AlertComponent.showWarning("Thông báo", null, "Không có sinh viên nào đang học trong hệ thống!");
                    return;
                }

                // Xuất file theo định dạng đã chọn
                switch (selectedFormat) {
                    case "JSON":
                        exportActiveStudentsToJson(file, activeStudents);
                        break;
                    case "Excel":
                        exportActiveStudentsToExcel(file, activeStudents);
                        break;
                    case "CSV":
                        exportActiveStudentsToCsv(file, activeStudents);
                        break;
                }
                AlertComponent.showInformation("Thành công", null, "Đã xuất file thành công: " + file.getName());

                // Xuất file hướng dẫn nếu chưa tồn tại
                String txtFileName = file.getAbsolutePath().replaceAll("\\.(json|xlsx|csv)$", ".txt");
                File instructionFile = new File(txtFileName);
                if (!instructionFile.exists()) {
                    exportInstructionsToTxt(instructionFile);
                    AlertComponent.showInformation("Thành công", null,
                            "Đã xuất file hướng dẫn: " + instructionFile.getName());
                }
            } catch (SQLException e) {
                AlertComponent.showError("Lỗi", null, "Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                AlertComponent.showError("Lỗi", null, "Không thể lưu file: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                AlertComponent.showError("Lỗi", null, "Lỗi không xác định khi xuất file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Lấy danh sách sinh viên đang học
    private ObservableList<StudentData> getActiveStudents() throws SQLException {
        ObservableList<StudentData> activeStudents = FXCollections.observableArrayList();
        String sql = """
                SELECT s.student_id, s.first_name, s.last_name, s.school_year, s.major, c.course_name
                FROM students s
                LEFT JOIN courses c ON s.subject = c.course_id
                WHERE s.status = 'Đang học'
                """;

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                activeStudents.add(new StudentData(
                        resultSet.getString("student_id"),
                        resultSet.getString("first_name"), // Để tương thích với model, nhưng không dùng trực tiếp
                        resultSet.getString("last_name"), // Để tương thích với model, nhưng không dùng trực tiếp
                        null, // birth_date không cần trong báo cáo này
                        null, // gender không cần
                        resultSet.getString("school_year"),
                        resultSet.getString("major"),
                        resultSet.getString("course_name"),
                        "Đang học",
                        null // photo_path không cần
                ));
            }
        }
        return activeStudents;
    }

    // Xuất danh sách sinh viên đang học sang file JSON
    private void exportActiveStudentsToJson(File file, ObservableList<StudentData> activeStudents) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("total_active_students", activeStudents.size());
        List<Map<String, String>> studentList = activeStudents.stream().map(student -> {
            Map<String, String> studentData = new HashMap<>();
            studentData.put("student_id", student.getStudentID());
            studentData.put("full_name", student.getFirstName() + " " + student.getLastName());
            studentData.put("school_year", student.getSchoolYear());
            studentData.put("major", student.getMajor());
            studentData.put("subject", student.getSubject());
            return studentData;
        }).collect(Collectors.toList());
        data.put("active_students", studentList);

        objectMapper.writeValue(file, data);
    }

    // Xuất danh sách sinh viên đang học sang file Excel
    private void exportActiveStudentsToExcel(File file, ObservableList<StudentData> activeStudents) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Danh sách sinh viên đang học");

        int rowNum = 0;
        Row summaryRow = sheet.createRow(rowNum++);
        summaryRow.createCell(0).setCellValue("Tổng số sinh viên đang học: " + activeStudents.size());

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = { "MSSV", "Họ tên", "Năm học", "Chuyên ngành", "Môn học" };
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        for (StudentData student : activeStudents) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getStudentID());
            row.createCell(1).setCellValue(student.getFirstName() + " " + student.getLastName());
            row.createCell(2).setCellValue(student.getSchoolYear());
            row.createCell(3).setCellValue(student.getMajor());
            row.createCell(4).setCellValue(student.getSubject());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    // Xuất danh sách sinh viên đang học sang file CSV
    private void exportActiveStudentsToCsv(File file, ObservableList<StudentData> activeStudents) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Tổng số sinh viên đang học," + activeStudents.size());
            writer.newLine();
            writer.write("MSSV,Họ tên,Năm học,Chuyên ngành,Môn học");
            writer.newLine();

            for (StudentData student : activeStudents) {
                String line = String.format("%s,\"%s\",%s,%s,%s",
                        student.getStudentID(),
                        student.getFirstName() + " " + student.getLastName(),
                        student.getSchoolYear(),
                        student.getMajor(),
                        student.getSubject());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    // ========== QUẢN LÝ SINH VIÊN ==========

    // ========== QUẢN LÝ MÔN HỌC ==========

    private void clearFormSubject() {
        txtSubjectID.setDisable(false); // Mở khóa ô nhập mã môn học
        btnAddSubject.setDisable(false); // Mở khóa nút thêm
        txtSubjectID.clear();
        txtSubjectName.clear();
        txtCredits.clear();
        cbLecturer.setValue("Chọn");
        cbSemester.setValue("Chọn");
        cbStatus.setValue("Chọn");
    }

    // Phương thức lấy dữ liệu từ database -> ánh xạ vào table view
    public ObservableList<CourseData> getCoursesListData() {
        ObservableList<CourseData> courseList = FXCollections.observableArrayList();
        String sql = """
                    SELECT c.course_id, c.course_name, c.credits, c.lecturer, l.lecturer_name,
                           c.semester, c.status
                    FROM courses c
                    LEFT JOIN lecturers l ON c.lecturer = l.lecturer_id
                """;

        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String lecturerDisplayName = resultSet.getString("lecturer_name") != null
                        ? resultSet.getString("lecturer_name")
                        : "Chưa có giảng viên";

                courseList.add(new CourseData(
                        resultSet.getString("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getInt("credits"),
                        lecturerDisplayName, // Hiển thị tên giảng viên thay vì ID
                        resultSet.getString("semester"),
                        resultSet.getString("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

    // Hiển thị danh sách môn học
    private ObservableList<CourseData> courseListData;

    public void showCoursesList() {
        courseListData = getCoursesListData();

        colSubjectID.setCellValueFactory(new PropertyValueFactory<>("subjectID"));
        colSubjectName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));
        colLecturer.setCellValueFactory(new PropertyValueFactory<>("lecturer"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tblSubjects.setItems(courseListData);
    }

    private void loadSelectedCourseData() {
        CourseData selectedCourse = tblSubjects.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một môn học!");
            return;
        }

        txtSubjectID.setText(selectedCourse.getSubjectID());
        txtSubjectName.setText(selectedCourse.getSubjectName());
        txtCredits.setText(String.valueOf(selectedCourse.getCredits()));
        cbSemester.setValue(selectedCourse.getSemester());
        cbStatus.setValue(selectedCourse.getStatus());

        // Lấy lecturer_id từ database theo tên giảng viên
        String lecturerID = getLecturerIDByName(selectedCourse.getLecturer());

        // Lưu `lecturer_id` vào `UserData` của ComboBox
        cbLecturer.setUserData(lecturerID);
        cbLecturer.setValue(selectedCourse.getLecturer());

        // Khóa mã môn học để tránh sửa
        txtSubjectID.setDisable(true);
    }

    // Lấy lecturer_id từ lecturer_name
    private String getLecturerIDByName(String lecturerName) {
        String sql = "SELECT lecturer_id FROM lecturers WHERE lecturer_name = ?";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, lecturerName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("lecturer_id");
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
        return null;
    }

    // Hàm overloading mới: Chuẩn hóa tên giảng viên trước khi tìm kiếm
    private String getLecturerIDByName(String lecturerName, boolean normalize) {
        if (lecturerName == null || lecturerName.trim().isEmpty()) {
            return null;
        }

        // Chuẩn hóa tên giảng viên: Loại bỏ tiền tố, khoảng trắng thừa, và chuẩn hóa
        // dấu tiếng Việt
        if (normalize) {
            String normalizedName = lecturerName.trim()
                    .replaceAll("^(CN\\.|ThS\\.|TS\\.|PSG\\.|GS\\.\\s*)", "") // Loại bỏ các tiền tố
                    .replaceAll("\\s+", " ") // Chuẩn hóa khoảng trắng thành một khoảng trắng duy nhất
                    .trim(); // Loại bỏ khoảng trắng đầu và cuối

            // Chuẩn hóa dấu tiếng Việt (nếu cần)
            normalizedName = normalizeVietnameseChars(normalizedName);

            // Tìm lecturer_id với tên đã chuẩn hóa
            String sql = "SELECT lecturer_id FROM lecturers WHERE lecturer_name = ?";
            try (Connection connect = database.connectDB();
                    PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
                preparedStatement.setString(1, normalizedName);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    return resultSet.getString("lecturer_id");
                }
            } catch (SQLException e) {
                AlertComponent.showError("Lỗi", null, "Không thể truy vấn thông tin giảng viên: " + e.getMessage());
                return null;
            }
        } else {
            // Gọi hàm cũ nếu không cần chuẩn hóa
            return getLecturerIDByName(lecturerName);
        }
        return null; // Không tìm thấy lecturer_name sau khi chuẩn hóa
    }

    // Hàm phụ trợ: Chuẩn hóa dấu tiếng Việt
    private String normalizeVietnameseChars(String input) {
        if (input == null)
            return null;

        // Chuẩn hóa dấu tiếng Việt (ví dụ: "Nguyen" -> "Nguyễn")
        return input
                .replaceAll("([aA])", "$1") // Giữ nguyên 'a' và 'A'
                .replaceAll("([eE])", "$1")
                .replaceAll("([oO])", "$1")
                .replaceAll("([uU])", "$1")
                .replaceAll("([iI])", "$1")
                .replaceAll("([dD])", "$1")
                .replaceAll("([yY])", "$1")
                // Thêm các quy tắc khác nếu cần (ví dụ: "nguyen" -> "nguyễn")
                .replaceAll("nguyen", "nguyễn")
                .replaceAll("tran", "trần")
                .replaceAll("huynh", "huỳnh");
    }

    // Kiểm tra input hợp lệ đối với môn học
    private boolean validateCourseInput() {
        if (txtSubjectID.getText().trim().isEmpty() ||
                txtSubjectName.getText().trim().isEmpty() ||
                txtCredits.getText().trim().isEmpty() ||
                cbLecturer.getValue().equals("Chọn") ||
                cbSemester.getValue() == null ||
                cbStatus.getValue() == null ||
                cbSemester.getValue().equals("Chọn") ||
                cbStatus.getValue().equals("Chọn")) {

            AlertComponent.showWarning("Lỗi nhập liệu", null, "Vui lòng điền đầy đủ thông tin!");
            return false;
        }

        // Kiểm tra mã môn học bắt đầu bằng "MH"
        String courseID = txtSubjectID.getText().trim();
        if (!courseID.matches("^MH\\d+$")) { // Mã phải bắt đầu bằng "MH" và theo sau là số
            AlertComponent.showWarning("Lỗi nhập liệu", null, "Mã môn học phải bắt đầu bằng 'MH' và theo sau là số!");
            return false;
        }

        // Kiểm tra số tín chỉ là số hợp lệ
        try {
            int credits = Integer.parseInt(txtCredits.getText().trim());
            if (credits <= 0) {
                AlertComponent.showWarning("Lỗi nhập liệu", null, "Số tín chỉ phải là số nguyên dương!");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertComponent.showWarning("Lỗi nhập liệu", null, "Số tín chỉ phải là số nguyên!");
            return false;
        }

        return true;
    }

    // Kiểm tra mã môn học đã tồn tại chưa
    private boolean isCourseIDExists(String courseID) {
        String sql = "SELECT COUNT(*) FROM courses WHERE course_id = ?";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, courseID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true; // Mã môn học đã tồn tại
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
        return false; // Mã môn học chưa tồn tại
    }

    // Sự kiên thêm môn học
    private void handleAddCourse() {
        if (!validateCourseInput())
            return;

        String courseID = txtSubjectID.getText().trim();
        String courseName = txtSubjectName.getText().trim();
        int credits = Integer.parseInt(txtCredits.getText().trim());
        String selectedLecturer = cbLecturer.getValue();

        if (selectedLecturer.equals("Chọn") || selectedLecturer == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn giảng viên!");
            return;
        }

        // Tách lấy mã giảng viên từ "GV001 - TS. Nguyễn Văn A"
        String lecturerID = selectedLecturer.split(" - ")[0];

        // Kiểm tra xem lecturer_id có tồn tại không
        if (!isLecturerIDExists(lecturerID)) {
            AlertComponent.showError("Lỗi", null, "Mã giảng viên không tồn tại!");
            return;
        }

        String semester = cbSemester.getValue();
        String status = cbStatus.getValue();

        if (isCourseIDExists(courseID)) {
            AlertComponent.showWarning("Lỗi", null, "Mã môn học đã tồn tại!");
            return;
        }

        String sql = "INSERT INTO courses (course_id, course_name, credits, lecturer, semester, status) VALUES (?, ?, ?, ?, ?, ?)";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, courseID);
            preparedStatement.setString(2, courseName);
            preparedStatement.setInt(3, credits);
            preparedStatement.setString(4, lecturerID); // Lưu lecturer_id thay vì lecturer_name
            preparedStatement.setString(5, semester);
            preparedStatement.setString(6, status);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                AlertComponent.showInformation("Thành công", null, "Môn học đã được thêm thành công!");
                showCoursesList();
                updateStudentSubjectsComboBox();
                clearFormSubject();
            } else {
                AlertComponent.showError("Lỗi", null, "Không thể thêm môn học!");
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

    // Phương thức xoá một môn học khỏi danh sách
    private void handleDeleteCourse() {
        try {
            CourseData selectedCourse = tblSubjects.getSelectionModel().getSelectedItem();

            // Kiểm tra nếu không có môn học nào được chọn
            if (selectedCourse == null) {
                AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một môn học để xóa!");
                return;
            }

            // Hiển thị hộp thoại xác nhận
            boolean confirm = AlertComponent.showConfirmation("Xác nhận", null,
                    "Bạn có chắc chắn muốn xóa môn học: " + selectedCourse.getSubjectName() + "?");

            if (!confirm)
                return;

            // Xóa môn học trong database
            String sql = "DELETE FROM courses WHERE course_id = ?";
            connect = database.connectDB();

            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, selectedCourse.getSubjectID());

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                AlertComponent.showInformation("Thành công", null, "Môn học đã được xóa thành công!");
                showCoursesList(); // Cập nhật lại danh sách môn học
                updateStudentSubjectsComboBox(); // Cập nhật danh sách môn học của sinh viên
            } else {
                AlertComponent.showError("Lỗi", null, "Không thể xóa môn học!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            AlertComponent.showError("Lỗi", null, "Đã xảy ra lỗi khi xóa môn học!");
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

    // Phương thức xoá toàn bộ môn học
    private void handleDeleteAllCourses() {
        if (tblSubjects.getItems().isEmpty()) {
            AlertComponent.showWarning("Thông báo", null, "Không có môn học nào để xóa!");
            return;
        }

        boolean confirm = AlertComponent.showConfirmation("Xác nhận", null,
                "Bạn có chắc chắn muốn xóa toàn bộ môn học trong hệ thống?\nHành động này không thể hoàn tác!");

        if (!confirm)
            return;

        String sql = "DELETE FROM courses";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                AlertComponent.showInformation("Thành công", null, "Đã xóa toàn bộ môn học!");
                showCoursesList(); // Cập nhật lại danh sách môn học

                updateStudentSubjectsComboBox();
            } else {
                AlertComponent.showWarning("Thông báo", null, "Không có môn học nào để xóa!");
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

    // Phương thức cập nhật môn học
    private void handleUpdateCourse() {
        CourseData selectedCourse = tblSubjects.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một môn học để cập nhật!");
            return;
        }

        if (!validateCourseInput())
            return;

        String courseID = selectedCourse.getSubjectID();
        String courseName = txtSubjectName.getText().trim();
        int credits = Integer.parseInt(txtCredits.getText().trim());
        String selectedLecturer = cbLecturer.getValue();

        if (selectedLecturer.equals("Chọn") || selectedLecturer == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn giảng viên!");
            return;
        }

        // Nếu giảng viên không thay đổi, lấy `lecturer_id` từ `UserData`
        String lecturerID = (String) cbLecturer.getUserData();

        // Nếu người dùng chọn lại giảng viên, lấy `lecturer_id` từ chuỗi "GV001 - TS.
        // Nguyễn Văn A"
        if (cbLecturer.getSelectionModel().getSelectedIndex() != -1) {
            lecturerID = selectedLecturer.split(" - ")[0];
        }

        // Kiểm tra xem `lecturer_id` có tồn tại không
        if (!isLecturerIDExists(lecturerID)) {
            AlertComponent.showError("Lỗi", null, "Mã giảng viên không tồn tại!");
            return;
        }

        String semester = cbSemester.getValue();
        String status = cbStatus.getValue();

        String updateQuery = "UPDATE courses SET course_name = ?, credits = ?, lecturer = ?, semester = ?, status = ? WHERE course_id = ?";

        try {
            connect = database.connectDB();
            preparedStatement = connect.prepareStatement(updateQuery);
            preparedStatement.setString(1, courseName);
            preparedStatement.setInt(2, credits);
            preparedStatement.setString(3, lecturerID);
            preparedStatement.setString(4, semester);
            preparedStatement.setString(5, status);
            preparedStatement.setString(6, courseID);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                AlertComponent.showInformation("Thành công", null, "Môn học đã được cập nhật!");
                showCoursesList();
                clearFormSubject();
            } else {
                AlertComponent.showError("Lỗi", null, "Không thể cập nhật môn học!");
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

    // Phương thức Update ComboBox môn học trong Quản lý sinh viên
    private void updateStudentSubjectsComboBox() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/student_form.fxml"));
            Parent root = loader.load();

            studentController studentCtrl = loader.getController();
            studentCtrl.loadSubjectsIntoComboBox(); // Cập nhật danh sách môn học

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mở file Subjects
    private void openSubjectFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file môn học");

        // Thêm các bộ lọc cho JSON, Excel, CSV, và All Files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"), // Hiển thị tất cả file
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Đặt "All Files" làm bộ lọc mặc định để hiển thị tất cả file
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            processSubjectFile(file);
        }
    }

    // Xử lý file theo subject
    private void processSubjectFile(File file) {
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".json")) {
            importFromJsonSubject(file);
        } else if (fileName.endsWith(".xlsx")) {
            importFromExcelSubject(file);
        } else if (fileName.endsWith(".csv")) {
            importFromCsvSubject(file);
        } else {
            AlertComponent.showWarning("Lỗi", null, "Định dạng file không hợp lệ! Vui lòng chọn JSON, Excel hoặc CSV.");
        }
    }

    // Import theo JSON
    private void importFromJsonSubject(File file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<CourseData> courses = objectMapper.readValue(Paths.get(file.getAbsolutePath()).toFile(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CourseData.class));

            int duplicateCount = 0; // Đếm số môn học trùng mã môn học
            int successCount = 0; // Đếm số môn học thêm thành công
            int errorCount = 0; // Đếm số lỗi khác

            for (CourseData course : courses) {
                try {
                    insertCourseIntoDatabase(course);
                    successCount++;
                } catch (SQLException e) {
                    if (e.getMessage().contains("Mã môn học")) {
                        // Nếu lỗi là do trùng mã môn học, tăng đếm và bỏ qua
                        duplicateCount++;
                        AlertComponent.showWarning("Lỗi", null, "Môn học trùng mã: " + course.getSubjectID());
                    } else if (e.getMessage().contains("lecturer_id")) {
                        // Nếu lỗi là do lecturer_id không tồn tại
                        duplicateCount++;
                        AlertComponent.showError("Lỗi", null,
                                "Không tìm thấy mã giảng viên cho môn học: " + course.getSubjectID());
                    } else {
                        // Nếu lỗi khác, tăng đếm lỗi và thông báo
                        errorCount++;
                        AlertComponent.showError("Lỗi", null,
                                "Lỗi khi thêm môn học " + course.getSubjectID() + ": " + e.getMessage());
                    }
                }
            }

            // Tạo thông báo dựa trên kết quả
            String message = "Đã thêm " + successCount + " môn học thành công từ JSON!";
            if (duplicateCount > 0) {
                message += "\nCó " + duplicateCount + " môn học bị bỏ qua (trùng mã hoặc lỗi giảng viên).";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " lỗi khác xảy ra.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            showCoursesList(); // Cập nhật danh sách môn học trên TableView
            updateStudentSubjectsComboBox(); // Cập nhật danh sách môn học trong ComboBox của sinh viên
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập môn học từ JSON: " + e.getMessage());
        }
    }

    // Import theo excel
    private void importFromExcelSubject(File file) {
        try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis);
                Connection connect = database.connectDB()) {

            PreparedStatement preparedStatement = connect.prepareStatement(
                    "INSERT INTO courses (course_id, course_name, credits, lecturer, semester, status) VALUES (?, ?, ?, ?, ?, ?)");

            Sheet sheet = workbook.getSheetAt(0);
            int successCount = 0;
            int duplicateCount = 0;
            int errorCount = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue; // Bỏ qua dòng tiêu đề

                try {
                    String courseID = getCellStringValue(row.getCell(0));
                    String courseName = getCellStringValue(row.getCell(1));
                    String creditsStr = getCellStringValue(row.getCell(2));
                    String lecturerName = getCellStringValue(row.getCell(3)); // Giả sử dùng tên giảng viên trong Excel
                    String semester = getCellStringValue(row.getCell(4));
                    String status = getCellStringValue(row.getCell(5));

                    if (courseID == null || courseName == null || creditsStr == null || lecturerName == null ||
                            semester == null || status == null) {
                        throw new IllegalArgumentException("Dữ liệu trống tại dòng " + (row.getRowNum() + 1));
                    }

                    // Chuyển credits từ chuỗi sang int
                    int credits;
                    try {
                        credits = Integer.parseInt(creditsStr);
                        if (credits <= 0) {
                            throw new NumberFormatException("Số tín chỉ phải là số nguyên dương");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Số tín chỉ không hợp lệ tại dòng " + (row.getRowNum() + 1));
                    }

                    // Lấy lecturer_id từ lecturer_name
                    String lecturerID = getLecturerIDByName(lecturerName);
                    if (lecturerID == null) {
                        throw new SQLException("Không tìm thấy mã giảng viên cho tên: " + lecturerName + " tại dòng "
                                + (row.getRowNum() + 1));
                    }

                    CourseData course = new CourseData(courseID, courseName, credits, lecturerName, semester, status);

                    if (!isCourseIDExists(courseID)) {
                        preparedStatement.setString(1, courseID);
                        preparedStatement.setString(2, courseName);
                        preparedStatement.setInt(3, credits);
                        preparedStatement.setString(4, lecturerID);
                        preparedStatement.setString(5, semester);
                        preparedStatement.setString(6, status);
                        preparedStatement.addBatch();
                        successCount++;
                    } else {
                        duplicateCount++;
                        AlertComponent.showWarning("Lỗi", null,
                                "Môn học trùng mã: " + courseID + " tại dòng " + (row.getRowNum() + 1));
                    }
                } catch (Exception e) {
                    if (e instanceof SQLException) {
                        if (e.getMessage().contains("lecturer_id")) {
                            duplicateCount++;
                            AlertComponent.showError("Lỗi", null, "Không tìm thấy mã giảng viên tại dòng "
                                    + (row.getRowNum() + 1) + ": " + e.getMessage());
                        } else {
                            errorCount++;
                            AlertComponent.showError("Lỗi", null,
                                    "Lỗi SQL tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                        }
                    } else if (e instanceof IllegalArgumentException) {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null,
                                "Dòng lỗi tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                    } else {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null,
                                "Dòng lỗi tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                    }
                }
            }

            if (successCount > 0) {
                preparedStatement.executeBatch();
            }

            String message = "Đã nhập " + successCount + " môn học thành công từ Excel!";
            if (duplicateCount > 0) {
                message += "\nCó " + duplicateCount + " môn học trùng mã hoặc lỗi giảng viên bị bỏ qua.";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " dòng lỗi khác.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            showCoursesList(); // Cập nhật danh sách trên TableView
            updateStudentSubjectsComboBox(); // Cập nhật danh sách môn học trong ComboBox của sinh viên
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập môn học từ Excel: " + e.getMessage());
        }
    }

    // Import theo CSV
    private void importFromCsvSubject(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            int successCount = 0;
            int duplicateCount = 0;
            int errorCount = 0;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    String[] data = line.split(",");
                    if (data.length < 6) {
                        throw new IllegalArgumentException("Dữ liệu không đủ tại dòng hiện tại");
                    }

                    String courseID = data[0].trim();
                    String courseName = data[1].trim();
                    String creditsStr = data[2].trim();
                    String lecturerName = data[3].trim();
                    String semester = data[4].trim();
                    String status = data[5].trim();

                    if (courseID.isEmpty() || courseName.isEmpty() || creditsStr.isEmpty() || lecturerName.isEmpty() ||
                            semester.isEmpty() || status.isEmpty()) {
                        throw new IllegalArgumentException("Dữ liệu trống tại dòng hiện tại");
                    }

                    // Chuyển credits từ chuỗi sang int
                    int credits;
                    try {
                        credits = Integer.parseInt(creditsStr);
                        if (credits <= 0) {
                            throw new NumberFormatException("Số tín chỉ phải là số nguyên dương");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Số tín chỉ không hợp lệ tại dòng hiện tại");
                    }

                    // Lấy lecturer_id từ lecturer_name
                    String lecturerID = getLecturerIDByName(lecturerName);
                    if (lecturerID == null) {
                        throw new SQLException(
                                "Không tìm thấy mã giảng viên cho tên: " + lecturerName + " tại dòng hiện tại");
                    }

                    CourseData course = new CourseData(courseID, courseName, credits, lecturerName, semester, status);

                    if (!isCourseIDExists(courseID)) {
                        insertCourseIntoDatabase(course);
                        successCount++;
                    } else {
                        duplicateCount++;
                        AlertComponent.showWarning("Lỗi", null, "Môn học trùng mã: " + courseID + " tại dòng hiện tại");
                    }
                } catch (Exception e) {
                    if (e instanceof SQLException) {
                        if (e.getMessage().contains("lecturer_id")) {
                            duplicateCount++;
                            AlertComponent.showError("Lỗi", null,
                                    "Không tìm thấy mã giảng viên tại dòng hiện tại: " + e.getMessage());
                        } else {
                            errorCount++;
                            AlertComponent.showError("Lỗi", null, "Lỗi SQL tại dòng hiện tại: " + e.getMessage());
                        }
                    } else if (e instanceof IllegalArgumentException) {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null, "Dòng lỗi tại dòng hiện tại: " + e.getMessage());
                    } else {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null, "Dòng lỗi tại dòng hiện tại: " + e.getMessage());
                    }
                }
            }

            String message = "Đã nhập " + successCount + " môn học thành công từ CSV!";
            if (duplicateCount > 0) {
                message += "\nCó " + duplicateCount + " môn học trùng mã hoặc lỗi giảng viên bị bỏ qua.";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " dòng lỗi khác.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            showCoursesList(); // Cập nhật danh sách trên TableView
            updateStudentSubjectsComboBox(); // Cập nhật danh sách môn học trong ComboBox của sinh viên
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập môn học từ CSV: " + e.getMessage());
        }
    }

    // Thêm danh sách môn học từ file -> database.
    private void insertCourseIntoDatabase(CourseData course) throws SQLException {
        String sql = "INSERT INTO courses (course_id, course_name, credits, lecturer, semester, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            String lecturerID = getLecturerIDByName(course.getLecturer(), true); // Sử dụng phiên bản chuẩn hóa
            if (lecturerID == null) {
                throw new SQLException("Không tìm thấy mã giảng viên cho tên: " + course.getLecturer());
            }

            if (isCourseIDExists(course.getSubjectID())) {
                throw new SQLException("Mã môn học " + course.getSubjectID() + " đã tồn tại trong database.");
            }

            preparedStatement.setString(1, course.getSubjectID());
            preparedStatement.setString(2, course.getSubjectName());
            preparedStatement.setInt(3, course.getCredits());
            preparedStatement.setString(4, lecturerID);
            preparedStatement.setString(5, course.getSemester());
            preparedStatement.setString(6, course.getStatus());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không thể thêm môn học vào database.");
            }
        }
    }

    // Xuất file môn học
    private void downloadSubjectsFile() {
        // Hiển thị dialog để người dùng chọn định dạng file
        ChoiceDialog<String> dialog = new ChoiceDialog<>("JSON", "JSON", "Excel", "CSV");
        dialog.setTitle("Chọn định dạng file");
        dialog.setHeaderText("Vui lòng chọn định dạng file để xuất danh sách môn học:");
        dialog.setContentText("Định dạng:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Lưu file danh sách môn học");

            // Thêm các bộ lọc tương ứng với định dạng được chọn
            switch (result.get()) {
                case "JSON":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
                    fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
                    break;
                case "Excel":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                    fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
                    break;
                case "CSV":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                    fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
                    break;
            }

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    // Xuất file chính (JSON, Excel, hoặc CSV)
                    switch (result.get()) {
                        case "JSON":
                            exportCoursesToJson(file);
                            break;
                        case "Excel":
                            exportCoursesToExcel(file);
                            break;
                        case "CSV":
                            exportCoursesToCsv(file);
                            break;
                    }
                    AlertComponent.showInformation("Thành công", null, "Đã xuất file thành công: " + file.getName());

                    // Tạo tên file .txt dựa trên tên file chính (thay .json/.xlsx/.csv bằng .txt)
                    String txtFileName = file.getAbsolutePath().replaceAll("\\.(json|xlsx|csv)$", ".txt");
                    File instructionFile = new File(txtFileName);

                    // Kiểm tra nếu file .txt chưa tồn tại, thì xuất hướng dẫn
                    if (!instructionFile.exists()) {
                        exportInstructionsToTxt(instructionFile);
                        AlertComponent.showInformation("Thành công", null,
                                "Đã xuất file hướng dẫn: " + instructionFile.getName());
                    }
                } catch (Exception e) {
                    AlertComponent.showError("Lỗi", null, "Không thể xuất file: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    // Xuất danh sách môn học sang file JSON
    private void exportCoursesToJson(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("MM/dd/yyyy")); // Định dạng ngày (nếu cần)

        ObservableList<CourseData> courses = tblSubjects.getItems();
        if (courses == null || courses.isEmpty()) {
            // Nếu TableView trống, lấy từ database
            courses = getCoursesListData();
            if (courses == null || courses.isEmpty()) {
                // Nếu database trống, xuất chỉ tiêu đề cột
                List<String> headers = Arrays.asList("course_id", "course_name", "credits", "lecturer", "semester",
                        "status");
                objectMapper.writeValue(file, headers);
            } else {
                objectMapper.writeValue(file, courses);
            }
        } else {
            // Nếu TableView có dữ liệu, xuất trực tiếp
            objectMapper.writeValue(file, courses);
        }
    }

    // Xuất danh sách môn học sang file Excel
    private void exportCoursesToExcel(File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Courses");

        ObservableList<CourseData> courses = tblSubjects.getItems();
        int rowNum = 0;

        // Tạo tiêu đề cột
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = { "course_id", "course_name", "credits", "lecturer", "semester", "status" };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        if (courses == null || courses.isEmpty()) {
            // Nếu TableView trống, lấy từ database
            courses = getCoursesListData();
            if (courses != null && !courses.isEmpty()) {
                // Nếu database có dữ liệu, xuất danh sách
                for (CourseData course : courses) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(course.getSubjectID());
                    row.createCell(1).setCellValue(course.getSubjectName());
                    row.createCell(2).setCellValue(course.getCredits());
                    row.createCell(3).setCellValue(course.getLecturer());
                    row.createCell(4).setCellValue(course.getSemester());
                    row.createCell(5).setCellValue(course.getStatus());
                }
            }
        } else {
            // Nếu TableView có dữ liệu, xuất danh sách
            for (CourseData course : courses) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(course.getSubjectID());
                row.createCell(1).setCellValue(course.getSubjectName());
                row.createCell(2).setCellValue(course.getCredits());
                row.createCell(3).setCellValue(course.getLecturer());
                row.createCell(4).setCellValue(course.getSemester());
                row.createCell(5).setCellValue(course.getStatus());
            }
        }

        // Tự động điều chỉnh chiều rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    // Xuất danh sách môn học sang file CSV
    private void exportCoursesToCsv(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String[] headers = { "course_id", "course_name", "credits", "lecturer", "semester", "status" };
            StringBuilder line = new StringBuilder();

            // Ghi tiêu đề
            for (int i = 0; i < headers.length; i++) {
                line.append(headers[i]);
                if (i < headers.length - 1)
                    line.append(",");
            }
            writer.write(line.toString());
            writer.newLine();

            ObservableList<CourseData> courses = tblSubjects.getItems();
            if (courses != null && !courses.isEmpty()) {
                for (CourseData course : courses) {
                    line.setLength(0); // Xóa nội dung cũ
                    line.append(course.getSubjectID()).append(",")
                            .append(course.getSubjectName()).append(",")
                            .append(course.getCredits()).append(",")
                            .append("\"" + course.getLecturer() + "\"").append(",")
                            .append(course.getSemester()).append(",")
                            .append(course.getStatus());
                    writer.write(line.toString());
                    writer.newLine();
                }
            } else {
                // Nếu TableView trống, lấy từ database
                courses = getCoursesListData();
                if (courses != null && !courses.isEmpty()) {
                    for (CourseData course : courses) {
                        line.setLength(0); // Xóa nội dung cũ
                        line.append(course.getSubjectID()).append(",")
                                .append(course.getSubjectName()).append(",")
                                .append(course.getCredits()).append(",")
                                .append("\"" + course.getLecturer() + "\"").append(",")
                                .append(course.getSemester()).append(",")
                                .append(course.getStatus());
                        writer.write(line.toString());
                        writer.newLine();
                    }
                }
            }
        }
    }

    // ========== QUẢN LÝ MÔN HỌC ==========

    // ========== QUẢN LÝ GIẢNG VIÊN ==========

    // Danh sách lecturerListData
    private ObservableList<LecturerData> lecturerListData;

    // Lấy danh sách giảng viên từ database
    public ObservableList<LecturerData> getLecturerListData() {
        ObservableList<LecturerData> lecturerList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM lecturers"; // Bảng giảng viên

        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                lecturerList.add(new LecturerData(
                        resultSet.getString("lecturer_id"),
                        resultSet.getString("lecturer_name"),
                        resultSet.getString("gender"),
                        resultSet.getString("degree"),
                        resultSet.getString("phone"),
                        resultSet.getString("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lecturerList;
    }

    // Hiển thị danh sách giảng viên lên TableView
    public void showLecturersList() {
        lecturerListData = getLecturerListData();

        colLecturerID.setCellValueFactory(new PropertyValueFactory<>("lecturerID"));
        colLecturerName.setCellValueFactory(new PropertyValueFactory<>("lecturerName"));
        colLecturerGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colLecturerDegree.setCellValueFactory(new PropertyValueFactory<>("degree"));
        colLecturerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colLecturerStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableLecturer.setItems(lecturerListData);
    }

    // Cập nhật giảng viên vào comboBox trong quản lý môn học
    private void updateLecturersInCourse() {
        ObservableList<String> lecturerList = FXCollections.observableArrayList();
        String sql = "SELECT lecturer_id, lecturer_name, degree, status FROM lecturers";

        connect = database.connectDB();
        try {
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String lecturerID = resultSet.getString("lecturer_id");
                String lecturerName = resultSet.getString("lecturer_name");
                String degree = resultSet.getString("degree");
                String status = resultSet.getString("status");

                // Chỉ hiển thị giảng viên đang giảng dạy
                if (status.equalsIgnoreCase("Nghỉ phép") || status.equalsIgnoreCase("Đã nghỉ hưu")) {
                    continue;
                }

                // Hiển thị tên giảng viên nhưng lưu lecturer_id
                lecturerList.add(lecturerID + " - " + formatLecturerName(lecturerName, degree));
            }
            cbLecturer.setItems(lecturerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Định dạng tên giảng viên theo học vị
     */
    private String formatLecturerName(String name, String degree) {
        switch (degree) {
            case "Cử nhân":
                return "CN. " + name;
            case "Thạc sĩ":
                return "ThS. " + name;
            case "Tiến sĩ":
                return "TS. " + name;
            case "Phó giáo sư":
                return "PGS. " + name;
            case "Giáo sư":
                return "GS. " + name;
            default:
                return name; // Nếu không có học vị, trả về nguyên tên
        }
    }

    // Kiểm tra dữ liệu nhập vào input
    private boolean validateLecturerInput() {
        if (txtLecturerID.getText().trim().isEmpty() ||
                txtLecturerName.getText().trim().isEmpty() ||
                cbLecturerGender.getValue() == null || cbLecturerGender.getValue().equals("Chọn") ||
                cbLecturerDegree.getValue() == null || cbLecturerDegree.getValue().equals("Chọn") ||
                txtLecturerPhone.getText().trim().isEmpty() ||
                cbLecturerStatus.getValue() == null || cbLecturerStatus.getValue().equals("Chọn")) {

            AlertComponent.showWarning("Lỗi nhập liệu", null, "Vui lòng điền đầy đủ thông tin!");
            return false;
        }

        // Kiểm tra mã giảng viên phải bắt đầu -> GV -> Số
        if (!txtLecturerID.getText().trim().matches("^GV\\d+$")) {
            AlertComponent.showWarning("Lỗi nhập liệu", null,
                    "Mã giảng viên phải bắt đầu bằng 'GV' và theo sau là số!");
            return false;
        }

        // Kiểm tra xem số điện thoại đủ 10-11 chữ số không
        if (!txtLecturerPhone.getText().trim().matches("^\\d{10,11}$")) {
            AlertComponent.showWarning("Lỗi nhập liệu", null, "Số điện thoại phải có 10-11 chữ số!");
            return false;
        }

        return true;
    }

    // Kiểm tra xem lecturer_id có tồn tại trong bảng lecturers không
    private boolean isLecturerIDExists(String lecturerID) {
        String sql = "SELECT COUNT(*) FROM lecturers WHERE lecturer_id = ?";
        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            preparedStatement.setString(1, lecturerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            AlertComponent.showError("Lỗi", null, "Không thể kiểm tra mã giảng viên: " + e.getMessage());
            return false;
        }
        return false;
    }

    // Kiểm tra số điện thoại đã tồn tại trong database (trừ giảng viên hiện tại)
    private boolean isPhoneNumberExists(String phone, String currentLecturerID) {
        String sql = "SELECT COUNT(*) FROM lecturers WHERE phone = ? AND lecturer_id != ?";
        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, currentLecturerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            AlertComponent.showError("Lỗi", null, "Không thể kiểm tra số điện thoại: " + e.getMessage());
            return false;
        }
        return false;
    }

    // Phương thức thêm giảng viên vào database và load vào tableview
    private void handleAddLecturer() {
        if (!validateLecturerInput())
            return;

        String lecturerID = txtLecturerID.getText().trim();
        String lecturerName = txtLecturerName.getText().trim();
        String gender = cbLecturerGender.getValue();
        String degree = cbLecturerDegree.getValue();
        String phone = txtLecturerPhone.getText().trim();
        String status = cbLecturerStatus.getValue();

        // Kiểm tra trùng Mã giảng viên trong database
        if (isLecturerIDExists(lecturerID)) {
            AlertComponent.showWarning("Lỗi", null, "Mã giảng viên đã tồn tại! Vui lòng nhập mã khác.");
            return;
        }

        // Kiểm tra trùng Số điện thoại trong database
        if (isPhoneNumberExists(phone, lecturerID)) {
            AlertComponent.showWarning("Lỗi", null, "Số điện thoại đã tồn tại! Vui lòng nhập số khác.");
            return;
        }

        String sql = "INSERT INTO lecturers (lecturer_id, lecturer_name, gender, degree, phone, status) VALUES (?, ?, ?, ?, ?, ?)";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, lecturerID);
            preparedStatement.setString(2, lecturerName);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, degree);
            preparedStatement.setString(5, phone);
            preparedStatement.setString(6, status);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                AlertComponent.showInformation("Thành công", null, "Giảng viên đã được thêm thành công!");
                showLecturersList(); // Cập nhật danh sách giảng viên vào TableView -> Bảng
                updateLecturersInCourse(); // Cập nhật danh sách giảng viên vào ComboBox trong Quản lý môn học
                handleClearFormLecturer(); // Xoá form nhập liệu khi thêm thành công giảng viên
            } else {
                AlertComponent.showError("Lỗi", null, "Không thể thêm giảng viên!");
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

    // Nhấp đôi vào giảng viên -> chọn giảng viên
    private void loadSelectedLecturerData() {
        LecturerData selectedLecturer = tableLecturer.getSelectionModel().getSelectedItem();

        if (selectedLecturer == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một giảng viên!");
            return;
        }

        // Hiển thị dữ liệu vào các ô nhập
        txtLecturerID.setText(selectedLecturer.getLecturerID());
        txtLecturerName.setText(selectedLecturer.getLecturerName());
        cbLecturerGender.setValue(selectedLecturer.getGender());
        cbLecturerDegree.setValue(selectedLecturer.getDegree());
        txtLecturerPhone.setText(selectedLecturer.getPhone());
        cbLecturerStatus.setValue(selectedLecturer.getStatus());

        // Vô hiệu hóa ô Mã giảng viên
        txtLecturerID.setDisable(true);
    }

    // Phương thức cập nhật giảng viên
    private void handleUpdateLecturer() {
        // Lấy giảng viên được chọn từ TableView
        LecturerData selectedLecturer = tableLecturer.getSelectionModel().getSelectedItem();

        if (selectedLecturer == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một giảng viên để cập nhật!");
            return;
        }

        // Kiểm tra xem các trường nhập có hợp lệ không
        if (!validateLecturerInput())
            return;

        String lecturerID = selectedLecturer.getLecturerID(); // Không cho sửa mã giảng viên
        String lecturerName = txtLecturerName.getText().trim();
        String gender = cbLecturerGender.getValue();
        String degree = cbLecturerDegree.getValue();
        String phone = txtLecturerPhone.getText().trim();
        String status = cbLecturerStatus.getValue();

        // Kiểm tra số điện thoại có bị trùng không (trừ giảng viên hiện tại)
        if (isPhoneNumberExists(phone, lecturerID)) {
            AlertComponent.showWarning("Lỗi nhập dữ liệu", null,
                    "Số điện thoại này đã được sử dụng bởi một giảng viên khác!");
            return;
        }

        // Xác nhận trước khi cập nhật
        boolean confirm = AlertComponent.showConfirmation("Xác nhận cập nhật", null,
                "Bạn có chắc chắn muốn cập nhật thông tin giảng viên: " + lecturerName + " không?");

        if (!confirm)
            return;

        String sql = "UPDATE lecturers SET lecturer_name = ?, gender = ?, degree = ?, phone = ?, status = ? WHERE lecturer_id = ?";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, lecturerName);
            preparedStatement.setString(2, gender);
            preparedStatement.setString(3, degree);
            preparedStatement.setString(4, phone);
            preparedStatement.setString(5, status);
            preparedStatement.setString(6, lecturerID);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                AlertComponent.showInformation("Thành công", null, "Thông tin giảng viên đã được cập nhật!");
                showLecturersList(); // Cập nhật lại danh sách giảng viên
                updateLecturersInCourse(); // Cập nhật danh sách giảng viên trong Quản lý Môn học
                handleClearFormLecturer(); // Xoá form nhập liệu
            } else {
                AlertComponent.showError("Lỗi", null, "Cập nhật không thành công!");
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

    // Phương thức xoá giảng viên
    private void handleDeleteLecturer() {
        // Lấy giảng viên được chọn từ TableView
        LecturerData selectedLecturer = tableLecturer.getSelectionModel().getSelectedItem();

        if (selectedLecturer == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một giảng viên để xóa!");
            return;
        }

        // Hiển thị xác nhận trước khi xóa
        boolean confirm = AlertComponent.showConfirmation("Xác nhận", null,
                "Bạn có chắc chắn muốn xóa giảng viên: " + selectedLecturer.getLecturerName() + " không?");

        if (!confirm)
            return;

        String sql = "DELETE FROM lecturers WHERE lecturer_id = ?";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, selectedLecturer.getLecturerID());
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                AlertComponent.showInformation("Thành công", null, "Giảng viên đã được xóa!");
                showLecturersList(); // Cập nhật danh sách giảng viên trong TableView
                updateLecturersInCourse(); // Cập nhật danh sách giảng viên trong Quản lý môn học
            } else {
                AlertComponent.showError("Lỗi", null, "Không thể xóa giảng viên!");
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

    // Phương thức xoá toàn bộ giảng viên
    private void handleClearAllLecturer() {
        if (tableLecturer.getItems().isEmpty()) {
            AlertComponent.showWarning("Thông báo", null, "Không có giảng viên nào để xóa!");
            return;
        }

        // Hiển thị cảnh báo trước khi xóa toàn bộ dữ liệu
        boolean confirm = AlertComponent.showConfirmation("Xác nhận", null,
                "Bạn có chắc chắn muốn xóa toàn bộ giảng viên trong hệ thống?\nHành động này không thể hoàn tác!");

        if (!confirm)
            return;

        String sql = "DELETE FROM lecturers";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                AlertComponent.showInformation("Thành công", null, "Đã xóa toàn bộ giảng viên!");
                showLecturersList(); // Cập nhật danh sách giảng viên trong TableView
                updateLecturersInCourse(); // Cập nhật danh sách giảng viên trong Quản lý môn học
            } else {
                AlertComponent.showWarning("Thông báo", null, "Không có giảng viên nào để xóa!");
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

    // Phương thức xoá toàn bộ dữ liệu trong form nhập liệu giảng viên
    private void handleClearFormLecturer() {
        // Xử lý các logic khi xoá form nhập liệu
        txtLecturerID.setDisable(false); // Mở khóa ô nhập mã giảng viên
        btnAddLecturer.setDisable(false); // Mở khóa nút thêm giảng viên

        // Clear all forms
        txtLecturerID.clear();
        txtLecturerName.clear();
        txtLecturerPhone.clear();
        cbLecturerGender.setValue("Chọn");
        cbLecturerDegree.setValue("Chọn");
        cbLecturerStatus.setValue("Chọn");
    }

    // Chọn file danh sách giảng viên
    private void openLecturerFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file giảng viên");

        // Thêm các bộ lọc cho JSON, Excel, CSV, và All Files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"), // Hiển thị tất cả file
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Đặt "All Files" làm bộ lọc mặc định để hiển thị tất cả file
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            processLecturerFile(file);
        }
    }

    // Danh sách các file hợp lệ
    private void processLecturerFile(File file) {
        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".json")) {
            importFromJsonLecturer(file);
        } else if (fileName.endsWith(".xlsx")) {
            importFromExcelLecturer(file);
        } else if (fileName.endsWith(".csv")) {
            importFromCsvLecturer(file);
        } else {
            AlertComponent.showWarning("Lỗi", null, "Định dạng file không hợp lệ! Vui lòng chọn JSON, Excel hoặc CSV.");
        }
    }

    // Import Json
    private void importFromJsonLecturer(File file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<LecturerData> lecturers = objectMapper.readValue(Paths.get(file.getAbsolutePath()).toFile(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, LecturerData.class));

            int duplicateIDCount = 0; // Đếm số lecturer_id trùng
            int duplicatePhoneCount = 0; // Đếm số phone trùng
            int successCount = 0; // Đếm số giảng viên thêm thành công
            int errorCount = 0; // Đếm số lỗi khác

            for (LecturerData lecturer : lecturers) {
                try {
                    if (insertLecturerIntoDatabase(lecturer)) {
                        successCount++;
                    }
                } catch (Exception e) {
                    if (e.getMessage().contains("lecturer_id")) {
                        duplicateIDCount++;
                        AlertComponent.showWarning("Lỗi", null, "Giảng viên trùng mã: " + lecturer.getLecturerID());
                    } else if (e.getMessage().contains("phone")) {
                        duplicatePhoneCount++;
                        AlertComponent.showWarning("Lỗi", null,
                                "Số điện thoại đã tồn tại cho giảng viên: " + lecturer.getLecturerID());
                    } else {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null,
                                "Lỗi khi thêm giảng viên " + lecturer.getLecturerID() + ": " + e.getMessage());
                    }
                }
            }

            String message = "Đã thêm " + successCount + " giảng viên thành công từ JSON!";
            if (duplicateIDCount > 0) {
                message += "\nCó " + duplicateIDCount + " giảng viên trùng mã bị bỏ qua.";
            }
            if (duplicatePhoneCount > 0) {
                message += "\nCó " + duplicatePhoneCount + " số điện thoại trùng bị bỏ qua.";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " lỗi khác xảy ra.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            showLecturersList(); // Cập nhật danh sách giảng viên trên TableView
            updateLecturersInCourse(); // Cập nhật danh sách giảng viên trong ComboBox của môn học
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập giảng viên từ JSON: " + e.getMessage());
        }
    }

    // Import Excel
    private void importFromExcelLecturer(File file) {
        try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis);
                Connection connect = database.connectDB()) {

            PreparedStatement preparedStatement = connect.prepareStatement(
                    "INSERT INTO lecturers (lecturer_id, lecturer_name, gender, degree, phone, status) VALUES (?, ?, ?, ?, ?, ?)");

            Sheet sheet = workbook.getSheetAt(0);
            int successCount = 0;
            int duplicateIDCount = 0;
            int duplicatePhoneCount = 0;
            int errorCount = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue; // Bỏ qua dòng tiêu đề

                try {
                    String lecturerID = getCellStringValue(row.getCell(0));
                    String lecturerName = getCellStringValue(row.getCell(1));
                    String gender = getCellStringValue(row.getCell(2));
                    String degree = getCellStringValue(row.getCell(3));
                    String phone = getCellStringValue(row.getCell(4));
                    String status = getCellStringValue(row.getCell(5));

                    if (lecturerID == null || lecturerName == null || gender == null || degree == null ||
                            phone == null || status == null) {
                        throw new IllegalArgumentException("Dữ liệu trống tại dòng " + (row.getRowNum() + 1));
                    }

                    // Kiểm tra định dạng lecturer_id (bắt đầu bằng "GV" và theo sau là số)
                    if (!lecturerID.matches("^GV\\d+$")) {
                        throw new IllegalArgumentException("Mã giảng viên không hợp lệ tại dòng "
                                + (row.getRowNum() + 1) + ": Phải bắt đầu bằng 'GV' và theo sau là số");
                    }

                    // Kiểm tra định dạng phone (10-11 chữ số)
                    if (!phone.matches("^\\d{10,11}$")) {
                        throw new IllegalArgumentException("Số điện thoại không hợp lệ tại dòng "
                                + (row.getRowNum() + 1) + ": Phải có 10-11 chữ số");
                    }

                    LecturerData lecturer = new LecturerData(lecturerID, lecturerName, gender, degree, phone, status);

                    if (!isLecturerIDExists(lecturerID) && !isPhoneNumberExists(phone, lecturerID)) {
                        preparedStatement.setString(1, lecturerID);
                        preparedStatement.setString(2, lecturerName);
                        preparedStatement.setString(3, gender);
                        preparedStatement.setString(4, degree);
                        preparedStatement.setString(5, phone);
                        preparedStatement.setString(6, status);
                        preparedStatement.addBatch();
                        successCount++;
                    } else {
                        if (isLecturerIDExists(lecturerID)) {
                            duplicateIDCount++;
                            AlertComponent.showWarning("Lỗi", null,
                                    "Giảng viên trùng mã: " + lecturerID + " tại dòng " + (row.getRowNum() + 1));
                        }
                        if (isPhoneNumberExists(phone, lecturerID)) {
                            duplicatePhoneCount++;
                            AlertComponent.showWarning("Lỗi", null, "Số điện thoại đã tồn tại cho giảng viên: "
                                    + lecturerID + " tại dòng " + (row.getRowNum() + 1));
                        }
                    }
                } catch (Exception e) {
                    errorCount++;
                    AlertComponent.showError("Lỗi", null,
                            "Dòng lỗi tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                }
            }

            if (successCount > 0) {
                preparedStatement.executeBatch();
            }

            String message = "Đã nhập " + successCount + " giảng viên thành công từ Excel!";
            if (duplicateIDCount > 0) {
                message += "\nCó " + duplicateIDCount + " mã giảng viên trùng bị bỏ qua.";
            }
            if (duplicatePhoneCount > 0) {
                message += "\nCó " + duplicatePhoneCount + " số điện thoại trùng bị bỏ qua.";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " dòng lỗi không thể nhập.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            showLecturersList(); // Cập nhật danh sách trên TableView
            updateLecturersInCourse(); // Cập nhật danh sách giảng viên trong ComboBox của môn học
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập giảng viên từ Excel: " + e.getMessage());
        }
    }

    // Import CSV
    private void importFromCsvLecturer(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            int successCount = 0;
            int duplicateIDCount = 0;
            int duplicatePhoneCount = 0;
            int errorCount = 0;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    String[] data = line.split(",");
                    if (data.length < 6) { // 6 cột: lecturer_id, lecturer_name, gender, degree, phone, status
                        throw new IllegalArgumentException("Dữ liệu không đủ tại dòng hiện tại");
                    }

                    String lecturerID = data[0].trim();
                    String lecturerName = data[1].trim();
                    String gender = data[2].trim();
                    String degree = data[3].trim();
                    String phone = data[4].trim();
                    String status = data[5].trim();

                    if (lecturerID.isEmpty() || lecturerName.isEmpty() || gender.isEmpty() || degree.isEmpty() ||
                            phone.isEmpty() || status.isEmpty()) {
                        throw new IllegalArgumentException("Dữ liệu trống tại dòng hiện tại");
                    }

                    // Kiểm tra định dạng lecturer_id (bắt đầu bằng "GV" và theo sau là số)
                    if (!lecturerID.matches("^GV\\d+$")) {
                        throw new IllegalArgumentException(
                                "Mã giảng viên không hợp lệ tại dòng hiện tại: Phải bắt đầu bằng 'GV' và theo sau là số");
                    }

                    // Kiểm tra định dạng phone (10-11 chữ số)
                    if (!phone.matches("^\\d{10,11}$")) {
                        throw new IllegalArgumentException(
                                "Số điện thoại không hợp lệ tại dòng hiện tại: Phải có 10-11 chữ số");
                    }

                    LecturerData lecturer = new LecturerData(lecturerID, lecturerName, gender, degree, phone, status);

                    if (!isLecturerIDExists(lecturerID) && !isPhoneNumberExists(phone, lecturerID)) {
                        insertLecturerIntoDatabase(lecturer);
                        successCount++;
                    } else {
                        if (isLecturerIDExists(lecturerID)) {
                            duplicateIDCount++;
                            AlertComponent.showWarning("Lỗi", null,
                                    "Giảng viên trùng mã: " + lecturerID + " tại dòng hiện tại");
                        }
                        if (isPhoneNumberExists(phone, lecturerID)) {
                            duplicatePhoneCount++;
                            AlertComponent.showWarning("Lỗi", null,
                                    "Số điện thoại đã tồn tại cho giảng viên: " + lecturerID + " tại dòng hiện tại");
                        }
                    }
                } catch (Exception e) {
                    errorCount++;
                    AlertComponent.showError("Lỗi", null, "Dòng lỗi tại dòng hiện tại: " + e.getMessage());
                }
            }

            String message = "Đã nhập " + successCount + " giảng viên thành công từ CSV!";
            if (duplicateIDCount > 0) {
                message += "\nCó " + duplicateIDCount + " mã giảng viên trùng bị bỏ qua.";
            }
            if (duplicatePhoneCount > 0) {
                message += "\nCó " + duplicatePhoneCount + " số điện thoại trùng bị bỏ qua.";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " dòng lỗi không thể nhập.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            showLecturersList(); // Cập nhật danh sách trên TableView
            updateLecturersInCourse(); // Cập nhật danh sách giảng viên trong ComboBox của môn học
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập giảng viên từ CSV: " + e.getMessage());
        }
    }

    // Load danh sách từ file vào database
    private boolean insertLecturerIntoDatabase(LecturerData lecturer) {
        String sql = "INSERT INTO lecturers (lecturer_id, lecturer_name, gender, degree, phone, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            if (isLecturerIDExists(lecturer.getLecturerID())) {
                AlertComponent.showWarning("Lỗi", null, "Giảng viên trùng mã: " + lecturer.getLecturerID());
                return false;
            }

            if (isPhoneNumberExists(lecturer.getPhone(), lecturer.getLecturerID())) {
                AlertComponent.showWarning("Lỗi", null,
                        "Số điện thoại đã tồn tại cho giảng viên: " + lecturer.getLecturerID());
                return false;
            }

            // Kiểm tra định dạng lecturer_id (bắt đầu bằng "GV" và theo sau là số)
            if (!lecturer.getLecturerID().matches("^GV\\d+$")) {
                AlertComponent.showError("Lỗi", null,
                        "Mã giảng viên không hợp lệ: Phải bắt đầu bằng 'GV' và theo sau là số");
                return false;
            }

            // Kiểm tra định dạng phone (10-11 chữ số)
            if (!lecturer.getPhone().matches("^\\d{10,11}$")) {
                AlertComponent.showError("Lỗi", null, "Số điện thoại không hợp lệ: Phải có 10-11 chữ số");
                return false;
            }

            preparedStatement.setString(1, lecturer.getLecturerID());
            preparedStatement.setString(2, lecturer.getLecturerName());
            preparedStatement.setString(3, lecturer.getGender());
            preparedStatement.setString(4, lecturer.getDegree());
            preparedStatement.setString(5, lecturer.getPhone());
            preparedStatement.setString(6, lecturer.getStatus());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                AlertComponent.showError("Lỗi", null, "Không thể thêm giảng viên vào database.");
                return false;
            }
            return true;
        } catch (SQLException e) {
            AlertComponent.showError("Lỗi", null, "Lỗi database khi thêm giảng viên: " + e.getMessage());
            return false;
        }
    }

    // Xuất file danh sách giảng viên
    private void downloadLecturerFile() {
        // Hiển thị dialog để người dùng chọn định dạng file
        ChoiceDialog<String> dialog = new ChoiceDialog<>("JSON", "JSON", "Excel", "CSV");
        dialog.setTitle("Chọn định dạng file");
        dialog.setHeaderText("Vui lòng chọn định dạng file để xuất danh sách giảng viên:");
        dialog.setContentText("Định dạng:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Lưu file danh sách giảng viên");

            // Thêm các bộ lọc tương ứng với định dạng được chọn
            switch (result.get()) {
                case "JSON":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
                    fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
                    break;
                case "Excel":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                    fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
                    break;
                case "CSV":
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                    fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));
                    break;
            }

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    // Xuất file chính (JSON, Excel, hoặc CSV)
                    switch (result.get()) {
                        case "JSON":
                            exportLecturersToJson(file);
                            break;
                        case "Excel":
                            exportLecturersToExcel(file);
                            break;
                        case "CSV":
                            exportLecturersToCsv(file);
                            break;
                    }
                    AlertComponent.showInformation("Thành công", null, "Đã xuất file thành công: " + file.getName());

                    // Tạo tên file .txt dựa trên tên file chính (thay .json/.xlsx/.csv bằng .txt)
                    String txtFileName = file.getAbsolutePath().replaceAll("\\.(json|xlsx|csv)$", ".txt");
                    File instructionFile = new File(txtFileName);

                    // Kiểm tra nếu file .txt chưa tồn tại, thì xuất hướng dẫn
                    if (!instructionFile.exists()) {
                        exportInstructionsToTxt(instructionFile);
                        AlertComponent.showInformation("Thành công", null,
                                "Đã xuất file hướng dẫn: " + instructionFile.getName());
                    }
                } catch (Exception e) {
                    AlertComponent.showError("Lỗi", null, "Không thể xuất file: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    // Xuất danh sách giảng viên sang file JSON
    private void exportLecturersToJson(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        ObservableList<LecturerData> lecturers = tableLecturer.getItems();
        if (lecturers == null || lecturers.isEmpty()) {
            // Nếu TableView trống, lấy từ database
            lecturers = getLecturerListData();
            if (lecturers == null || lecturers.isEmpty()) {
                // Nếu database trống, xuất chỉ tiêu đề cột
                List<String> headers = Arrays.asList("lecturer_id", "lecturer_name", "gender", "degree", "phone",
                        "status");
                objectMapper.writeValue(file, headers);
            } else {
                objectMapper.writeValue(file, lecturers);
            }
        } else {
            // Nếu TableView có dữ liệu, xuất trực tiếp
            objectMapper.writeValue(file, lecturers);
        }
    }

    // Xuất danh sách giảng viên sang file Excel
    private void exportLecturersToExcel(File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Lecturers");

        ObservableList<LecturerData> lecturers = tableLecturer.getItems();
        int rowNum = 0;

        // Tạo tiêu đề cột
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = { "lecturer_id", "lecturer_name", "gender", "degree", "phone", "status" };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        if (lecturers == null || lecturers.isEmpty()) {
            // Nếu TableView trống, lấy từ database
            lecturers = getLecturerListData();
            if (lecturers != null && !lecturers.isEmpty()) {
                // Nếu database có dữ liệu, xuất danh sách
                for (LecturerData lecturer : lecturers) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(lecturer.getLecturerID());
                    row.createCell(1).setCellValue(lecturer.getLecturerName());
                    row.createCell(2).setCellValue(lecturer.getGender());
                    row.createCell(3).setCellValue(lecturer.getDegree());
                    row.createCell(4).setCellValue(lecturer.getPhone());
                    row.createCell(5).setCellValue(lecturer.getStatus());
                }
            }
        } else {
            // Nếu TableView có dữ liệu, xuất danh sách
            for (LecturerData lecturer : lecturers) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(lecturer.getLecturerID());
                row.createCell(1).setCellValue(lecturer.getLecturerName());
                row.createCell(2).setCellValue(lecturer.getGender());
                row.createCell(3).setCellValue(lecturer.getDegree());
                row.createCell(4).setCellValue(lecturer.getPhone());
                row.createCell(5).setCellValue(lecturer.getStatus());
            }
        }

        // Tự động điều chỉnh chiều rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    // Xuất danh sách giảng viên sang file CSV
    private void exportLecturersToCsv(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String[] headers = { "lecturer_id", "lecturer_name", "gender", "degree", "phone", "status" };
            StringBuilder line = new StringBuilder();

            // Ghi tiêu đề
            for (int i = 0; i < headers.length; i++) {
                line.append(headers[i]);
                if (i < headers.length - 1)
                    line.append(",");
            }
            writer.write(line.toString());
            writer.newLine();

            ObservableList<LecturerData> lecturers = tableLecturer.getItems();
            if (lecturers != null && !lecturers.isEmpty()) {
                for (LecturerData lecturer : lecturers) {
                    line.setLength(0); // Xóa nội dung cũ
                    line.append(lecturer.getLecturerID()).append(",")
                            .append("\"" + lecturer.getLecturerName() + "\"").append(",")
                            .append(lecturer.getGender()).append(",")
                            .append(lecturer.getDegree()).append(",")
                            .append(lecturer.getPhone()).append(",")
                            .append(lecturer.getStatus());
                    writer.write(line.toString());
                    writer.newLine();
                }
            } else {
                // Nếu TableView trống, lấy từ database
                lecturers = getLecturerListData();
                if (lecturers != null && !lecturers.isEmpty()) {
                    for (LecturerData lecturer : lecturers) {
                        line.setLength(0); // Xóa nội dung cũ
                        line.append(lecturer.getLecturerID()).append(",")
                                .append("\"" + lecturer.getLecturerName() + "\"").append(",")
                                .append(lecturer.getGender()).append(",")
                                .append(lecturer.getDegree()).append(",")
                                .append(lecturer.getPhone()).append(",")
                                .append(lecturer.getStatus());
                        writer.write(line.toString());
                        writer.newLine();
                    }
                }
            }
        }
    }

    // ========== QUẢN LÝ GIẢNG VIÊN ==========

    // ========== QUẢN LÝ ĐIỂM ==========

    private ObservableList<GradesData> gradesListData; // Dữ liệu điểm

    // Lấy danh sách điểm số từ database
    public ObservableList<GradesData> getGradesListData() {
        ObservableList<GradesData> gradesList = FXCollections.observableArrayList();

        String sql = """
                    SELECT g.student_id, s.school_year, COALESCE(g.course_id, s.subject) AS course_id,
                           c.course_name, g.midterm_grade, g.final_grade, g.total_grade
                    FROM grades g
                    JOIN students s ON g.student_id = s.student_id
                    LEFT JOIN courses c ON COALESCE(g.course_id, s.subject) = c.course_id
                """;

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                gradesList.add(new GradesData(
                        resultSet.getString("student_id"),
                        resultSet.getString("school_year"),
                        resultSet.getString("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getFloat("midterm_grade"),
                        resultSet.getFloat("final_grade"),
                        resultSet.getFloat("total_grade")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gradesList;
    }

    // Hiển thị danh sách điểm số lên TableView
    public void showGradesList() {
        gradesListData = getGradesListData();

        colStudentIDGrades.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        colSchoolYearGrades.setCellValueFactory(new PropertyValueFactory<>("schoolYear"));
        colSubjectGrades.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colMidtermGrade.setCellValueFactory(new PropertyValueFactory<>("midtermGrade"));
        colFinalGrade.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));
        colTotalGrade.setCellValueFactory(new PropertyValueFactory<>("totalGrade"));

        tableGrades.setItems(gradesListData);
    }

    // Load thông tin năm học & môn học của sinh viên
    @FXML
    private void loadStudentInfo() {
        String studentID = txtStudentIDGrades.getText().trim();
        if (studentID.isEmpty()) {
            lblSchoolYearGrades.setText("-");
            lblSubjectGrades.setText("-");
            return;
        }

        String sql = """
                    SELECT s.school_year, s.subject, c.course_name
                    FROM students s
                    LEFT JOIN courses c ON s.subject = c.course_id
                    WHERE s.student_id = ?
                """;

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            preparedStatement.setString(1, studentID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    lblSchoolYearGrades.setText(resultSet.getString("school_year"));
                    String courseName = resultSet.getString("course_name");
                    lblSubjectGrades.setText(courseName != null ? courseName : "Chưa có môn học");
                } else {
                    lblSchoolYearGrades.setText("-");
                    lblSubjectGrades.setText("-");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Kiểm tra sinh viên có tồn tại không
    private boolean isStudentExists(String studentID) {
        return checkExists("SELECT COUNT(*) FROM students WHERE student_id = ?", studentID);
    }

    // Kiểm tra điểm có tồn tại chưa
    private boolean isGradeExists(String studentID, String courseID) {
        return checkExists("SELECT COUNT(*) FROM grades WHERE student_id = ? AND course_id = ?", studentID, courseID);
    }

    // Kiểm tra sự tồn tại của dữ liệu (hàm dùng chung)
    private boolean checkExists(String sql, String... params) {
        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setString(i + 1, params[i]);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy course_id từ tên môn học
    private String getCourseIDByName(String courseName) {
        String sql = "SELECT course_id FROM courses WHERE course_name = ?";
        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("course_id");
            }
        } catch (SQLException e) {
            AlertComponent.showError("Lỗi", null, "Không thể truy vấn thông tin môn học: " + e.getMessage());
            return null;
        }
        return null; // Không tìm thấy course_name
    }

    // Hàm lấy 1 giá trị từ database (hàm dùng chung)
    private String getSingleResult(String sql, String param) {
        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            preparedStatement.setString(1, param);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Xử lý thêm điểm
    private void handleAddGrades() {
        String studentID = txtStudentIDGrades.getText().trim();
        String schoolYear = lblSchoolYearGrades.getText().trim();
        String courseName = lblSubjectGrades.getText().trim(); // Lấy môn học từ label
        String midtermText = txtMidtermGrade.getText().trim();
        String finalText = txtFinalGrade.getText().trim();

        if (studentID.isEmpty() || schoolYear.equals("-") || courseName.equals("-") ||
                midtermText.isEmpty() || finalText.isEmpty()) {
            AlertComponent.showWarning("Lỗi nhập liệu", null, "Vui lòng nhập đầy đủ thông tin điểm số!");
            return;
        }

        // Kiểm tra định dạng điểm số
        float midtermGrade, finalGrade;
        try {
            midtermGrade = Float.parseFloat(midtermText);
            finalGrade = Float.parseFloat(finalText);
            if (midtermGrade < 0 || midtermGrade > 10 || finalGrade < 0 || finalGrade > 10) {
                AlertComponent.showWarning("Lỗi nhập liệu", null, "Điểm phải nằm trong khoảng 0 - 10!");
                return;
            }
        } catch (NumberFormatException e) {
            AlertComponent.showWarning("Lỗi nhập liệu", null, "Điểm phải là số hợp lệ!");
            return;
        }

        // Lấy course_id từ course_name
        String courseID = getCourseIDByName(courseName);
        if (courseID == null) {
            AlertComponent.showError("Lỗi", null, "Không tìm thấy mã môn học trong hệ thống!");
            return;
        }

        // Kiểm tra sinh viên có tồn tại không
        if (!isStudentExists(studentID)) {
            AlertComponent.showError("Lỗi", null, "Mã sinh viên không tồn tại trong hệ thống!");
            return;
        }

        // Kiểm tra sinh viên đã có điểm môn học chưa
        if (isGradeExists(studentID, courseID)) {
            AlertComponent.showWarning("Lỗi", null, "Sinh viên đã có điểm môn học này!");
            return;
        }

        // Tính điểm tổng kết nếu MySQL không tự động tính
        float totalGrade = (midtermGrade * 0.4f) + (finalGrade * 0.6f);

        // Cột total tự động tính
        String sql = "INSERT INTO grades (student_id, school_year, course_id, midterm_grade, final_grade) VALUES (?, ?, ?, ?, ?)";
        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            preparedStatement.setString(1, studentID);
            preparedStatement.setString(2, schoolYear);
            preparedStatement.setString(3, courseID);
            preparedStatement.setFloat(4, midtermGrade);
            preparedStatement.setFloat(5, finalGrade);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                AlertComponent.showInformation("Thành công", null, "Điểm đã được thêm thành công!");
                showGradesList();
                clearGradesForm();
                dashboardController.getInstance().refreshDashboard(); // Cập nhật lại Dashboard
            } else {
                AlertComponent.showError("Lỗi", null, "Không thể thêm điểm!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xử lý cập nhật điểm
    private void handleUpdateGrades() {
        String studentID = txtStudentIDGrades.getText().trim();
        String courseName = lblSubjectGrades.getText().trim();
        String midtermText = txtMidtermGrade.getText().trim();
        String finalText = txtFinalGrade.getText().trim();

        if (studentID.isEmpty() || courseName.equals("-") || midtermText.isEmpty() || finalText.isEmpty()) {
            AlertComponent.showWarning("Lỗi nhập liệu", null, "Vui lòng nhập đầy đủ thông tin điểm số!");
            return;
        }

        // Kiểm tra điểm có hợp lệ không
        float midtermGrade, finalGrade;
        try {
            midtermGrade = Float.parseFloat(midtermText);
            finalGrade = Float.parseFloat(finalText);
            if (midtermGrade < 0 || midtermGrade > 10 || finalGrade < 0 || finalGrade > 10) {
                AlertComponent.showWarning("Lỗi nhập liệu", null, "Điểm phải nằm trong khoảng 0 - 10!");
                return;
            }
        } catch (NumberFormatException e) {
            AlertComponent.showWarning("Lỗi nhập liệu", null, "Điểm phải là số hợp lệ!");
            return;
        }

        // Lấy course_id từ tên môn học
        String courseID = getCourseIDByName(courseName);
        if (courseID == null) {
            AlertComponent.showError("Lỗi", null, "Không tìm thấy mã môn học trong hệ thống!");
            return;
        }

        // Kiểm tra xem điểm đã tồn tại chưa
        if (!isGradeExists(studentID, courseID)) {
            AlertComponent.showError("Lỗi", null, "Không tìm thấy điểm của sinh viên này!");
            return;
        }

        // Tính lại điểm tổng
        float totalGrade = (midtermGrade * 0.4f) + (finalGrade * 0.6f);

        // Cập nhật điểm vào database
        String sql = """
                    UPDATE grades
                    SET midterm_grade = ?, final_grade = ?
                    WHERE student_id = ? AND course_id = ?
                """;

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            preparedStatement.setFloat(1, midtermGrade);
            preparedStatement.setFloat(2, finalGrade);
            preparedStatement.setString(3, studentID);
            preparedStatement.setString(4, courseID);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                AlertComponent.showInformation("Thành công", null, "Cập nhật điểm thành công!");
                showGradesList();
                clearGradesForm();
                dashboardController.getInstance().refreshDashboard(); // Cập nhật lại Dashboard
            } else {
                AlertComponent.showError("Lỗi", null, "Không thể cập nhật điểm!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Tính năng xoá điểm
    private void handleDeleteGrades() {
        // Kiểm tra xem có sinh viên nào được chọn trong bảng không
        GradesData selectedGrade = tableGrades.getSelectionModel().getSelectedItem();

        // Nếu không có dữ liệu nhập, lấy từ TableView
        String studentID = txtStudentIDGrades.getText().trim();
        String courseName = lblSubjectGrades.getText().trim();

        if (studentID.isEmpty() && selectedGrade != null) {
            studentID = selectedGrade.getStudentID();
            courseName = selectedGrade.getCourseName();
        }

        // Kiểm tra nếu không có dữ liệu hợp lệ
        if (studentID.isEmpty() || courseName.equals("-")) {
            AlertComponent.showWarning("Lỗi nhập liệu", null,
                    "Vui lòng nhập MSSV và chọn môn học hoặc chọn trong bảng!");
            return;
        }

        // Lấy course_id từ tên môn học
        String courseID = getCourseIDByName(courseName);
        if (courseID == null) {
            AlertComponent.showError("Lỗi", null, "Không tìm thấy mã môn học trong hệ thống!");
            return;
        }

        // Kiểm tra xem điểm có tồn tại không
        if (!isGradeExists(studentID, courseID)) {
            AlertComponent.showError("Lỗi", null, "Không tìm thấy điểm của sinh viên này!");
            return;
        }

        // Hỏi xác nhận trước khi xóa
        if (!AlertComponent.showConfirmation("Xóa điểm", null, "Bạn có chắc chắn muốn xóa điểm này?")) {
            return;
        }

        // Xóa điểm trong database
        String sql = "DELETE FROM grades WHERE student_id = ? AND course_id = ?";

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            preparedStatement.setString(1, studentID);
            preparedStatement.setString(2, courseID);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                AlertComponent.showInformation("Thành công", null, "Xóa điểm thành công!");
                showGradesList();
                clearGradesForm();
                dashboardController.getInstance().refreshDashboard(); // Cập nhật lại Dashboard
            } else {
                AlertComponent.showError("Lỗi", null, "Không thể xóa điểm!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tính năng xoá toàn bộ điểm của sinh viên
    private void handleClearAllGrades() {
        // Hỏi xác nhận trước khi xóa (Cảnh báo nguy hiểm)
        if (!AlertComponent.showConfirmation("Cảnh báo nguy hiểm", null,
                "Bạn sắp xóa toàn bộ điểm số của tất cả sinh viên!\n" +
                        "Hành động này không thể hoàn tác. Bạn có chắc chắn muốn tiếp tục?")) {
            return;
        }

        // Xóa toàn bộ dữ liệu từ bảng `grades`
        String deleteAllGradesSQL = "DELETE FROM grades";

        try (Connection connect = database.connectDB();
                PreparedStatement deleteGradesStmt = connect.prepareStatement(deleteAllGradesSQL)) {

            int rowsDeleted = deleteGradesStmt.executeUpdate();

            if (rowsDeleted > 0) {
                AlertComponent.showInformation("Thành công", null,
                        "Đã xóa toàn bộ điểm của tất cả sinh viên thành công!");
            } else {
                AlertComponent.showWarning("Thông báo", null,
                        "Không có điểm nào để xóa.");
            }

            showGradesList(); // Cập nhật danh sách điểm trên TableView
            clearGradesForm(); // Xóa nội dung form
            dashboardController.getInstance().refreshDashboard(); // Cập nhật lại Dashboard
        } catch (Exception e) {
            e.printStackTrace();
            AlertComponent.showError("Lỗi", null, "Không thể xóa điểm!");
        }
    }

    private void clearGradesForm() {
        txtStudentIDGrades.clear();
        lblSchoolYearGrades.setText("-");
        lblSubjectGrades.setText("-");
        txtMidtermGrade.clear();
        txtFinalGrade.clear();
        txtTotalGrade.clear();

        // Hiện add
        btnAddGrades.setDisable(false);
    }

    private void fillFormWithSelectedGrade() {
        // Ẩn add
        btnAddGrades.setDisable(true);

        GradesData selectedGrade = tableGrades.getSelectionModel().getSelectedItem();
        if (selectedGrade == null) {
            AlertComponent.showWarning("Thông báo", null, "Vui lòng chọn một sinh viên trong bảng!");
            return;
        }

        // Điền dữ liệu vào form
        txtStudentIDGrades.setText(selectedGrade.getStudentID());
        lblSchoolYearGrades.setText(selectedGrade.getSchoolYear());
        lblSubjectGrades.setText(selectedGrade.getCourseName());
        txtMidtermGrade.setText(String.valueOf(selectedGrade.getMidtermGrade()));
        txtFinalGrade.setText(String.valueOf(selectedGrade.getFinalGrade()));

    }

    // Tính năng tìm kiếm theo MSSV
    private void searchGradesByStudentID(String studentID) {
        // Nếu ô tìm kiếm trống, hiển thị toàn bộ danh sách điểm
        if (studentID.isEmpty()) {
            showGradesList();
            return;
        }

        ObservableList<GradesData> filteredList = FXCollections.observableArrayList();

        String sql = """
                    SELECT g.student_id, s.school_year, COALESCE(g.course_id, s.subject) AS course_id,
                           c.course_name, g.midterm_grade, g.final_grade, g.total_grade
                    FROM grades g
                    JOIN students s ON g.student_id = s.student_id
                    LEFT JOIN courses c ON COALESCE(g.course_id, s.subject) = c.course_id
                    WHERE g.student_id LIKE ?
                """;

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + studentID + "%"); // Tìm kiếm không phân biệt toàn bộ MSSV

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                filteredList.add(new GradesData(
                        resultSet.getString("student_id"),
                        resultSet.getString("school_year"),
                        resultSet.getString("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getFloat("midterm_grade"),
                        resultSet.getFloat("final_grade"),
                        resultSet.getFloat("total_grade")));
            }

            // Hiển thị kết quả tìm kiếm lên bảng
            tableGrades.setItems(filteredList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mở file điểm
    private void openGradesFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn file điểm");

        // Thêm các bộ lọc cho JSON, Excel, CSV, và All Files
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"), // Hiển thị tất cả file
                new FileChooser.ExtensionFilter("JSON Files", "*.json"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Đặt "All Files" làm bộ lọc mặc định để hiển thị tất cả file
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));

        File file = fileChooser.showOpenDialog(null);
        if (file != null && file.exists()) { // Kiểm tra file không null và tồn tại
            processGradesFile(file);
        } else {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một file hợp lệ!");
        }
    }

    // Xử lý file theo điểm
    private void processGradesFile(File file) {
        if (file == null || !file.exists()) {
            AlertComponent.showError("Lỗi", null, "File không tồn tại hoặc không hợp lệ!");
            return;
        }

        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".json")) {
            importFromJsonGrades(file);
        } else if (fileName.endsWith(".xlsx")) {
            importFromExcelGrades(file);
        } else if (fileName.endsWith(".csv")) {
            importFromCsvGrades(file);
        } else {
            AlertComponent.showWarning("Lỗi", null, "Định dạng file không hợp lệ! Vui lòng chọn JSON, Excel hoặc CSV.");
        }
    }

    // Import theo JSON
    private void importFromJsonGrades(File file) {
        if (file == null || !file.exists()) {
            AlertComponent.showError("Lỗi", null, "File không tồn tại hoặc không hợp lệ!");
            return;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<GradesData> grades = objectMapper.readValue(Paths.get(file.getAbsolutePath()).toFile(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, GradesData.class));

            int duplicateCount = 0; // Đếm số điểm trùng (MSSV + course_id)
            int successCount = 0; // Đếm số điểm thêm thành công
            int errorCount = 0; // Đếm số lỗi khác

            for (GradesData grade : grades) {
                try {
                    // Lấy thông tin school_year và course_id từ MSSV
                    String studentID = grade.getStudentID();
                    if (studentID == null || studentID.trim().isEmpty()) {
                        throw new IllegalArgumentException("Mã sinh viên không được để trống trong file JSON.");
                    }

                    // Lấy school_year và subject từ bảng students
                    String sqlStudent = """
                                SELECT school_year, subject
                                FROM students
                                WHERE student_id = ?
                            """;
                    String schoolYear = null;
                    String courseID = null;
                    try (Connection connect = database.connectDB();
                            PreparedStatement psStudent = connect.prepareStatement(sqlStudent)) {
                        psStudent.setString(1, studentID);
                        try (ResultSet rsStudent = psStudent.executeQuery()) {
                            if (rsStudent.next()) {
                                schoolYear = rsStudent.getString("school_year");
                                courseID = rsStudent.getString("subject");
                                if (courseID == null) {
                                    throw new SQLException("Không tìm thấy môn học cho sinh viên " + studentID);
                                }
                            } else {
                                throw new SQLException("Mã sinh viên " + studentID + " không tồn tại trong hệ thống.");
                            }
                        }
                    }

                    // Lấy course_name từ course_id
                    String courseName = getCourseNameByCourseID(courseID);
                    if (courseName == null) {
                        throw new SQLException("Không tìm thấy tên môn học cho mã môn học: " + courseID);
                    }

                    // Kiểm tra định dạng và giá trị điểm
                    float midtermGrade = grade.getMidtermGrade();
                    float finalGrade = grade.getFinalGrade();
                    if (midtermGrade < 0 || midtermGrade > 10 || finalGrade < 0 || finalGrade > 10) {
                        throw new IllegalArgumentException(
                                "Điểm của sinh viên " + studentID + " phải nằm trong khoảng 0 - 10.");
                    }

                    // Tạo đối tượng GradesData với thông tin tự động điền
                    GradesData updatedGrade = new GradesData(studentID, schoolYear, courseID, courseName, midtermGrade,
                            finalGrade, 0.0f);

                    if (insertGradeIntoDatabase(updatedGrade)) {
                        successCount++;
                    } else {
                        if (isGradeExists(studentID, courseID)) {
                            duplicateCount++;
                            AlertComponent.showWarning("Lỗi", null,
                                    "Điểm của sinh viên " + studentID + " cho môn " + courseID + " đã tồn tại.");
                        } else {
                            errorCount++;
                            AlertComponent.showError("Lỗi", null,
                                    "Lỗi khi thêm điểm cho sinh viên " + studentID + ": Dữ liệu không hợp lệ.");
                        }
                    }
                } catch (Exception e) {
                    errorCount++;
                    AlertComponent.showError("Lỗi", null,
                            "Lỗi khi xử lý điểm cho sinh viên "
                                    + (grade.getStudentID() != null ? grade.getStudentID() : "không xác định") + ": "
                                    + e.getMessage());
                }
            }

            // Tạo thông báo dựa trên kết quả
            String message = "Đã thêm " + successCount + " điểm thành công từ JSON!";
            if (duplicateCount > 0) {
                message += "\nCó " + duplicateCount + " điểm bị bỏ qua (trùng MSSV và mã môn học).";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " lỗi khác xảy ra.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            showGradesList(); // Cập nhật danh sách điểm trên TableView
        } catch (IOException e) {
            AlertComponent.showError("Lỗi", null, "Không thể đọc file JSON: " + e.getMessage());
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập điểm từ JSON: " + e.getMessage());
        }
    }

    // Import theo Excel
    private void importFromExcelGrades(File file) {
        if (file == null || !file.exists()) {
            AlertComponent.showError("Lỗi", null, "File không tồn tại hoặc không hợp lệ!");
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
                Workbook workbook = new XSSFWorkbook(fis);
                Connection connect = database.connectDB()) {

            PreparedStatement preparedStatement = connect.prepareStatement(
                    "INSERT INTO grades (student_id, school_year, course_id, midterm_grade, final_grade) VALUES (?, ?, ?, ?, ?)");

            Sheet sheet = workbook.getSheetAt(0);
            int successCount = 0;
            int duplicateCount = 0;
            int errorCount = 0;

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue; // Bỏ qua dòng tiêu đề

                try {
                    String studentID = getCellStringValue(row.getCell(0));
                    String midtermStr = getCellStringValue(row.getCell(1));
                    String finalStr = getCellStringValue(row.getCell(2));

                    if (studentID.isEmpty() || midtermStr.isEmpty() || finalStr.isEmpty()) {
                        throw new IllegalArgumentException("Dữ liệu trống tại dòng " + (row.getRowNum() + 1));
                    }

                    // Chuyển điểm từ chuỗi sang float
                    float midtermGrade, finalGrade;
                    try {
                        midtermGrade = Float.parseFloat(midtermStr);
                        finalGrade = Float.parseFloat(finalStr);
                        if (midtermGrade < 0 || midtermGrade > 10 || finalGrade < 0 || finalGrade > 10) {
                            throw new NumberFormatException("Điểm phải nằm trong khoảng 0 - 10");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                                "Điểm không hợp lệ tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                    }

                    // Lấy school_year và course_id từ MSSV
                    String sqlStudent = """
                                SELECT school_year, subject
                                FROM students
                                WHERE student_id = ?
                            """;
                    String schoolYear = null;
                    String courseID = null;
                    try (PreparedStatement psStudent = connect.prepareStatement(sqlStudent)) {
                        psStudent.setString(1, studentID);
                        try (ResultSet rsStudent = psStudent.executeQuery()) {
                            if (rsStudent.next()) {
                                schoolYear = rsStudent.getString("school_year");
                                courseID = rsStudent.getString("subject");
                                if (courseID == null) {
                                    throw new SQLException("Không tìm thấy môn học cho sinh viên " + studentID
                                            + " tại dòng " + (row.getRowNum() + 1));
                                }
                            } else {
                                throw new SQLException("Mã sinh viên " + studentID + " không tồn tại tại dòng "
                                        + (row.getRowNum() + 1));
                            }
                        }
                    }

                    // Lấy course_name từ course_id
                    String courseName = getCourseNameByCourseID(courseID);
                    if (courseName == null) {
                        throw new SQLException("Không tìm thấy tên môn học cho mã môn học: " + courseID + " tại dòng "
                                + (row.getRowNum() + 1));
                    }

                    GradesData grade = new GradesData(studentID, schoolYear, courseID, courseName, midtermGrade,
                            finalGrade, 0.0f); // total_grade không cần nhập

                    if (!isGradeExists(studentID, courseID)) {
                        preparedStatement.setString(1, studentID);
                        preparedStatement.setString(2, schoolYear);
                        preparedStatement.setString(3, courseID);
                        preparedStatement.setFloat(4, midtermGrade);
                        preparedStatement.setFloat(5, finalGrade);
                        preparedStatement.addBatch();
                        successCount++;
                    } else {
                        duplicateCount++;
                        AlertComponent.showWarning("Lỗi", null, "Điểm của sinh viên " + studentID + " cho môn "
                                + courseID + " đã tồn tại tại dòng " + (row.getRowNum() + 1));
                    }
                } catch (Exception e) {
                    if (e instanceof SQLException) {
                        if (e.getMessage().contains("sinh viên") || e.getMessage().contains("môn học")) {
                            duplicateCount++;
                            AlertComponent.showError("Lỗi", null, e.getMessage());
                        } else {
                            errorCount++;
                            AlertComponent.showError("Lỗi", null,
                                    "Lỗi SQL tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                        }
                    } else if (e instanceof IllegalArgumentException) {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null,
                                "Dòng lỗi tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                    } else {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null,
                                "Dòng lỗi tại dòng " + (row.getRowNum() + 1) + ": " + e.getMessage());
                    }
                }
            }

            if (successCount > 0) {
                preparedStatement.executeBatch();
            }

            String message = "Đã nhập " + successCount + " điểm thành công từ Excel!";
            if (duplicateCount > 0) {
                message += "\nCó " + duplicateCount + " điểm trùng MSSV và mã môn học bị bỏ qua.";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " dòng lỗi khác.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            showGradesList(); // Cập nhật danh sách điểm trên TableView
        } catch (IOException e) {
            AlertComponent.showError("Lỗi", null, "Không thể đọc file Excel: " + e.getMessage());
        } catch (SQLException e) {
            AlertComponent.showError("Lỗi", null, "Lỗi kết nối database khi nhập điểm: " + e.getMessage());
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập điểm từ Excel: " + e.getMessage());
        }
    }

    // Import theo CSV
    private void importFromCsvGrades(File file) {
        if (file == null || !file.exists()) {
            AlertComponent.showError("Lỗi", null, "File không tồn tại hoặc không hợp lệ!");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            int successCount = 0;
            int duplicateCount = 0;
            int errorCount = 0;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                try {
                    String[] data = line.split(",");
                    if (data.length < 3) {
                        throw new IllegalArgumentException("Dữ liệu không đủ tại dòng hiện tại");
                    }

                    String studentID = data[0].trim();
                    String midtermStr = data[1].trim();
                    String finalStr = data[2].trim();

                    if (studentID.isEmpty() || midtermStr.isEmpty() || finalStr.isEmpty()) {
                        throw new IllegalArgumentException("Dữ liệu trống tại dòng hiện tại");
                    }

                    // Chuyển điểm từ chuỗi sang float
                    float midtermGrade, finalGrade;
                    try {
                        midtermGrade = Float.parseFloat(midtermStr);
                        finalGrade = Float.parseFloat(finalStr);
                        if (midtermGrade < 0 || midtermGrade > 10 || finalGrade < 0 || finalGrade > 10) {
                            throw new NumberFormatException("Điểm phải nằm trong khoảng 0 - 10");
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Điểm không hợp lệ tại dòng hiện tại: " + e.getMessage());
                    }

                    // Lấy school_year và course_id từ MSSV
                    String sqlStudent = """
                                SELECT school_year, subject
                                FROM students
                                WHERE student_id = ?
                            """;
                    String schoolYear = null;
                    String courseID = null;
                    try (Connection connect = database.connectDB();
                            PreparedStatement psStudent = connect.prepareStatement(sqlStudent)) {
                        psStudent.setString(1, studentID);
                        try (ResultSet rsStudent = psStudent.executeQuery()) {
                            if (rsStudent.next()) {
                                schoolYear = rsStudent.getString("school_year");
                                courseID = rsStudent.getString("subject");
                                if (courseID == null) {
                                    throw new SQLException(
                                            "Không tìm thấy môn học cho sinh viên " + studentID + " tại dòng hiện tại");
                                }
                            } else {
                                throw new SQLException(
                                        "Mã sinh viên " + studentID + " không tồn tại tại dòng hiện tại");
                            }
                        }
                    }

                    // Lấy course_name từ course_id
                    String courseName = getCourseNameByCourseID(courseID);
                    if (courseName == null) {
                        throw new SQLException(
                                "Không tìm thấy tên môn học cho mã môn học: " + courseID + " tại dòng hiện tại");
                    }

                    GradesData grade = new GradesData(studentID, schoolYear, courseID, courseName, midtermGrade,
                            finalGrade, 0.0f); // total_grade không cần nhập

                    if (!isGradeExists(studentID, courseID)) {
                        insertGradeIntoDatabase(grade);
                        successCount++;
                    } else {
                        duplicateCount++;
                        AlertComponent.showWarning("Lỗi", null, "Điểm của sinh viên " + studentID + " cho môn "
                                + courseID + " đã tồn tại tại dòng hiện tại");
                    }
                } catch (Exception e) {
                    if (e instanceof SQLException) {
                        if (e.getMessage().contains("sinh viên") || e.getMessage().contains("môn học")) {
                            duplicateCount++;
                            AlertComponent.showError("Lỗi", null, e.getMessage());
                        } else {
                            errorCount++;
                            AlertComponent.showError("Lỗi", null, "Lỗi SQL tại dòng hiện tại: " + e.getMessage());
                        }
                    } else if (e instanceof IllegalArgumentException) {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null, "Dòng lỗi tại dòng hiện tại: " + e.getMessage());
                    } else {
                        errorCount++;
                        AlertComponent.showError("Lỗi", null, "Dòng lỗi tại dòng hiện tại: " + e.getMessage());
                    }
                }
            }

            String message = "Đã nhập " + successCount + " điểm thành công từ CSV!";
            if (duplicateCount > 0) {
                message += "\nCó " + duplicateCount + " điểm trùng MSSV và mã môn học bị bỏ qua.";
            }
            if (errorCount > 0) {
                message += "\nCó " + errorCount + " dòng lỗi khác.";
            }
            AlertComponent.showInformation("Thành công", null, message);
            showGradesList(); // Cập nhật danh sách điểm trên TableView
        } catch (IOException e) {
            AlertComponent.showError("Lỗi", null, "Không thể đọc file CSV: " + e.getMessage());
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Không thể nhập điểm từ CSV: " + e.getMessage());
        }
    }

    // Xử lý thêm điểm vào database
    private boolean insertGradeIntoDatabase(GradesData grade) throws SQLException {
        String sql = "INSERT INTO grades (student_id, school_year, course_id, midterm_grade, final_grade) VALUES (?, ?, ?, ?, ?)";

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {

            // Kiểm tra sinh viên có tồn tại không
            if (!isStudentExists(grade.getStudentID())) {
                throw new SQLException("Mã sinh viên " + grade.getStudentID() + " không tồn tại trong hệ thống.");
            }

            // Kiểm tra mã môn học có tồn tại không
            String courseIDFromDB = getCourseIDByName(grade.getCourseName());
            if (courseIDFromDB == null || !courseIDFromDB.equals(grade.getCourseID())) {
                throw new SQLException("Mã môn học " + grade.getCourseID()
                        + " không tồn tại hoặc không khớp với tên môn học trong hệ thống.");
            }

            if (isGradeExists(grade.getStudentID(), grade.getCourseID())) {
                AlertComponent.showWarning("Lỗi", null, "Điểm của sinh viên " + grade.getStudentID() + " cho môn "
                        + grade.getCourseID() + " đã tồn tại.");
                return false;
            }

            preparedStatement.setString(1, grade.getStudentID());
            preparedStatement.setString(2, grade.getSchoolYear());
            preparedStatement.setString(3, grade.getCourseID());
            preparedStatement.setFloat(4, grade.getMidtermGrade());
            preparedStatement.setFloat(5, grade.getFinalGrade());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không thể thêm điểm vào database.");
            }
            return true;
        }
    }

    // Lấy course_name từ course_id
    private String getCourseNameByCourseID(String courseID) {
        String sql = "SELECT course_name FROM courses WHERE course_id = ?";
        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            preparedStatement.setString(1, courseID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("course_name");
            }
        } catch (SQLException e) {
            AlertComponent.showError("Lỗi", null, "Không thể truy vấn tên môn học: " + e.getMessage());
        }
        return null; // Không tìm thấy course_id
    }

    // Xuất file danh sách điểm số
    private void downloadGradesFile() {
        // Hiển thị dialog với hai lựa chọn
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Toàn bộ danh sách", "Toàn bộ danh sách",
                "Bảng điểm từng sinh viên");
        dialog.setTitle("Chọn loại xuất file");
        dialog.setHeaderText("Vui lòng chọn loại dữ liệu để xuất:");
        dialog.setContentText("Loại xuất:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent())
            return; // Người dùng hủy dialog

        String choice = result.get();
        if ("Toàn bộ danh sách".equals(choice)) {
            // Xuất toàn bộ danh sách (giữ nguyên logic cũ)
            exportFullGradesList();
        } else if ("Bảng điểm từng sinh viên".equals(choice)) {
            // Xuất bảng điểm từng sinh viên
            exportIndividualGrade();
        }
    }

    private void exportFullGradesList() {
        ChoiceDialog<String> formatDialog = new ChoiceDialog<>("JSON", "JSON", "Excel", "CSV");
        formatDialog.setTitle("Chọn định dạng file");
        formatDialog.setHeaderText("Vui lòng chọn định dạng file để xuất danh sách điểm số:");
        formatDialog.setContentText("Định dạng:");

        Optional<String> formatResult = formatDialog.showAndWait();
        if (!formatResult.isPresent())
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file danh sách điểm số");
        String selectedFormat = formatResult.get();

        switch (selectedFormat) {
            case "JSON":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
                break;
            case "Excel":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                break;
            case "CSV":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                break;
        }
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                switch (selectedFormat) {
                    case "JSON":
                        exportGradesToJson(file);
                        break;
                    case "Excel":
                        exportGradesToExcel(file);
                        break;
                    case "CSV":
                        exportGradesToCsv(file);
                        break;
                }
                AlertComponent.showInformation("Thành công", null, "Đã xuất file thành công: " + file.getName());

                String txtFileName = file.getAbsolutePath().replaceAll("\\.(json|xlsx|csv)$", ".txt");
                File instructionFile = new File(txtFileName);
                if (!instructionFile.exists()) {
                    exportInstructionsToTxt(instructionFile);
                    AlertComponent.showInformation("Thành công", null,
                            "Đã xuất file hướng dẫn: " + instructionFile.getName());
                }
            } catch (Exception e) {
                AlertComponent.showError("Lỗi", null, "Không thể xuất file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void exportIndividualGrade() {
        // Yêu cầu người dùng nhập MSSV
        TextInputDialog mssvDialog = new TextInputDialog();
        mssvDialog.setTitle("Nhập MSSV");
        mssvDialog.setHeaderText("Vui lòng nhập MSSV của sinh viên cần xuất bảng điểm:");
        mssvDialog.setContentText("MSSV:");

        Optional<String> mssvResult = mssvDialog.showAndWait();
        if (!mssvResult.isPresent() || mssvResult.get().trim().isEmpty()) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng nhập MSSV hợp lệ!");
            return;
        }

        String studentID = mssvResult.get().trim();
        if (!isStudentExists(studentID)) {
            AlertComponent.showError("Lỗi", null, "MSSV " + studentID + " không tồn tại trong hệ thống!");
            return;
        }

        // Lấy thông tin sinh viên để kiểm tra
        String studentName = getStudentNameByID(studentID);
        ObservableList<GradesData> studentGrades = getGradesForStudent(studentID);
        if (studentGrades.isEmpty()) {
            AlertComponent.showWarning("Lỗi", null, "Sinh viên " + studentID + " chưa có dữ liệu điểm!");
            return;
        }

        // Chọn định dạng file
        ChoiceDialog<String> formatDialog = new ChoiceDialog<>("JSON", "JSON", "Excel", "CSV");
        formatDialog.setTitle("Chọn định dạng file");
        formatDialog.setHeaderText("Vui lòng chọn định dạng file cho bảng điểm của sinh viên " + studentID + ":");
        formatDialog.setContentText("Định dạng:");

        Optional<String> formatResult = formatDialog.showAndWait();
        if (!formatResult.isPresent())
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu bảng điểm sinh viên " + studentID);
        fileChooser.setInitialFileName("BangDiem_" + studentID); // Đặt tên mặc định
        String selectedFormat = formatResult.get();

        switch (selectedFormat) {
            case "JSON":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
                break;
            case "Excel":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                break;
            case "CSV":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                break;
        }
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                switch (selectedFormat) {
                    case "JSON":
                        exportStudentGradeToJson(file, studentID, studentName, studentGrades);
                        break;
                    case "Excel":
                        exportStudentGradeToExcel(file, studentID, studentName, studentGrades);
                        break;
                    case "CSV":
                        exportStudentGradeToCsv(file, studentID, studentName, studentGrades);
                        break;
                }
                AlertComponent.showInformation("Thành công", null, "Đã xuất bảng điểm thành công: " + file.getName());
            } catch (IOException e) {
                AlertComponent.showError("Lỗi", null, "Không thể lưu file: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                AlertComponent.showError("Lỗi", null, "Lỗi không xác định khi xuất file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String getStudentNameByID(String studentID) {
        String sql = "SELECT CONCAT(first_name, ' ', last_name) AS full_name FROM students WHERE student_id = ?";
        return getSingleResult(sql, studentID);
    }

    private ObservableList<GradesData> getGradesForStudent(String studentID) {
        ObservableList<GradesData> gradesList = FXCollections.observableArrayList();
        String sql = """
                SELECT g.student_id, s.school_year, COALESCE(g.course_id, s.subject) AS course_id,
                       c.course_name, g.midterm_grade, g.final_grade, g.total_grade
                FROM grades g
                JOIN students s ON g.student_id = s.student_id
                LEFT JOIN courses c ON COALESCE(g.course_id, s.subject) = c.course_id
                WHERE g.student_id = ?
                """;

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql)) {
            preparedStatement.setString(1, studentID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                gradesList.add(new GradesData(
                        resultSet.getString("student_id"),
                        resultSet.getString("school_year"),
                        resultSet.getString("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getFloat("midterm_grade"),
                        resultSet.getFloat("final_grade"),
                        resultSet.getFloat("total_grade")));
            }
        } catch (SQLException e) {
            AlertComponent.showError("Lỗi", null, "Không thể truy vấn điểm của sinh viên: " + e.getMessage());
        }
        return gradesList;
    }

    private void exportStudentGradeToJson(File file, String studentID, String studentName,
            ObservableList<GradesData> grades) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("student_id", studentID);
        studentData.put("student_name", studentName);
        List<Map<String, Object>> gradeList = grades.stream().map(grade -> {
            Map<String, Object> gradeData = new HashMap<>();
            gradeData.put("school_year", grade.getSchoolYear());
            gradeData.put("course_name", grade.getCourseName());
            gradeData.put("midterm_grade", grade.getMidtermGrade());
            gradeData.put("final_grade", grade.getFinalGrade());
            gradeData.put("total_grade", grade.getTotalGrade());
            return gradeData;
        }).collect(Collectors.toList());
        studentData.put("grades", gradeList);

        objectMapper.writeValue(file, studentData);
    }

    private void exportStudentGradeToExcel(File file, String studentID, String studentName,
            ObservableList<GradesData> grades) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Bảng Điểm - " + studentID);

        int rowNum = 0;
        Row infoRow = sheet.createRow(rowNum++);
        infoRow.createCell(0).setCellValue("MSSV: " + studentID);
        infoRow.createCell(1).setCellValue("Họ tên: " + studentName);

        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = { "Năm học", "Tên môn học", "Điểm giữa kỳ", "Điểm cuối kỳ", "Điểm tổng kết" };
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        for (GradesData grade : grades) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(grade.getSchoolYear());
            row.createCell(1).setCellValue(grade.getCourseName());
            row.createCell(2).setCellValue(grade.getMidtermGrade());
            row.createCell(3).setCellValue(grade.getFinalGrade());
            row.createCell(4).setCellValue(grade.getTotalGrade());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    private void exportStudentGradeToCsv(File file, String studentID, String studentName,
            ObservableList<GradesData> grades) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("MSSV," + studentID);
            writer.newLine();
            writer.write("Họ tên," + studentName);
            writer.newLine();
            writer.write("Năm học,Tên môn học,Điểm giữa kỳ,Điểm cuối kỳ,Điểm tổng kết");
            writer.newLine();

            for (GradesData grade : grades) {
                String line = String.format("%s,%s,%.2f,%.2f,%.2f",
                        grade.getSchoolYear(), grade.getCourseName(),
                        grade.getMidtermGrade(), grade.getFinalGrade(), grade.getTotalGrade());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    // Xuất danh sách điểm số sang file JSON
    private void exportGradesToJson(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        ObservableList<GradesData> grades = tableGrades.getItems();
        if (grades == null || grades.isEmpty()) {
            // Nếu TableView trống, lấy từ database
            grades = getGradesListData();
            if (grades == null || grades.isEmpty()) {
                // Nếu database trống, xuất chỉ tiêu đề cột
                List<String> headers = Arrays.asList("student_id", "midterm_grade", "final_grade");
                objectMapper.writeValue(file, headers);
            } else {
                // Tạo danh sách JSON với định dạng rút gọn
                List<Map<String, Object>> simplifiedGrades = grades.stream().map(grade -> {
                    Map<String, Object> simplifiedGrade = new HashMap<>();
                    simplifiedGrade.put("student_id", grade.getStudentID());
                    simplifiedGrade.put("midterm_grade", grade.getMidtermGrade());
                    simplifiedGrade.put("final_grade", grade.getFinalGrade());
                    return simplifiedGrade;
                }).collect(Collectors.toList());
                objectMapper.writeValue(file, simplifiedGrades);
            }
        } else {
            // Tạo danh sách JSON với định dạng rút gọn
            List<Map<String, Object>> simplifiedGrades = grades.stream().map(grade -> {
                Map<String, Object> simplifiedGrade = new HashMap<>();
                simplifiedGrade.put("student_id", grade.getStudentID());
                simplifiedGrade.put("midterm_grade", grade.getMidtermGrade());
                simplifiedGrade.put("final_grade", grade.getFinalGrade());
                return simplifiedGrade;
            }).collect(Collectors.toList());
            objectMapper.writeValue(file, simplifiedGrades);
        }
    }

    // Xuất danh sách điểm số sang file Excel
    private void exportGradesToExcel(File file) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Grades");

        ObservableList<GradesData> grades = tableGrades.getItems();
        int rowNum = 0;

        // Tạo tiêu đề cột
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = { "student_id", "midterm_grade", "final_grade" };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        if (grades == null || grades.isEmpty()) {
            // Nếu TableView trống, lấy từ database
            grades = getGradesListData();
            if (grades != null && !grades.isEmpty()) {
                // Nếu database có dữ liệu, xuất danh sách
                for (GradesData grade : grades) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(grade.getStudentID());
                    row.createCell(1).setCellValue(grade.getMidtermGrade());
                    row.createCell(2).setCellValue(grade.getFinalGrade());
                }
            }
        } else {
            // Nếu TableView có dữ liệu, xuất danh sách
            for (GradesData grade : grades) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(grade.getStudentID());
                row.createCell(1).setCellValue(grade.getMidtermGrade());
                row.createCell(2).setCellValue(grade.getFinalGrade());
            }
        }

        // Tự động điều chỉnh chiều rộng cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    // Xuất danh sách điểm số sang file CSV
    private void exportGradesToCsv(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String[] headers = { "student_id", "midterm_grade", "final_grade" };
            StringBuilder line = new StringBuilder();

            // Ghi tiêu đề
            for (int i = 0; i < headers.length; i++) {
                line.append(headers[i]);
                if (i < headers.length - 1)
                    line.append(",");
            }
            writer.write(line.toString());
            writer.newLine();

            ObservableList<GradesData> grades = tableGrades.getItems();
            if (grades != null && !grades.isEmpty()) {
                for (GradesData grade : grades) {
                    line.setLength(0); // Xóa nội dung cũ
                    line.append(grade.getStudentID()).append(",")
                            .append(grade.getMidtermGrade()).append(",")
                            .append(grade.getFinalGrade());
                    writer.write(line.toString());
                    writer.newLine();
                }
            } else {
                // Nếu TableView trống, lấy từ database
                grades = getGradesListData();
                if (grades != null && !grades.isEmpty()) {
                    for (GradesData grade : grades) {
                        line.setLength(0); // Xóa nội dung cũ
                        line.append(grade.getStudentID()).append(",")
                                .append(grade.getMidtermGrade()).append(",")
                                .append(grade.getFinalGrade());
                        writer.write(line.toString());
                        writer.newLine();
                    }
                }
            }
        }
    }

    private void suggestImprovement() {
        try {
            // Lấy danh sách môn cần cải thiện
            ObservableList<ImprovementData> improvements = getImprovementSuggestions();
            if (improvements.isEmpty()) {
                AlertComponent.showInformation("Thông báo", null, "Không có sinh viên nào cần cải thiện điểm!");
                return;
            }

            // Hiển thị dialog chọn hành động
            ChoiceDialog<String> actionDialog = new ChoiceDialog<>("Hiển thị trên giao diện", "Hiển thị trên giao diện",
                    "Xuất file");
            actionDialog.setTitle("Chọn hành động");
            actionDialog.setHeaderText("Vui lòng chọn cách hiển thị danh sách đề xuất:");
            actionDialog.setContentText("Hành động:");

            Optional<String> actionResult = actionDialog.showAndWait();
            if (!actionResult.isPresent())
                return;

            String action = actionResult.get();
            if ("Hiển thị trên giao diện".equals(action)) {
                showImprovementSuggestions(improvements);
            } else if ("Xuất file".equals(action)) {
                exportImprovementSuggestions(improvements);
            }
        } catch (SQLException e) {
            AlertComponent.showError("Lỗi", null, "Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            AlertComponent.showError("Lỗi", null, "Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private ObservableList<ImprovementData> getImprovementSuggestions() throws SQLException {
        ObservableList<ImprovementData> improvements = FXCollections.observableArrayList();
        String sql = """
                SELECT g.student_id, CONCAT(s.first_name, ' ', s.last_name) AS full_name,
                       c.course_name, g.total_grade
                FROM grades g
                JOIN students s ON g.student_id = s.student_id
                LEFT JOIN courses c ON g.course_id = c.course_id
                WHERE g.total_grade < 5.0
                """;

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                improvements.add(new ImprovementData(
                        resultSet.getString("student_id"),
                        resultSet.getString("full_name"),
                        resultSet.getString("course_name"),
                        resultSet.getFloat("total_grade")));
            }
        }
        return improvements;
    }

    private void showImprovementSuggestions(ObservableList<ImprovementData> improvements) {
        TableView<ImprovementData> table = new TableView<>();
        table.setItems(improvements);

        TableColumn<ImprovementData, String> studentIDCol = new TableColumn<>("MSSV");
        studentIDCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudentID()));

        TableColumn<ImprovementData, String> fullNameCol = new TableColumn<>("Họ tên");
        fullNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFullName()));

        TableColumn<ImprovementData, String> courseNameCol = new TableColumn<>("Môn học");
        courseNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCourseName()));

        TableColumn<ImprovementData, Number> totalGradeCol = new TableColumn<>("Điểm tổng kết");
        totalGradeCol.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getTotalGrade()));

        table.getColumns().addAll(studentIDCol, fullNameCol, courseNameCol, totalGradeCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Stage stage = new Stage();
        stage.setTitle("Danh sách môn cần cải thiện");
        stage.setScene(new Scene(table, 600, 400));
        stage.show();
    }

    private void exportImprovementSuggestions(ObservableList<ImprovementData> improvements) throws IOException {
        ChoiceDialog<String> formatDialog = new ChoiceDialog<>("JSON", "JSON", "Excel", "CSV");
        formatDialog.setTitle("Chọn định dạng file");
        formatDialog.setHeaderText("Vui lòng chọn định dạng file để xuất danh sách đề xuất:");
        formatDialog.setContentText("Định dạng:");

        Optional<String> formatResult = formatDialog.showAndWait();
        if (!formatResult.isPresent())
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file đề xuất môn cần cải thiện");
        fileChooser
                .setInitialFileName("DeXuatCaiThienDiem_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        String selectedFormat = formatResult.get();

        switch (selectedFormat) {
            case "JSON":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
                break;
            case "Excel":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                break;
            case "CSV":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                break;
        }
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            switch (selectedFormat) {
                case "JSON":
                    exportImprovementsToJson(file, improvements);
                    break;
                case "Excel":
                    exportImprovementsToExcel(file, improvements);
                    break;
                case "CSV":
                    exportImprovementsToCsv(file, improvements);
                    break;
            }
            AlertComponent.showInformation("Thành công", null, "Đã xuất file thành công: " + file.getName());
        }
    }

    private void exportImprovementsToJson(File file, ObservableList<ImprovementData> improvements) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> improvementList = improvements.stream().map(data -> {
            Map<String, Object> map = new HashMap<>();
            map.put("student_id", data.getStudentID());
            map.put("full_name", data.getFullName());
            map.put("course_name", data.getCourseName());
            map.put("total_grade", data.getTotalGrade());
            return map;
        }).collect(Collectors.toList());
        objectMapper.writeValue(file, improvementList);
    }

    private void exportImprovementsToExcel(File file, ObservableList<ImprovementData> improvements) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Đề xuất cải thiện điểm");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = { "MSSV", "Họ tên", "Môn học", "Điểm tổng kết" };
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        for (ImprovementData data : improvements) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getStudentID());
            row.createCell(1).setCellValue(data.getFullName());
            row.createCell(2).setCellValue(data.getCourseName());
            row.createCell(3).setCellValue(data.getTotalGrade());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    private void exportImprovementsToCsv(File file, ObservableList<ImprovementData> improvements) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("MSSV,Họ tên,Môn học,Điểm tổng kết");
            writer.newLine();

            for (ImprovementData data : improvements) {
                String line = String.format("%s,\"%s\",%s,%.2f",
                        data.getStudentID(), data.getFullName(), data.getCourseName(), data.getTotalGrade());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    private void accumulateCredits() {
        Connection connect = null;
        try {
            connect = database.connectDB();
            connect.setAutoCommit(false); // Bắt đầu transaction

            // Tính toán và cập nhật tín chỉ
            ObservableList<CreditData> creditData = updateAndGetAccumulatedCredits(connect);
            if (creditData.isEmpty()) {
                AlertComponent.showInformation("Thông báo", null, "Không có sinh viên nào để tích lũy tín chỉ!");
                connect.rollback();
                return;
            }

            // Hiển thị dialog chọn hành động
            ChoiceDialog<String> actionDialog = new ChoiceDialog<>("Hiển thị trên giao diện", "Hiển thị trên giao diện",
                    "Xuất file");
            actionDialog.setTitle("Chọn hành động");
            actionDialog.setHeaderText("Vui lòng chọn cách hiển thị tín chỉ tích lũy trước khi xóa:");
            actionDialog.setContentText("Hành động:");

            Optional<String> actionResult = actionDialog.showAndWait();
            if (!actionResult.isPresent()) {
                connect.rollback();
                return;
            }

            String action = actionResult.get();
            if ("Hiển thị trên giao diện".equals(action)) {
                showAccumulatedCredits(creditData);
            } else if ("Xuất file".equals(action)) {
                exportAccumulatedCredits(creditData);
            }

            // Xác nhận xóa sinh viên
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Xác nhận xóa");
            confirmation.setHeaderText("Bạn có muốn xóa các sinh viên đã tích lũy tín chỉ?");
            confirmation.setContentText("Dữ liệu điểm và thông tin sinh viên sẽ bị xóa khỏi hệ thống.");
            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteStudentsAfterAccumulation(connect, creditData);
                AlertComponent.showInformation("Thành công", null, "Đã xóa sinh viên sau khi tích lũy tín chỉ!");
                // Cập nhật lại giao diện quản lý sinh viên và điểm
                addStudentsShowList();
                showGradesList();
            } else {
                AlertComponent.showInformation("Hủy bỏ", null, "Dữ liệu sinh viên được giữ nguyên.");
            }

            connect.commit();
        } catch (SQLException e) {
            if (connect != null) {
                try {
                    connect.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            AlertComponent.showError("Lỗi", null, "Lỗi truy vấn hoặc cập nhật cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            if (connect != null) {
                try {
                    connect.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            AlertComponent.showError("Lỗi", null, "Không thể xuất file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            if (connect != null) {
                try {
                    connect.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            AlertComponent.showError("Lỗi", null, "Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connect != null) {
                try {
                    connect.setAutoCommit(true);
                    connect.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    private ObservableList<CreditData> updateAndGetAccumulatedCredits(Connection connect) throws SQLException {
        ObservableList<CreditData> creditData = FXCollections.observableArrayList();

        // Tính tổng tín chỉ cho từng sinh viên
        String selectSql = """
                SELECT g.student_id, CONCAT(s.first_name, ' ', s.last_name) AS full_name,
                       COALESCE(SUM(c.credits), 0) AS total_credits
                FROM grades g
                JOIN students s ON g.student_id = s.student_id
                JOIN courses c ON g.course_id = c.course_id
                WHERE g.total_grade >= 5.0
                GROUP BY g.student_id, s.first_name, s.last_name
                """;

        Map<String, CreditData> creditMap = new HashMap<>();
        try (PreparedStatement selectStmt = connect.prepareStatement(selectSql);
                ResultSet resultSet = selectStmt.executeQuery()) {
            while (resultSet.next()) {
                String studentID = resultSet.getString("student_id");
                String fullName = resultSet.getString("full_name");
                int totalCredits = resultSet.getInt("total_credits");
                creditMap.put(studentID, new CreditData(studentID, fullName, totalCredits));
            }
        }

        if (creditMap.isEmpty()) {
            return creditData;
        }

        // Cập nhật tín chỉ tích lũy vào bảng students
        String updateSql = "UPDATE students SET credits_accumulated = ? WHERE student_id = ?";
        try (PreparedStatement updateStmt = connect.prepareStatement(updateSql)) {
            for (CreditData data : creditMap.values()) {
                updateStmt.setInt(1, data.getCreditsAccumulated());
                updateStmt.setString(2, data.getStudentID());
                updateStmt.executeUpdate();
                creditData.add(data);
            }
        }

        return creditData;
    }

    private void deleteStudentsAfterAccumulation(Connection connect, ObservableList<CreditData> creditData)
            throws SQLException {
        // Xóa khỏi bảng grades
        String deleteGradesSql = "DELETE FROM grades WHERE student_id = ?";
        try (PreparedStatement deleteGradesStmt = connect.prepareStatement(deleteGradesSql)) {
            for (CreditData data : creditData) {
                deleteGradesStmt.setString(1, data.getStudentID());
                deleteGradesStmt.executeUpdate();
            }
        }

        // Xóa khỏi bảng students
        String deleteStudentsSql = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement deleteStudentsStmt = connect.prepareStatement(deleteStudentsSql)) {
            for (CreditData data : creditData) {
                deleteStudentsStmt.setString(1, data.getStudentID());
                deleteStudentsStmt.executeUpdate();
            }
        }
    }

    private void updateAccumulatedCredits() throws SQLException {
        Connection connect = null;
        try {
            connect = database.connectDB();
            connect.setAutoCommit(false); // Bắt đầu transaction để tránh xung đột

            // Bước 1: Tính tổng tín chỉ cho từng sinh viên
            String selectSql = """
                    SELECT g.student_id, COALESCE(SUM(c.credits), 0) AS total_credits
                    FROM grades g
                    JOIN courses c ON g.course_id = c.course_id
                    WHERE g.total_grade >= 5.0
                    GROUP BY g.student_id
                    """;

            Map<String, Integer> creditMap = new HashMap<>();
            try (PreparedStatement selectStmt = connect.prepareStatement(selectSql);
                    ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    creditMap.put(resultSet.getString("student_id"), resultSet.getInt("total_credits"));
                }
            }

            // Bước 2: Cập nhật tín chỉ tích lũy vào bảng students
            String updateSql = "UPDATE students SET credits_accumulated = ? WHERE student_id = ?";
            try (PreparedStatement updateStmt = connect.prepareStatement(updateSql)) {
                int rowsAffected = 0;
                for (Map.Entry<String, Integer> entry : creditMap.entrySet()) {
                    updateStmt.setInt(1, entry.getValue());
                    updateStmt.setString(2, entry.getKey());
                    rowsAffected += updateStmt.executeUpdate();
                }

                if (rowsAffected > 0) {
                    AlertComponent.showInformation("Thành công", null,
                            "Đã cập nhật tín chỉ tích lũy cho " + rowsAffected + " sinh viên!");
                } else {
                    AlertComponent.showInformation("Thông báo", null, "Không có tín chỉ nào được cập nhật!");
                }
            }

            connect.commit(); // Commit transaction
        } catch (SQLException e) {
            if (connect != null) {
                try {
                    connect.rollback(); // Rollback nếu có lỗi
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e; // Ném lại ngoại lệ để xử lý ở cấp cao hơn
        } finally {
            if (connect != null) {
                try {
                    connect.setAutoCommit(true);
                    connect.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    private ObservableList<CreditData> getAccumulatedCredits() throws SQLException {
        ObservableList<CreditData> creditData = FXCollections.observableArrayList();
        String sql = """
                SELECT s.student_id, CONCAT(s.first_name, ' ', s.last_name) AS full_name, s.credits_accumulated
                FROM students s
                WHERE s.credits_accumulated > 0
                """;

        try (Connection connect = database.connectDB();
                PreparedStatement preparedStatement = connect.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                creditData.add(new CreditData(
                        resultSet.getString("student_id"),
                        resultSet.getString("full_name"),
                        resultSet.getInt("credits_accumulated")));
            }
        }
        return creditData;
    }

    private void showAccumulatedCredits(ObservableList<CreditData> creditData) {
        TableView<CreditData> table = new TableView<>();
        table.setItems(creditData);

        TableColumn<CreditData, String> studentIDCol = new TableColumn<>("MSSV");
        studentIDCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudentID()));

        TableColumn<CreditData, String> fullNameCol = new TableColumn<>("Họ tên");
        fullNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFullName()));

        TableColumn<CreditData, Number> creditsCol = new TableColumn<>("Tín chỉ tích lũy");
        creditsCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCreditsAccumulated()));

        table.getColumns().addAll(studentIDCol, fullNameCol, creditsCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Stage stage = new Stage();
        stage.setTitle("Danh sách tín chỉ tích lũy");
        stage.setScene(new Scene(table, 500, 400));
        stage.show();
    }

    private void exportAccumulatedCredits(ObservableList<CreditData> creditData) throws IOException {
        ChoiceDialog<String> formatDialog = new ChoiceDialog<>("JSON", "JSON", "Excel", "CSV");
        formatDialog.setTitle("Chọn định dạng file");
        formatDialog.setHeaderText("Vui lòng chọn định dạng file để xuất tín chỉ tích lũy:");
        formatDialog.setContentText("Định dạng:");

        Optional<String> formatResult = formatDialog.showAndWait();
        if (!formatResult.isPresent())
            return;

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu file tín chỉ tích lũy");
        fileChooser.setInitialFileName("TinChiTichLuy_" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        String selectedFormat = formatResult.get();

        switch (selectedFormat) {
            case "JSON":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
                break;
            case "Excel":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                break;
            case "CSV":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
                break;
        }
        fileChooser.setSelectedExtensionFilter(fileChooser.getExtensionFilters().get(0));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            switch (selectedFormat) {
                case "JSON":
                    exportCreditsToJson(file, creditData);
                    break;
                case "Excel":
                    exportCreditsToExcel(file, creditData);
                    break;
                case "CSV":
                    exportCreditsToCsv(file, creditData);
                    break;
            }
            AlertComponent.showInformation("Thành công", null, "Đã xuất file thành công: " + file.getName());
        }
    }

    private void exportCreditsToJson(File file, ObservableList<CreditData> creditData) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> creditList = creditData.stream().map(data -> {
            Map<String, Object> map = new HashMap<>();
            map.put("student_id", data.getStudentID());
            map.put("full_name", data.getFullName());
            map.put("credits_accumulated", data.getCreditsAccumulated());
            return map;
        }).collect(Collectors.toList());
        objectMapper.writeValue(file, creditList);
    }

    private void exportCreditsToExcel(File file, ObservableList<CreditData> creditData) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tín chỉ tích lũy");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = { "MSSV", "Họ tên", "Tín chỉ tích lũy" };
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        for (CreditData data : creditData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getStudentID());
            row.createCell(1).setCellValue(data.getFullName());
            row.createCell(2).setCellValue(data.getCreditsAccumulated());
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();
    }

    private void exportCreditsToCsv(File file, ObservableList<CreditData> creditData) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("MSSV,Họ tên,Tín chỉ tích lũy");
            writer.newLine();

            for (CreditData data : creditData) {
                String line = String.format("%s,\"%s\",%d",
                        data.getStudentID(), data.getFullName(), data.getCreditsAccumulated());
                writer.write(line);
                writer.newLine();
            }
        }
    }

    // ========== QUẢN LÝ ĐIỂM ==========

    // ========== THÔNG TIN ==========

    private void showForm(AnchorPane selectedForm) {
        // Ẩn tất cả các form
        versionForm.setVisible(false);
        teamForm.setVisible(false);
        technologyForm.setVisible(false);
        clauseForm.setVisible(false);
        questionForm.setVisible(false);

        // Hiển thị form được chọn
        selectedForm.setVisible(true);
    }

    // Phiên bản
    private void versionBtnHandle() {
        showForm(versionForm);
    }

    // Đội ngũ phát triển
    private void teamBtnHandle() {
        showForm(teamForm);
    }

    // Công nghệ sử dụng
    private void technologyBtnHandle() {
        showForm(technologyForm);
    }

    // Điều khoản sử dụng
    private void clauseBtnHandle() {
        showForm(clauseForm);
    }

    // Câu hỏi thường gặp
    private void questionBtnHandle() {
        showForm(questionForm);
    }

    // ========== THÔNG TIN ==========

    // ---> END CÁC HÀM XỬ LÝ <---
}
