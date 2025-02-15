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
import database.database;

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
    private TextField txtSubjectID, txtSubjectName, txtCredits, txtLecturer;
    @FXML
    private ComboBox<String> cbSemester, cbStatus;
    @FXML
    private TableView<Subject> tblSubjects;
    @FXML
    private TableColumn<Subject, String> colSubjectID, colSubjectName, colLecturer, colSemester, colStatus;
    @FXML
    private TableColumn<Subject, Integer> colCredits;
    @FXML
    private Button btnAddSubject, btnRefreshSubject, btnDeleteSubject, btnClearAllSubjects, btnUpdateSubject,
            btnClearFormSubject;

    private ObservableList<Subject> subjectList = FXCollections.observableArrayList();

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
    private TableView<?> tableLecturer;
    @FXML
    private TableColumn<?, ?> colLecturerID, colLecturerName, colLecturerGender, colLecturerDegree, colLecturerPhone,
            colLecturerStatus;
    @FXML
    private Button btnAddLecturer, btnRefreshLecturer, btnDeleteLecturer, btnClearAllLecturer, btnUpdateLecturer,
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

        addStudentsShowList();

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
        studentManageSearch.textProperty().addListener((observable, oldValue, newValue) -> searchStudent()); // Tìm kiếm sinh viên

        // ========== QUẢN LÝ SINH VIÊN ==========

        // ========== QUẢN LÝ MÔN HỌC ==========

        cbSemester.getItems().addAll("Chọn", "Học kỳ 1", "Học kỳ 2", "Học kỳ 3");
        cbStatus.getItems().addAll("Chọn", "Đang mở", "Đóng");

        colSubjectID.setCellValueFactory(new PropertyValueFactory<>("subjectID"));
        colSubjectName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        colCredits.setCellValueFactory(new PropertyValueFactory<>("credits"));
        colLecturer.setCellValueFactory(new PropertyValueFactory<>("lecturer"));
        colSemester.setCellValueFactory(new PropertyValueFactory<>("semester"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tblSubjects.setItems(subjectList);

        btnClearFormSubject.setOnAction(e -> clearFormSubject());

        // ========== QUẢN LÝ MÔN HỌC ==========

        // ========== QUẢN LÝ GIẢNG VIÊN ==========

        colLecturerID.setCellValueFactory(new PropertyValueFactory<>("lecturerID"));
        colLecturerName.setCellValueFactory(new PropertyValueFactory<>("lecturerName"));
        colLecturerGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colLecturerDegree.setCellValueFactory(new PropertyValueFactory<>("degree"));
        colLecturerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colLecturerStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Khởi tạo dữ liệu cho ComboBox
        cbLecturerGender.getItems().addAll("Chọn", "Nam", "Nữ");
        cbLecturerDegree.getItems().addAll("Chọn", "Cử nhân", "Thạc sĩ", "Tiến sĩ", "Phó giáo sư", "Giáo sư");
        cbLecturerStatus.getItems().addAll("Chọn", "Đang giảng dạy", "Nghỉ phép", "Đã nghỉ hưu");

        // Thêm sự kiện cho các Button
        btnAddLecturer.setOnAction(e -> AddLecturerHandle());
        btnUpdateLecturer.setOnAction(e -> UpdateLecturerHandle());
        btnDeleteLecturer.setOnAction(e -> DeleteLecturerHandle());
        btnClearFormLecturer.setOnAction(e -> clearFormLecturerHandle());
        btnClearAllLecturer.setOnAction(e -> clearAllLecturerHandle());
        btnRefreshLecturer.setOnAction(e -> refreshLecturerHandle());

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
        txtSubjectID.clear();
        txtSubjectName.clear();
        txtCredits.clear();
        txtLecturer.clear();
        cbSemester.setValue("Chọn");
        cbStatus.setValue("Chọn");
    }

    // ========== QUẢN LÝ MÔN HỌC ==========

    // ========== QUẢN LÝ GIẢNG VIÊN ==========
    private void AddLecturerHandle() {
        System.out.println("Add lecturer");
    }

    private void UpdateLecturerHandle() {
        System.out.println("Update lecturer");
    }

    private void DeleteLecturerHandle() {
        System.out.println("Delete lecturer");
    }

    private void clearFormLecturerHandle() {
        // Clear all forms
        txtLecturerID.clear();
        txtLecturerName.clear();
        txtLecturerPhone.clear();
        cbLecturerGender.setValue(null);
        cbLecturerDegree.setValue(null);
        cbLecturerStatus.setValue(null);
    }

    private void clearAllLecturerHandle() {
        System.out.println("Clear all lecturer");
    }

    private void refreshLecturerHandle() {
        System.out.println("Refresh lecturer");
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
