package model;

public class GradesData {
    private String studentID;
    private String schoolYear;
    private String courseID;  // Thêm courseID để lưu khóa chính của môn học
    private String courseName;
    private float midtermGrade;
    private float finalGrade;
    private float totalGrade;

    // Defalut Constructor
    public GradesData() {}

    // Constructor CHUẨN XÁC theo database
    public GradesData(String studentID, String schoolYear, String courseID, String courseName, 
                      float midtermGrade, float finalGrade, float totalGrade) {
        this.studentID = studentID;
        this.schoolYear = schoolYear;
        this.courseID = courseID;
        this.courseName = courseName;
        this.midtermGrade = midtermGrade;
        this.finalGrade = finalGrade;
        this.totalGrade = totalGrade; // Dữ liệu đã tính từ SQL
    }

    // Getter & Setter
    public String getStudentID() {
        return studentID;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public float getMidtermGrade() {
        return midtermGrade;
    }

    public float getFinalGrade() {
        return finalGrade;
    }

    public float getTotalGrade() {
        return totalGrade;
    }

    public void setMidtermGrade(float midtermGrade) {
        this.midtermGrade = midtermGrade;
        this.totalGrade = calculateTotalGrade();
    }

    public void setFinalGrade(float finalGrade) {
        this.finalGrade = finalGrade;
        this.totalGrade = calculateTotalGrade();
    }

    // Tính lại điểm tổng kết khi cần
    private float calculateTotalGrade() {
        return (midtermGrade * 0.4f) + (finalGrade * 0.6f);
    }
}
