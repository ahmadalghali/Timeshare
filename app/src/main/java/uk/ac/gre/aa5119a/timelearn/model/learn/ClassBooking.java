package uk.ac.gre.aa5119a.timelearn.model.learn;

public class ClassBooking {

    private int id;
    private int classId;
    private int studentId;
    private boolean isAccepted;


    public ClassBooking(int classId, int studentId, boolean isAccepted) {
        this.classId = classId;
        this.studentId = studentId;
        this.isAccepted = isAccepted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
