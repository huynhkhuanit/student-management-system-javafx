package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javax.security.auth.Subject;

import components.AlertComponent;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import util.showStage;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.stage.StageStyle;
import java.sql.Statement;
import java.util.Date;
import java.time.LocalDate;

// model data
import model.StudentData;
import model.CourseData;
import model.LecturerData;
import database.database;
import controller.studentController;

public class dashboardController {
    // Database tools
    private Connection connect;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;

    @FXML
    private Button closeBtn;

    @FXML
    private Button dashboardBtn;

    @FXML
    private AnchorPane dashboardForm;

    @FXML
    private StackPane mainDashboard;

    @FXML
    private Button minimizeBtn, logoutBtn;

    @FXML
    private ResourceBundle resources;

    // ========== DASHBOARD PROPERTY ==========

    @FXML
    private Label totalStudentsLabel, totalMaleStudentsLabel, totalFemaleStudentsLabel;
    @FXML
    private AreaChart<?, ?> avgScoreChart;
    @FXML
    private LineChart<?, ?> yearlyStatsChart;
    @FXML
    private BarChart<?, ?> totalStudentsChart;

    // ========== DASHBOARD PROPERTY ==========

    // ========== SINH VIÊN PROPERTY ==========
    @FXML
    private AnchorPane studentManageForm;
    @FXML
    private Button studentManageBtn;

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

    // 🔘 Khu vực chứa các nút điều khiển
    @FXML
    private HBox controlButtonsBox;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClearAll;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnRefresh;

    @FXML
    private Button btnUpdate;

    // Ô tìm kiếm
    @FXML
    private TextField studentManageSearch;

    // ========== SINH VIÊN PROPERTY ==========

    // ========== MÔN HỌC PROPERTY ==========

    @FXML
    private Button courseManageBtn;
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
    private Button lecturerManageBtn;
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

    // - Cần tạo một đối tượng Lecturer
    // private ObservableList<Lecturer> lecturerList =
    // FXCollections.observableArrayList();

    // =========== QUẢN LÝ GIẢNG VIÊN ===========

    // =========== QUẢN LÝ ĐIỂM ===========

    @FXML
    private AnchorPane gradesManageForm;
    @FXML
    private TextField txtStudentIDGrades, txtMidtermGrade, txtFinalGrade, txtTotalGrade, txtSearchGrades;

    @FXML
    private Label lblSchoolYearGrades, lblSubjectGrades;

    @FXML
    private Button btnAddGrades, btnRefreshGrades, btnDeleteGrades, btnClearAllGrades, btnUpdateGrades,
            btnClearFormGrades, gradesManageBtn;

    @FXML
    private TableView<?> tableGrades;

    @FXML
    private TableColumn<?, ?> colStudentIDGrades, colSchoolYearGrades, colSubjectGrades, colMidtermGrade,
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
        // ========== Load dữ liệu từ database cho các bảng ==========

        addStudentsShowList(); // Load dữ liệu sinh viên
        showCoursesList(); // Load dữ liệu môn học
        showLecturersList(); // Load dữ liệu giảng viên

        updateLecturersInCourse(); // Cập nhật danh sách giảng viên trong ComboBox

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

        // ========== SHOW FORMS ==========
        dashboardBtn.setOnAction(e -> switchForm(dashboardForm, dashboardBtn));
        studentManageBtn.setOnMouseClicked(e -> switchForm(studentManageForm, studentManageBtn));
        courseManageBtn.setOnMouseClicked(e -> switchForm(courseManageForm, courseManageBtn));
        lecturerManageBtn.setOnMouseClicked(e -> switchForm(lecturerManageForm, lecturerManageBtn));
        gradesManageBtn.setOnMouseClicked(e -> switchForm(gradesManageForm, gradesManageBtn));
        infoManageBtn.setOnMouseClicked(e -> switchForm(infoManageForm, infoManageBtn));

