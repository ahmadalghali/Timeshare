package uk.ac.gre.aa5119a.timelearn.model.listing;

public class TeachingStyle {
    private int id;
    private String type;

    public TeachingStyle(String type) {
        this.type = type;
    }

    protected TeachingStyle() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
