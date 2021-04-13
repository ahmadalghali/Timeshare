package uk.ac.gre.aa5119a.timelearn.web.response;

public class LessonTimeUpdateResponse {

    private Boolean hasTimeLeft;
    private long timeLeft;
    private Boolean hasStarted;

    public Boolean getHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(Boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public Boolean getHasTimeLeft() {
        return hasTimeLeft;
    }

    public void setHasTimeLeft(Boolean hasTimeLeft) {
        this.hasTimeLeft = hasTimeLeft;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }
}
