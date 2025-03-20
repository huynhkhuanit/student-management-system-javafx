package model;

public class ImprovementData {
    private final String studentID;
    private final String fullName;
    private final String courseName;
    private final float totalGrade;

    public ImprovementData(String studentID, String fullName, String courseName, float totalGrade) {
        this.studentID = studentID;
        this.fullName = fullName;
        this.courseName = courseName;
        this.totalGrade = totalGrade;
    }

    public String getStudentID() { return studentID; }
    public String getFullName() { return fullName; }
    public String getCourseName() { return courseName; }
    public float getTotalGrade() { return totalGrade; }
}