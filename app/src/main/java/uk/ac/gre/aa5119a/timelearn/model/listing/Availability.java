package uk.ac.gre.aa5119a.timelearn.model.listing;

public class Availability {
    private int id;
    private String days;

    public Availability(String days) {
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
