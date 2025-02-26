package model;

public class ColumnHeader {
    private String student_id, first_name, last_name, birth_date, gender, school_year, major, subject, status, photo_path;

    public ColumnHeader(String student_id, String first_name, String last_name, String birth_date, String gender,
                       String school_year, String major, String subject, String status, String photo_path) {
        this.student_id = student_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.gender = gender;
        this.school_year = school_year;
        this.major = major;
        this.subject = subject;
        this.status = status;
        this.photo_path = photo_path;
    }

    // Getters (nếu cần)
    public String getStudent_id() { return student_id; }
    public String getFirst_name() { return first_name; }
    public String getLast_name() { return last_name; }
    public String getBirth_date() { return birth_date; }
    public String getGender() { return gender; }
    public String getSchool_year() { return school_year; }
    public String getMajor() { return major; }
    public String getSubject() { return subject; }
    public String getStatus() { return status; }
    public String getPhoto_path() { return photo_path; }
}