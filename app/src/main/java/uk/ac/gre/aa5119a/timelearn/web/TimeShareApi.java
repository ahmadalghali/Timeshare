package uk.ac.gre.aa5119a.timelearn.web;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import uk.ac.gre.aa5119a.timelearn.model.listing.TeacherListing;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.web.request.TeacherListingRequest;
import uk.ac.gre.aa5119a.timelearn.web.response.LoginResponse;
import uk.ac.gre.aa5119a.timelearn.web.response.RegisterResponse;
import uk.ac.gre.aa5119a.timelearn.web.response.TeacherListingResponse;

public interface TimeShareApi {

    @POST("login")
    Call<LoginResponse> login(@Body User user);

    @POST("register")
    Call<RegisterResponse> register(@Body User user);

    @POST("teach/listing")
    Call<TeacherListingResponse> addListing(@Body TeacherListingRequest teacherListingRequest);

    @GET("learn/subject/{subjectId}")
    Call<List<TeacherListingResponse>> getClassesBySubject(@Path("subjectId") int subjectId);

    @GET("user/{userId}")
    Call<User> getUserById(@Path("userId")int userId);
}
