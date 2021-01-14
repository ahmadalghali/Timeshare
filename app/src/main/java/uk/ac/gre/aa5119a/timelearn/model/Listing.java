package uk.ac.gre.aa5119a.timelearn.model;

import java.util.List;

import uk.ac.gre.aa5119a.timelearn.model.ui.Subject;

public class Listing {

    private int id;
    private Subject subject;
    private User teacher;

    private String title;
    private String description;
    private int image;
    private List<String> teachingStyles;
    private List<String> daysAvailable;

    public Listing(Subject subject, User teacher, String title, String description, int image, List<String> teachingStyles, List<String> daysAvailable) {
        this.subject = subject;
        this.teacher = teacher;
        this.title = title;
        this.description = description;
        this.image = image;
        this.teachingStyles = teachingStyles;
        this.daysAvailable = daysAvailable;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public List<String> getTeachingStyles() {
        return teachingStyles;
    }

    public void setTeachingStyles(List<String> teachingStyles) {
        this.teachingStyles = teachingStyles;
    }

    public List<String> getDaysAvailable() {
        return daysAvailable;
    }

    public void setDaysAvailable(List<String> daysAvailable) {
        this.daysAvailable = daysAvailable;
    }
}
