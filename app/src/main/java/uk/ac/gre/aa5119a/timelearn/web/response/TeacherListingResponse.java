package uk.ac.gre.aa5119a.timelearn.web.response;

import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.model.listing.Subject;
import uk.ac.gre.aa5119a.timelearn.model.listing.TeacherListing;

public class TeacherListingResponse {

    private TeacherListing teacherListing;
    private String message;
    private User user;
    private Subject subject;


    public TeacherListingResponse() {

    }

    public User getUser() {
        return user;
    }

    public TeacherListing getListing() {
        return teacherListing;
    }

    public String getMessage() {
        return message;
    }

    public Subject getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return "TeacherListingResponse{" +
                "teacherListing=" + teacherListing.toString() +
                ", message='" + message + '\'' +
                ", user=" + user.toString() +
                '}';
    }
}
