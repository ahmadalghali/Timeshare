package uk.ac.gre.aa5119a.timelearn.fragment.classroom;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.dialog.LoadingDialog;
import uk.ac.gre.aa5119a.timelearn.model.LessonDTO;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.LessonViewModel;
import uk.ac.gre.aa5119a.timelearn.web.response.LessonTimeUpdateResponse;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.userViewModel;

public class ClassroomFragment extends Fragment {

    View view;

    ImageView btnExit;
    TextView tvPutAwayPhone;
    TextView tvClassMode;
    TextView tvTimeRemaining;
    TextView tvTeacherName;
    TextView tvStudentName;
    Button btnStart;

    ImageView ivTeacherPhoto;
    ImageView ivStudentPhoto;

    private long START_TIME_IN_MILLIS;
    private boolean timerIsRunning;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    boolean stopClassRunnable = false;

    boolean stopTimerRunnable = false;


    LessonViewModel lessonViewModel;

    final Handler handler = new Handler();

    LessonDTO lesson;

    User user;

    boolean isStudent;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_classroom, null);


        assignGlobalVariables();
        setListeners();

        getLesson();

//        refreshSessionData();


        return view;
    }

    private void assignGlobalVariables() {
        bottomNavigation.setVisibility(View.GONE);

        btnExit = view.findViewById(R.id.btnExit);
        tvPutAwayPhone = view.findViewById(R.id.tvPutAwayPhone);
        tvClassMode = view.findViewById(R.id.tvClassMode);
        tvTimeRemaining = view.findViewById(R.id.tvTimeRemaining);
        tvTeacherName = view.findViewById(R.id.tvTeacherName);
        tvStudentName = view.findViewById(R.id.tvStudentName);
        btnStart = view.findViewById(R.id.btnStart);
        ivTeacherPhoto = view.findViewById(R.id.ivTeacherPhoto);
        ivStudentPhoto = view.findViewById(R.id.ivStudentPhoto);


        lessonViewModel = new ViewModelProvider(getActivity()).get(LessonViewModel.class);

        user = userViewModel.getUser().getValue();


//        getLesson();

//        timeShareApi.getLesson(lessonViewModel.getLessonId().getValue()).enqueue(new Callback<LessonDTO>() {
//            @Override
//            public void onResponse(Call<LessonDTO> call, Response<LessonDTO> response) {
//                loadingDialog.dismissDialog();
//                if(response.isSuccessful()){
//                    lesson = response.body();
//                    setLessonDetails();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<LessonDTO> call, Throwable t) {
//                loadingDialog.dismissDialog();
//
//            }
//        });


    }

    private void setListeners() {
        btnExit.setOnClickListener(v -> {

            showExitDialog();


        });

        btnStart.setOnClickListener(v -> {
//
//            if (timerIsRunning) {
//
//                pauseTimer();
//
//            } else {
            startTimer();
//                runTimer();
//            }


//            if (timerIsRunning) {
//                // start a global timer .. send a http request
//                pauseTimer();
//            } else {
//                startTimer();
//            }
        });
    }

    private void setClassroomDetails() {

        isStudent = lesson.getStudentId() == user.getId();

//
//        if (isStudent) {
//            updateTimeRemaining();
////            timerRunnable.run();
////            btnStart.setVisibility(View.VISIBLE);
//        }

        Picasso.get()
                .load(lesson.getTeacherImage())
                .placeholder(R.drawable.ic_account)
                .into(ivTeacherPhoto);

        ivTeacherPhoto.setVisibility(View.VISIBLE);
        tvTeacherName.setText(lesson.getTeacherFirstName());

        if (lesson.getStudentHasJoined()) {
//            tvClassMode.setVisibility(View.VISIBLE);

            tvClassMode.setText("");

            Picasso.get()
                    .load(lesson.getStudentImage())
                    .placeholder(R.drawable.ic_account)
                    .into(ivStudentPhoto);

            ivStudentPhoto.setVisibility(View.VISIBLE);
            tvStudentName.setText(lesson.getStudentFirstName());


            if (!isStudent && lesson.getTimeStarted() == -1) {
                btnStart.setVisibility(View.VISIBLE);
            }

//            stopClassRunnable();

//            handler.removeCallbacks(classRunnable);
//            stopClassRunnable();

//            Toast.makeText(getContext(), "Removed classRunnable", Toast.LENGTH_SHORT).show();
        } else {
//            tvClassMode.setVisibility(View.VISIBLE);
//            tvClassMode.setText("Waiting for " + lesson.getStudentFirstName() + " to join...");

            Picasso.get()
                    .load(R.drawable.ic_account)
                    .into(ivStudentPhoto);
            tvStudentName.setText("");

            tvClassMode.setText("Waiting for " + lesson.getStudentFirstName() + " to join...");
            tvClassMode.setVisibility(View.VISIBLE);

        }
//        if (userViewModel.getUser().getValue().getId() == lesson.getStudentId()) {
//            btnStart.setVisibility(View.GONE);
//        }

//        START_TIME_IN_MILLIS = 3600000 * lesson.getHours();
//        timeLeftInMillis = START_TIME_IN_MILLIS;

//        lesson.setTimeLeft(timeLeftInMillis);

//        START_TIME_IN_MILLIS = lesson.getTimeLeft();

//        timeLeftInMillis = START_TIME_IN_MILLIS;
//        lesson.setTimeLeft(timeLeftInMillis);
        updateCountDownText();

    }

    private void getLesson() {
        LoadingDialog loadingDialog = new LoadingDialog(getActivity(), false);
        loadingDialog.setMessage("Initializing session...");
        loadingDialog.startLoadingDialog();

        timeShareApi.getLesson(lessonViewModel.getLessonId().getValue()).enqueue(new Callback<LessonDTO>() {
            @Override
            public void onResponse(Call<LessonDTO> call, Response<LessonDTO> response) {
                loadingDialog.dismissDialog();
                if (response.isSuccessful()) {

                    lesson = response.body();



                    lessonViewModel.setLesson(lesson);


                    setClassroomDetails();



                    refreshSessionData();


//                    setClassroomDetails();

                    //after this step - keep checking for student  to join
//                    refreshSessionData();

                } else {
                    Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LessonDTO> call, Throwable t) {
                loadingDialog.dismissDialog();
                Toast.makeText(getContext(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void updateLesson() {

        timeShareApi.getLesson(lessonViewModel.getLessonId().getValue()).enqueue(new Callback<LessonDTO>() {
            @Override
            public void onResponse(Call<LessonDTO> call, Response<LessonDTO> response) {
                if (response.isSuccessful()) {

                    lesson = response.body();

                    lessonViewModel.setLesson(lesson);

                    setClassroomDetails();

//                    setClassroomDetails();

                    //after this step - keep checking for student  to join
//                    refreshSessionData();

                } else {
                    Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LessonDTO> call, Throwable t) {
                Toast.makeText(getContext(), "failed " + t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }

    private void refreshSessionData() {
            classRunnable.run();
    }

    private Runnable classRunnable = new Runnable() {

        @Override
        public void run() {

            if (stopClassRunnable) {
                return;
            }

            updateLesson();

//            setClassroomDetails();
//            timeShareApi.getLesson(lesson.getId()).enqueue(new Callback<LessonDTO>() {
//                @Override
//                public void onResponse(Call<LessonDTO> call, Response<LessonDTO> response) {
//                    if (response.isSuccessful()) {
//
//                        lesson = response.body();
//
//                        Toast.makeText(getContext(), "Still running classRunnable", Toast.LENGTH_SHORT).show();
//                        System.out.println("Still running classRunnable");
////                        setLessonDetails();
////                        updateLessonDetails();
//
//                        //after this step - keep checking for student  to join
////                        refreshSessionData();
//
//                    } else {
//                        Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<LessonDTO> call, Throwable t) {
//
//                }
//            });
//            updateLessonDetails();
//            runTimer();

            handler.postDelayed(this, 1000);
        }
    };


    private void startTimer() {

        timeShareApi.startTimer(lesson.getId()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {

                        btnStart.setVisibility(View.INVISIBLE);
                        refreshTimer();
//                        refreshTimer();
//                       if(lesson.getStudentId() == user.getId()){
//                           refreshTimer();
//                       }
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });


//        timerRunnable.run();
//        startRepeating();

//        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                timeLeftInMillis = millisUntilFinished;
//                updateCountDownText();
//            }
//
//            @Override
//            public void onFinish() {
//                timerIsRunning = false;
//                btnStart.setVisibility(View.GONE);
//
//                NavController navController = navHostFragment.getNavController();
//                NavDirections action = ClassroomFragmentDirections.actionClassroomFragmentToRateUserFragment();
//                navController.navigate(action);
//
//            }
//        }.start();
//
//        timerIsRunning = true;
//        btnStart.setText("Take a break");
//        tvPutAwayPhone.setVisibility(View.VISIBLE);
//        tvClassMode.setVisibility(View.VISIBLE);
//        tvClassMode.setText("Class in progress");

    }

    private void refreshTimer() {
        timerRunnable.run();
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {

            if (stopTimerRunnable) {
                return;
            }

            timeShareApi.getLessonTimeRemaining(lesson.getId()).enqueue(new Callback<LessonTimeUpdateResponse>() {
                @Override
                public void onResponse(Call<LessonTimeUpdateResponse> call, Response<LessonTimeUpdateResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getHasTimeLeft()) {

                            lesson.setTimeLeft(response.body().getTimeLeft());

//                            updateCountDownText();
                        } else {
                            stopTimerRunnable();

                            Toast.makeText(getContext(), "Lesson ended", Toast.LENGTH_SHORT).show();
                            navigateToRatingPage();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LessonTimeUpdateResponse> call, Throwable t) {

                }
            });
            handler.postDelayed(this, 1000);
        }
    };

//    private Runnable timerRunnable = new Runnable() {
//        @Override
//        public void run() {
//
//
//            runTimer();
//
//            handler.postDelayed(this, 1000);
//        }
//    };


    private void runTimer() {
        timeShareApi.runTimer(lesson.getId(), System.currentTimeMillis()).enqueue(new Callback<LessonTimeUpdateResponse>() {
            @Override
            public void onResponse(Call<LessonTimeUpdateResponse> call, Response<LessonTimeUpdateResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getHasTimeLeft()) {


                        btnStart.setVisibility(View.INVISIBLE);

                        tvPutAwayPhone.setVisibility(View.VISIBLE);
                        tvClassMode.setVisibility(View.VISIBLE);
                        tvClassMode.setText("Class in progress");

                        lesson.setTimeLeft(response.body().getTimeLeft());

                        updateCountDownText();
                    } else {
                        // end the lesson

//                        timerIsRunning = false;
                        btnStart.setVisibility(View.GONE);

                        NavController navController = navHostFragment.getNavController();
                        NavDirections action = ClassroomFragmentDirections.actionClassroomFragmentToRateUserFragment();

                        if (navController.getCurrentDestination().getId() == R.id.classroomFragment) {
                            navController.navigate(action);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LessonTimeUpdateResponse> call, Throwable t) {

            }
        });
    }

    private void stopClassRunnable() {
        stopClassRunnable = true;
    }

    private void stopTimerRunnable() {
        stopTimerRunnable = true;
    }

    private void navigateToRatingPage() {
        Toast.makeText(getContext(), "Navigating to rate user page", Toast.LENGTH_SHORT).show();

        NavController navController = navHostFragment.getNavController();
        NavDirections action = ClassroomFragmentDirections.actionClassroomFragmentToRateUserFragment();

        if (navController.getCurrentDestination().getId() == R.id.classroomFragment) {
            Toast.makeText(getContext(), "Navigated", Toast.LENGTH_SHORT).show();

            navController.navigate(action);
        }
    }

    private void updateCountDownText() {

//        if(lesson.getTimeLeft() < 1000){
//            navigateToRatingPage();
//        }
//        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeLeftInMillis),
//                TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeftInMillis)),
//                TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis)));
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(lesson.getTimeLeft()),
                TimeUnit.MILLISECONDS.toMinutes(lesson.getTimeLeft()) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(lesson.getTimeLeft())),
                TimeUnit.MILLISECONDS.toSeconds(lesson.getTimeLeft()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(lesson.getTimeLeft())));
        tvTimeRemaining.setText(timeLeftFormatted);
    }

//    private void updateTimeRemaining() {
//        timeShareApi.getLessonTimeRemaining(lesson.getId()).enqueue(new Callback<Long>() {
//            @Override
//            public void onResponse(Call<Long> call, Response<Long> response) {
//                if(response.isSuccessful()){
//                    long timeRemaining = response.body();
//                    timeLeftInMillis = timeRemaining;
//
//
//
//                    String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeLeftInMillis),
//                            TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeftInMillis)),
//                            TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis)));
//                    tvTimeRemaining.setText(timeLeftFormatted);
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Long> call, Throwable t) {
//
//            }
//        });
//    }

    private void showExitDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Are you sure you want to quit this lesson?");
        alertDialog.setMessage("Note: Your TimeCredits will not be refunded if you quit.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Leave",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        endLesson(lesson.getId(), user.getId());
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void endLesson(int lessonId, int userId) {
        navigateToRatingPage();
    }
}
