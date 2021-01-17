package uk.ac.gre.aa5119a.timelearn.web;

import uk.ac.gre.aa5119a.timelearn.model.listing.TeacherListing;

public class TeacherListingResponse {

    private TeacherListing listing;
    private String message;

    public TeacherListingResponse() {

    }

    public TeacherListing getListing() {
        return listing;
    }

    public String getMessage() {
        return message;
    }
}
