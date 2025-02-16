package model;

public class LecturerData {
    private String lecturerID;
    private String lecturerName;
    private String gender;
    private String degree;
    private String phone;
    private String status;

    // Constructor
    public LecturerData(String lecturerID, String lecturerName, String gender, String degree, String phone, String status) {
        this.lecturerID = lecturerID;
        this.lecturerName = lecturerName;
        this.gender = gender;
        this.degree = degree;
        this.phone = phone;
        this.status = status;
    }

    // Getters
    public String getLecturerID() {
        return lecturerID;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public String getGender() {
        return gender;
    }

    public String getDegree() {
        return degree;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setLecturerID(String lecturerID) {
        this.lecturerID = lecturerID;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return lecturerName;
    }
}
