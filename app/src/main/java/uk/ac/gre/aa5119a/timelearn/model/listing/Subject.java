package uk.ac.gre.aa5119a.timelearn.model.listing;

public class Subject {

    private int subjectId;
    private int icon;
    private String title;


    public Subject(int subjectId, int icon, String title) {
        this.subjectId = subjectId;
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
