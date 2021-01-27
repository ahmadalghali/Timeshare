package uk.ac.gre.aa5119a.timelearn.model.notification;

import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.model.learn.ClassBooking;

public class NotificationClassBooking {

    private String subjectTitle;
    private int hours;
    private double timeCreditPrice;
    private String studentName;
    private String studentProfileImage;
    private String subjectIconUrl;

    private ClassBooking classBooking;

    public NotificationClassBooking(String subjectTitle, int hours, double timeCreditPrice, String studentName, String studentProfileImage, String subjectIconUrl, ClassBooking classBooking) {
        this.subjectTitle = subjectTitle;
        this.hours = hours;
        this.timeCreditPrice = timeCreditPrice;
        this.studentName = studentName;
        this.studentProfileImage = studentProfileImage;
        this.subjectIconUrl = subjectIconUrl;
        this.classBooking = classBooking;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public double getTimeCreditPrice() {
        return timeCreditPrice;
    }

    public void setTimeCreditPrice(double timeCreditPrice) {
        this.timeCreditPrice = timeCreditPrice;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentProfileImage() {
        return studentProfileImage;
    }

    public void setStudentProfileImage(String studentProfileImage) {
        this.studentProfileImage = studentProfileImage;
    }

    public String getSubjectIconUrl() {
        return subjectIconUrl;
    }

    public void setSubjectIconUrl(String subjectIconUrl) {
        this.subjectIconUrl = subjectIconUrl;
    }

    public ClassBooking getClassBooking() {
        return classBooking;
    }

    public void setClassBooking(ClassBooking classBooking) {
        this.classBooking = classBooking;
    }
}
