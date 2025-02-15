package model;

import java.util.Date;

public class StudentData {
    private String studentID;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String gender;
    private String schoolYear;
    private String major;
    private String subject;
    private String status;
    private String photoPath;

    // Constructor
    public StudentData(String studentID, String firstName, String lastName, Date birthDate, String gender, String schoolYear, String major, String subject, String status, String photoPath) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.schoolYear = schoolYear;
        this.major = major;
        this.subject = subject;
        this.status = status;
        this.photoPath = photoPath;
    }

    // Getters & Setters
    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    // toString() method to debug ez
    @Override
    public String toString() {
        return "Student{" +
                "studentID='" + studentID + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", schoolYear='" + schoolYear + '\'' +
                ", major='" + major + '\'' +
                ", subject='" + subject + '\'' +
                ", status='" + status + '\'' +
                ", photoPath='" + photoPath + '\'' +
                '}';
    }
}
