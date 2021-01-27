package uk.ac.gre.aa5119a.timelearn.model.notification;

public class NotificationClassConfirmation {

    private String subjectTitle;
    private String teacherName;
    private String teacherProfileImageUrl;
    private String subjectIconName;
    private long classDate;
    private int classId;

    public NotificationClassConfirmation(String subjectTitle, String teacherName, String teacherProfileImageUrl, String subjectIconName, long classDate, int classId) {
        this.subjectTitle = subjectTitle;
        this.teacherName = teacherName;
        this.teacherProfileImageUrl = teacherProfileImageUrl;
        this.subjectIconName = subjectIconName;
        this.classDate = classDate;
        this.classId = classId;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherProfileImageUrl() {
        return teacherProfileImageUrl;
    }

    public void setTeacherProfileImageUrl(String teacherProfileImageUrl) {
        this.teacherProfileImageUrl = teacherProfileImageUrl;
    }

    public String getSubjectIconName() {
        return subjectIconName;
    }

    public void setSubjectIconName(String subjectIconName) {
        this.subjectIconName = subjectIconName;
    }

    public long getClassDate() {
        return classDate;
    }

    public void setClassDate(long classDate) {
        this.classDate = classDate;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }
}
