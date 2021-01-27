package uk.ac.gre.aa5119a.timelearn.web.request;

public class ClassBookingRequest {
    private int studentId;
    private int classId;
    private boolean isAccepted;
    private int hours;
    private long classDate;

    public ClassBookingRequest(int studentId, int classId, boolean isAccepted, int hours, long classDate) {
        this.studentId = studentId;
        this.classId = classId;
        this.isAccepted = isAccepted;
        this.hours = hours;
        this.classDate = classDate;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public long getClassDate() {
        return classDate;
    }

    public void setClassDate(long classDate) {
        this.classDate = classDate;
    }

    @Override
    public String toString() {
        return "ClassBookingRequest{" +
                "studentId=" + studentId +
                ", classId=" + classId +
                ", isAccepted=" + isAccepted +
                ", hours=" + hours +
                '}';
    }
}
