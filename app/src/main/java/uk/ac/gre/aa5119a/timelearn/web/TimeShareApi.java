package uk.ac.gre.aa5119a.timelearn.web;


import androidx.lifecycle.LiveData;

import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import uk.ac.gre.aa5119a.timelearn.model.LessonDTO;
import uk.ac.gre.aa5119a.timelearn.model.listing.TeacherListing;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.model.notification.Notification;
import uk.ac.gre.aa5119a.timelearn.model.notification.NotificationClassBooking;
import uk.ac.gre.aa5119a.timelearn.web.request.ClassBookingRequest;
import uk.ac.gre.aa5119a.timelearn.web.request.TeacherListingRequest;
import uk.ac.gre.aa5119a.timelearn.web.response.LessonTimeUpdateResponse;
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
    Call<Void> setClassBookingAccepted(@Path("classBookingId") int classBookingId, @Path("isAccepted") boolean isAccepted);

    @DELETE("notifications/{classBookingId}")
    Call<Void> deleteNotification(@Path("classBookingId") int classBookingId);

    @GET("account/lessonCount")
    Call<Integer> getUserLessonCount(@Query("userId") int userId);

    @GET("account/teachingLessonCount")
    Call<Integer> getUserTeachingLessonCount(@Query("userId") int userId);

    @GET("lessons/{userId}")
    Call<List<LessonDTO>> getUserLessons(@Path("userId") int userId);

    @GET("lessons/teach/{userId}")
    Call<List<LessonDTO>> getUserTeachingLessons(@Path("userId") int userId);


    @GET("lesson/{lessonId}")
    Call<LessonDTO> getLesson(@Path("lessonId")int lessonId);

//    @PUT("lesson/update")
//    Call<LessonDTO> updateLesson(@Body LessonDTO lesson);

    @PUT("lesson/updateTimeLeft")
    Call<Boolean> updateTimeLeft(@Query("lessonId") int lessonId, @Query("timeLeft") long timeLeft);

    @PUT("lesson/{lessonId}/startClass")
    Call<Boolean> startClass(@Path("lessonId") int lessonId);

    @PUT("lesson/{lessonId}/joinLesson")
    Call<Boolean> joinLesson(@Path("lessonId") int lessonId);

    @PUT("lesson/runTimer")
    Call<LessonTimeUpdateResponse> runTimer(@Query("lessonId") int lessonId,@Query("timeStarted") long timeStarted);

//    @GET("lesson/timerRemaining")
//    Call<Long> getLessonTimeRemaining(@Query("lessonId")int lessonId);

    @GET("lesson/timerRemaining")
    Call<LessonTimeUpdateResponse> getLessonTimeRemaining(@Query("lessonId")int lessonId);

    @POST("user/rate")
    Call<Boolean> rateUser(@Query("userId") int userId, @Query("rating") float rating, @Query("comments") String comments);

    @POST("lesson/startTimer")
    Call<Boolean> startTimer(@Query("lessonId")int lessonId);

    @PUT("lesson/end")
    Call<Boolean> endLesson(@Query("lessonId") int lessonId, @Query("userId") int userId);

    @PUT("lesson/quit")
    Call<Boolean> quitLesson(@Query("lessonId") int lessonId, @Query("userId") int userId);


    @GET("user")
    Call<User> getUser(@Query("userId") int userId);
}