package uk.ac.gre.aa5119a.timelearn.web;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import uk.ac.gre.aa5119a.timelearn.model.listing.TeacherListing;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.web.request.TeacherListingRequest;

public interface TimeShareApi {

    @POST("login")
    Call<LoginResponse> login(@Body User user);

    @POST("register")
    Call<RegisterResponse> register(@Body User user);

    @POST("teach/listing")
    Call<TeacherListingResponse> addListing(@Body TeacherListingRequest teacherListingRequest);
}