        // ========== SHOW FORMS ==========

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
            addStudentsShowList();
        }); // Phương thức làm mới bảng sinh viên
        btnUpdate.setOnAction(e -> handleUpdateStudent()); // Phương thức cập nhật sinh viên
        btnDelete.setOnAction(e -> handleDeleteStudent()); // Phương thức xóa sinh viên
        btnClearAll.setOnAction(e -> handleDeleteAllStudents()); // Phương thức xóa toàn bộ sinh viên
        studentManageSearch.textProperty().addListener((observable, oldValue, newValue) -> searchStudent());
        // Tìm kiếm sinh viên

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

        // ========== QUẢN LÝ GIẢNG VIÊN ==========

        // ========== QUẢN LÝ ĐIỂM ==========

        // Gán dữ liệu mặc định cho bảng điểm
        setupTableColumns();
        loadGradesData();

        // Sự kiện nút thêm điểm
        btnAddGrades.setOnAction(e -> handleAddGrades());

        // Sự kiện nút cập nhật điểm
        btnUpdateGrades.setOnAction(e -> handleUpdateGrades());

        // Sự kiện nút xóa điểm
        btnDeleteGrades.setOnAction(e -> handleDeleteGrades());

        // Sự kiện nút xóa toàn bộ điểm
        btnClearAllGrades.setOnAction(e -> handleClearAllGrades());

        // Sự kiện nút làm mới form
        btnRefreshGrades.setOnAction(e -> handleRefreshGrades());

        // Sự kiện nút xóa thông tin nhập liệu
        btnClearFormGrades.setOnAction(e -> clearGradesForm());

        // Gán giá trị mặc định
        btnAddGrades.setDisable(true);

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

    // ========== QUẢN LÝ SINH VIÊN ==========

    public ObservableList<StudentData> addStudentsListData() {
        ObservableList<StudentData> listStudents = FXCollections.observableArrayList();
        String sql = "SELECT * FROM students";
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

    private ObservableList<StudentData> addStudentsListD;

    public void addStudentsShowList() {
        addStudentsListD = addStudentsListData();

        student_colStudentID.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        student_colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        student_colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        student_colBirthYear.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        student_colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        student_colSchoolYear.setCellValueFactory(new PropertyValueFactory<>("schoolYear"));
        student_colMajor.setCellValueFactory(new PropertyValueFactory<>("major"));
        student_colSubject.setCellValueFactory(new PropertyValueFactory<>("subject"));
        student_colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        studentTable.setItems(addStudentsListD);
    }

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

    // MORE...

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
        String sql = "SELECT * FROM courses";

        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                courseList.add(new CourseData(
                        resultSet.getString("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getInt("credits"),
                        resultSet.getString("lecturer"),
                        resultSet.getString("semester"),
                        resultSet.getString("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseList;
    }

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

        // Load dữ liệu vào các ô nhập liệu
        txtSubjectID.setText(selectedCourse.getSubjectID());
        txtSubjectName.setText(selectedCourse.getSubjectName());
        txtCredits.setText(String.valueOf(selectedCourse.getCredits()));
        cbLecturer.setValue(selectedCourse.getLecturer());
        cbSemester.setValue(selectedCourse.getSemester());
        cbStatus.setValue(selectedCourse.getStatus());

        // Khóa ô nhập mã môn học để tránh thay đổi
        txtSubjectID.setDisable(true);
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

    private void handleAddCourse() {

        if (!validateCourseInput())
            return; // Kiểm tra input hợp lệ

        String courseID = txtSubjectID.getText().trim();
        String courseName = txtSubjectName.getText().trim();
        int credits = Integer.parseInt(txtCredits.getText().trim());
        String lecturer = cbLecturer.getValue();
        String semester = cbSemester.getValue();
        String status = cbStatus.getValue();

        // Kiểm tra trùng mã môn học
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
            preparedStatement.setString(4, lecturer);
            preparedStatement.setString(5, semester);
            preparedStatement.setString(6, status);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                AlertComponent.showInformation("Thành công", null, "Môn học đã được thêm thành công!");
                showCoursesList(); // Cập nhật danh sách môn học

                updateStudentSubjectsComboBox();

                clearFormSubject(); // Xóa form nhập
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
        CourseData selectedCourse = tblSubjects.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một môn học để xóa!");
        }

        boolean confirm = AlertComponent.showConfirmation("Xác nhận", null,
                "Bạn có chắc chắn muốn xóa môn học: " + selectedCourse.getSubjectName() + "?");

        if (!confirm)
            return;

        // Xóa trong database
        String sql = "DELETE FROM courses WHERE course_id = ?";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, selectedCourse.getSubjectID());
            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                AlertComponent.showInformation("Thành công", null, "Môn học đã được xóa!");
                showCoursesList();

                updateStudentSubjectsComboBox();
            } else {
                AlertComponent.showError("Lỗi", null, "Không thể xóa môn học!");
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
        // Lấy môn học được chọn từ TableView
        CourseData selectedCourse = tblSubjects.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            AlertComponent.showWarning("Lỗi", null, "Vui lòng chọn một môn học để cập nhật!");
            return;
        }

        // Kiểm tra nếu có trường nào bị bỏ trống
        if (txtSubjectName.getText().trim().isEmpty() ||
                txtCredits.getText().trim().isEmpty() ||
                cbLecturer.getValue() == null || cbLecturer.getValue().equals("Chọn") ||
                cbSemester.getValue() == null || cbSemester.getValue().equals("Chọn") ||
                cbStatus.getValue() == null || cbStatus.getValue().equals("Chọn")) {

            AlertComponent.showWarning("Lỗi nhập dữ liệu", null, "Vui lòng điền đầy đủ thông tin môn học!");
            return;
        }

        // Kiểm tra số tín chỉ phải là số nguyên dương
        int credits;
        try {
            credits = Integer.parseInt(txtCredits.getText().trim());
            if (credits <= 0) {
                AlertComponent.showWarning("Lỗi nhập dữ liệu", null, "Số tín chỉ phải là số nguyên dương!");
                return;
            }
        } catch (NumberFormatException e) {
            AlertComponent.showWarning("Lỗi nhập dữ liệu", null, "Số tín chỉ phải là số nguyên dương!");
            return;
        }

        // Kiểm tra tên môn học có bị trùng không (trừ môn học đang chỉnh sửa)
        String checkQuery = "SELECT COUNT(*) FROM courses WHERE course_name = ? AND course_id != ?";
        connect = database.connectDB();
        try {
            preparedStatement = connect.prepareStatement(checkQuery);
            preparedStatement.setString(1, txtSubjectName.getText().trim());
            preparedStatement.setString(2, selectedCourse.getSubjectID());
            resultSet = preparedStatement.executeQuery();
            resultSet.next();

            if (resultSet.getInt(1) > 0) {
                AlertComponent.showWarning("Lỗi nhập dữ liệu", null, "Tên môn học đã tồn tại, vui lòng nhập tên khác!");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cập nhật dữ liệu môn học
        String updateQuery = "UPDATE courses SET course_name = ?, credits = ?, lecturer = ?, semester = ?, status = ? WHERE course_id = ?";

        try {
            preparedStatement = connect.prepareStatement(updateQuery);
            preparedStatement.setString(1, txtSubjectName.getText().trim());
            preparedStatement.setInt(2, credits);
            preparedStatement.setString(3, cbLecturer.getValue());
            preparedStatement.setString(4, cbSemester.getValue());
            preparedStatement.setString(5, cbStatus.getValue());
            preparedStatement.setString(6, selectedCourse.getSubjectID());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                AlertComponent.showInformation("Thành công", null, "Môn học đã được cập nhật!");
                showCoursesList(); // Làm mới danh sách môn học
                clearFormSubject(); // Xoá form nhập liệu
            } else {
                AlertComponent.showError("Lỗi", null, "Cập nhật môn học không thành công!");
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

    // ========== QUẢN LÝ MÔN HỌC ==========

    // ========== QUẢN LÝ GIẢNG VIÊN ==========

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
        String sql = "SELECT lecturer_name, degree, status FROM lecturers"; // Lấy cả tên, học vị và trạng thái

        connect = database.connectDB();
        try {
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String lecturerName = resultSet.getString("lecturer_name");
                String degree = resultSet.getString("degree");
                String status = resultSet.getString("status"); // Lấy trạng thái của giảng viên

                // Logic -> Nếu giảng viên nghỉ phép hoặc đã nghỉ hưu thì không thêm vào
                // ComboBox
                if (status.equalsIgnoreCase("Nghỉ phép") || status.equalsIgnoreCase("Đã nghỉ hưu")) {
                    continue;
                }

                String formattedName = formatLecturerName(lecturerName, degree);
                lecturerList.add(formattedName);
            }
            cbLecturer.setItems(lecturerList); // Cập nhật ComboBox giảng viên trong Quản lý Môn học
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

    // Kiểm tra mã giảng viên đã tồn tại hay chưa?
    private boolean isLecturerIDExists(String lecturerID) {
        String sql = "SELECT COUNT(*) FROM lecturers WHERE lecturer_id = ?";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, lecturerID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;
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
        return false;
    }

    // Kiểm tra số điện thoại đã tồn tại chưa
    // Kiểm tra số điện thoại đã tồn tại trong database (trừ giảng viên hiện tại)
    private boolean isPhoneNumberExists(String phone, String currentLecturerID) {
        String sql = "SELECT COUNT(*) FROM lecturers WHERE phone = ? AND lecturer_id != ?";
        connect = database.connectDB();

        try {
            preparedStatement = connect.prepareStatement(sql);
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, currentLecturerID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;
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

    private void handleClearFormLecturer() {
        // Xử lý các logic khi xoá form nhập liệu
        txtLecturerID.setDisable(false); // Mở khóa ô nhập mã giảng viên
        btnAddLecturer.setDisable(false); // Mở khóa nút thêm giảng viên

        // Clear all forms
        txtLecturerID.clear();
        txtLecturerName.clear();
        txtLecturerPhone.clear();
        cbLecturerGender.setValue(null);
        cbLecturerDegree.setValue(null);
        cbLecturerStatus.setValue(null);
    }

    // ========== QUẢN LÝ GIẢNG VIÊN ==========

    // ========== QUẢN LÝ ĐIỂM ==========

    private void setupTableColumns() {
        colStudentIDGrades.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        colSchoolYearGrades.setCellValueFactory(new PropertyValueFactory<>("schoolYear"));
        colSubjectGrades.setCellValueFactory(new PropertyValueFactory<>("subject"));
        colMidtermGrade.setCellValueFactory(new PropertyValueFactory<>("midterm"));
        colFinalGrade.setCellValueFactory(new PropertyValueFactory<>("finalExam"));
        colTotalGrade.setCellValueFactory(new PropertyValueFactory<>("totalGrade"));
    }

    private void loadGradesData() {
        // ObservableList<Grades> gradesList = FXCollections.observableArrayList();

        // // Giả lập dữ liệu từ database
        // gradesList.add(new Grades("SV001", "2023-2024", "Lập trình Java", 7.5, 8.0,
        // 7.75));
        // gradesList.add(new Grades("SV002", "2023-2024", "Cấu trúc dữ liệu", 6.0, 7.5,
        // 6.75));

        // tableGrades.setItems(gradesList);
    }

    private void handleAddGrades() {
        System.out.println("Add grades");
    }

    private void handleUpdateGrades() {
        System.out.println("Update grades");
    }

    private void handleDeleteGrades() {
        System.out.println("Delete grades");
    }

    private void handleClearAllGrades() {
        System.out.println("Clear all grades");
    }

    private void handleRefreshGrades() {
        System.out.println("Refresh grades");
    }

    private void clearGradesForm() {
        txtStudentIDGrades.clear();
        lblSchoolYearGrades.setText("-");
        lblSubjectGrades.setText("-");
        txtMidtermGrade.clear();
        txtFinalGrade.clear();
        txtTotalGrade.clear();
    }

    // ========== QUẢN LÝ ĐIỂM ==========

    // ========== THÔNG TIN ==========

    private void versionBtnHandle() {
        if (teamForm.isVisible() || technologyForm.isVisible() || clauseForm.isVisible() || questionForm.isVisible()) {
            questionForm.setVisible(false);
            teamForm.setVisible(false);
            technologyForm.setVisible(false);
            clauseForm.setVisible(false);
        }
        versionForm.setVisible(true);
    }

    private void teamBtnHandle() {
        if (versionForm.isVisible() || technologyForm.isVisible() || clauseForm.isVisible()
                || questionForm.isVisible()) {
            questionForm.setVisible(false);
            versionForm.setVisible(false);
            technologyForm.setVisible(false);
            clauseForm.setVisible(false);
        }
        teamForm.setVisible(true);
    }

    private void technologyBtnHandle() {
        if (versionForm.isVisible() || teamForm.isVisible() || clauseForm.isVisible() || questionForm.isVisible()) {
            versionForm.setVisible(false);
            teamForm.setVisible(false);
            clauseForm.setVisible(false);
            questionForm.setVisible(false);
        }
        technologyForm.setVisible(true);
    }

    private void clauseBtnHandle() {
        if (versionForm.isVisible() || teamForm.isVisible() || technologyForm.isVisible() || questionForm.isVisible()) {
            versionForm.setVisible(false);
            teamForm.setVisible(false);
            technologyForm.setVisible(false);
            questionForm.setVisible(false);
        }
        clauseForm.setVisible(true);
    }

    private void questionBtnHandle() {
        if (versionForm.isVisible() || teamForm.isVisible() || technologyForm.isVisible() || clauseForm.isVisible()) {
            versionForm.setVisible(false);
            teamForm.setVisible(false);
            technologyForm.setVisible(false);
            clauseForm.setVisible(false);
        }
        questionForm.setVisible(true);
    }

    // ========== THÔNG TIN ==========

    // ---> END CÁC HÀM XỬ LÝ <---
}
