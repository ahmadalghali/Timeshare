package uk.ac.gre.aa5119a.timelearn.model.listing;

import java.util.List;

public class TeacherListing {

    private int id;
    private int subjectId;
    private int userId;
    private String title;
    private String description;
    private String imageId;
    private double timeRate;
    private List<Integer> teachingStyleIds;
    private List<Integer> availabilityIds;

    public TeacherListing(int subjectId, int userId, String title, String description, String imageId, double timeRate, List<Integer> teachingStyleIds, List<Integer> availabilityIds) {
        this.subjectId = subjectId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.imageId = imageId;
        this.timeRate = timeRate;
        this.teachingStyleIds = teachingStyleIds;
        this.availabilityIds = availabilityIds;
    }

    public double getTimeRate() {
        return timeRate;
    }

    public void setTimeRate(double timeRate) {
        this.timeRate = timeRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
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


    public List<Integer> getTeachingStyleIds() {
        return teachingStyleIds;
    }

    public void setTeachingStyleIds(List<Integer> teachingStyleIds) {
        this.teachingStyleIds = teachingStyleIds;
    }

    public List<Integer> getAvailabilityIds() {
        return availabilityIds;
    }

    public void setAvailabilityIds(List<Integer> availabilityIds) {
        this.availabilityIds = availabilityIds;
    }

    @Override
    public String toString() {
        return "TeacherListing{" +
                "id=" + id +
                ", subject=" + subjectId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageId='" + imageId + '\'' +
                ", timeRate=" + timeRate +
                ", teachingStyles=" + teachingStyleIds +
                ", daysAvailable=" + availabilityIds +
                ", user=" + userId +
                '}';
    }
}
