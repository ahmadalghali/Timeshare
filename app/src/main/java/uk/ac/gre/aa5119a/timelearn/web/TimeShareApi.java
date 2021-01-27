package uk.ac.gre.aa5119a.timelearn.web;


import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import uk.ac.gre.aa5119a.timelearn.model.listing.TeacherListing;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.model.notification.Notification;
import uk.ac.gre.aa5119a.timelearn.model.notification.NotificationClassBooking;
import uk.ac.gre.aa5119a.timelearn.web.request.ClassBookingRequest;
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
    Call<User> getUserById(@Path("userId") int userId);

    @GET("class/{classId}")
    Call<TeacherListing> getClassById(@Path("classId") int classId);

    @POST("book/class")
    Call<Boolean> bookClass(@Body ClassBookingRequest classBookingRequest);

    @GET("notifications/{userId}")
    Call<List<Notification>> getUserNotifications(@Path("userId") int userId);

    @GET("class/{classId}/isRequestedBy/user/{studentId}")
    Call<Boolean> isClassRequestedByUser(@Path("classId") int classId, @Path("studentId") int studentId);

    @PUT("class_booking/{classBookingId}/isAccepted={isAccepted}")
    Call<Boolean> setClassBookingAccepted(@Path("classBookingId") int classBookingId, @Path("isAccepted") boolean isAccepted);

}