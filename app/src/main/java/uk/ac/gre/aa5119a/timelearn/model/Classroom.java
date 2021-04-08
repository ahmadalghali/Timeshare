package uk.ac.gre.aa5119a.timelearn.model;

public class Classroom {

    private User teacher;
    private User student;
    private int hours;
    private int classBookingId;

    public Classroom(User teacher, User student, int hours, int classBookingId) {
        this.teacher = teacher;
        this.student = student;
        this.hours = hours;
        this.classBookingId = classBookingId;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getClassBookingId() {
        return classBookingId;
    }

    public void setClassBookingId(int classBookingId) {
        this.classBookingId = classBookingId;
    }
}
