package model;

public class CreditData {
    private final String studentID;
    private final String fullName;
    private final int creditsAccumulated;

    public CreditData(String studentID, String fullName, int creditsAccumulated) {
        this.studentID = studentID;
        this.fullName = fullName;
        this.creditsAccumulated = creditsAccumulated;
    }

    public String getStudentID() { return studentID; }
    public String getFullName() { return fullName; }
    public int getCreditsAccumulated() { return creditsAccumulated; }
}