package uk.ac.gre.aa5119a.timelearn.model.listing;

import java.util.List;

import uk.ac.gre.aa5119a.timelearn.model.User;

public class TeacherListing {

    private int id;
    private Subject subject;
    private int userId;

    private String title;
    private String description;
    private String imageId;
    private List<String> teachingStyles;
    private List<String> daysAvailable;

    public TeacherListing(Subject subject, int userId, String title, String description, String imageId, List<String> teachingStyles, List<String> daysAvailable) {
        this.subject = subject;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getImage() {
        return imageId;
    }

    public void setImage(int image) {
        this.imageId = imageId;
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


    @Override
    public String toString() {
        return "TeacherListing{" +
                "id=" + id +
                ", subject=" + subject +

                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageId=" + imageId +
                ", teachingStyles=" + teachingStyles +
                ", daysAvailable=" + daysAvailable +
                '}';
    }
}
