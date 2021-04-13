package uk.ac.gre.aa5119a.timelearn.model;


import java.util.Date;

public class LessonDTO {

    private int id;
    private int classListingId;
    private int studentId;
    private String studentFirstName;
    private String teacherFirstName;
    private int hours;
    private double lessonPrice;

    private Date lessonDate;
    private boolean isAttended;

    private String studentImage;
    private String teacherImage;
    private double teacherRating;

    private String subjectTitle;
    private String subjectIconUrl;

    private Boolean teacherHasJoined;
    private Boolean studentHasJoined;
    private String status;
    private long timeLeft;

    private Boolean studentHasLeft;
    private Boolean teacherHasLeft;

    private long timeStarted;


    public long getTimeStarted() {
        return timeStarted;
    }

    public void setTimeStarted(long timeStarted) {
        this.timeStarted = timeStarted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassListingId() {
        return classListingId;
    }

    public void setClassListingId(int classListingId) {
        this.classListingId = classListingId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }


    public Date getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(Date lessonDate) {
        this.lessonDate = lessonDate;
    }

    public boolean isAttended() {
        return isAttended;
    }

    public void setAttended(boolean attended) {
        isAttended = attended;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getTeacherFirstName() {
        return teacherFirstName;
    }

    public void setTeacherFirstName(String teacherFirstName) {
        this.teacherFirstName = teacherFirstName;
    }

    public double getLessonPrice() {
        return lessonPrice;
    }

    public void setLessonPrice(double lessonPrice) {
        this.lessonPrice = lessonPrice;
    }

    public String getStudentImage() {
        return studentImage;
    }

    public void setStudentImage(String studentImage) {
        this.studentImage = studentImage;
    }

    public String getTeacherImage() {
        return teacherImage;
    }

    public void setTeacherImage(String teacherImage) {
        this.teacherImage = teacherImage;
    }

    public double getTeacherRating() {
        return teacherRating;
    }

    public void setTeacherRating(double teacherRating) {
        this.teacherRating = teacherRating;
    }


    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getSubjectIconUrl() {
        return subjectIconUrl;
    }

    public void setSubjectIconUrl(String subjectIconUrl) {
        this.subjectIconUrl = subjectIconUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Boolean getTeacherHasJoined() {
        return teacherHasJoined;
    }

    public void setTeacherHasJoined(Boolean teacherHasJoined) {
        this.teacherHasJoined = teacherHasJoined;
    }

    public Boolean getStudentHasJoined() {
        return studentHasJoined;
    }

    public void setStudentHasJoined(Boolean studentHasJoined) {
        this.studentHasJoined = studentHasJoined;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public Boolean getStudentHasLeft() {
        return studentHasLeft;
    }

    public void setStudentHasLeft(Boolean studentHasLeft) {
        this.studentHasLeft = studentHasLeft;
    }

    public Boolean getTeacherHasLeft() {
        return teacherHasLeft;
    }

    public void setTeacherHasLeft(Boolean teacherHasLeft) {
        this.teacherHasLeft = teacherHasLeft;
    }

    @Override
    public String toString() {
        return "LessonDTO{" +
                "id=" + id +
                ", classListingId=" + classListingId +
                ", studentId=" + studentId +
                ", studentFirstName='" + studentFirstName + '\'' +
                ", teacherFirstName='" + teacherFirstName + '\'' +
                ", hours=" + hours +
                ", lessonPrice=" + lessonPrice +
                ", lessonDate=" + lessonDate +
                ", isAttended=" + isAttended +
                ", studentImage='" + studentImage + '\'' +
                ", teacherImage='" + teacherImage + '\'' +
                ", teacherRating=" + teacherRating +
                ", subjectTitle='" + subjectTitle + '\'' +
                ", subjectIconUrl='" + subjectIconUrl + '\'' +
                ", teacherHasJoined=" + teacherHasJoined +
                ", studentHasJoined=" + studentHasJoined +
                ", status='" + status + '\'' +
                ", timeLeft=" + timeLeft +
                ", studentHasLeft=" + studentHasLeft +
                ", teacherHasLeft=" + teacherHasLeft +
                '}';
    }
}
