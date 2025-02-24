package model;

public class CourseData {
    private String subjectID;
    private String subjectName;
    private int credits;
    private String lecturer;
    private String semester;
    private String status;

    public CourseData() {}

    // Constructor
    public CourseData(String subjectID, String subjectName, int credits, String lecturer, String semester, String status) {
        this.subjectID = subjectID;
        this.subjectName = subjectName;
        this.credits = credits;
        this.lecturer = lecturer;
        this.semester = semester;
        this.status = status;
    }

    // Getter & Setter
    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
