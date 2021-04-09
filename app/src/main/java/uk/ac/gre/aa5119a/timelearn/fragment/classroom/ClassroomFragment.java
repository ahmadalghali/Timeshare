package uk.ac.gre.aa5119a.timelearn.fragment.classroom;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.dialog.LoadingDialog;
import uk.ac.gre.aa5119a.timelearn.fragment.account.AccountFragmentDirections;
import uk.ac.gre.aa5119a.timelearn.model.LessonDTO;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;
import uk.ac.gre.aa5119a.timelearn.viewmodel.LessonViewModel;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;

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

    private long START_TIME_IN_MILLIS ;
    private boolean timerIsRunning;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;


    LessonViewModel lessonViewModel;


    LessonDTO lesson;




    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_classroom, null);

        assignGlobalVariables();
        setListeners();

        bottomNavigation.setVisibility(View.GONE);

        updateCountDownText();
        return view;
    }

    private void setListeners() {
        btnExit.setOnClickListener(v -> {

            showExitDialog();


        });

        btnStart.setOnClickListener(v -> {
            if(timerIsRunning){
                pauseTimer();
            }else{
                startTimer();
            }
        });
    }

    private void showExitDialog() {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Are you sure you want to quit this lesson?");
            alertDialog.setMessage("Note: Your TimeCredits will not be refunded if you quit.");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Leave",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            endLesson(lesson);
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

    private void endLesson(LessonDTO lessonId) {


        navigateToRatingPage();
    }

    private void navigateToRatingPage() {
        NavController navController = navHostFragment.getNavController();
        NavDirections action = ClassroomFragmentDirections.actionClassroomFragmentToRateUserFragment();
        navController.navigate(action);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerIsRunning = false;
                btnStart.setVisibility(View.GONE);

                NavController navController = navHostFragment.getNavController();
                NavDirections action = ClassroomFragmentDirections.actionClassroomFragmentToRateUserFragment();
                navController.navigate(action);

            }
        }.start();

        timerIsRunning = true;
        btnStart.setText("Take a break");
        tvPutAwayPhone.setVisibility(View.VISIBLE);
        tvClassMode.setVisibility(View.VISIBLE);
        tvClassMode.setText("Class in progress");

    }

    private void updateCountDownText() {
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeLeftInMillis),
                TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeftInMillis)),
                TimeUnit.MILLISECONDS.toSeconds(timeLeftInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeftInMillis)));

        tvTimeRemaining.setText(timeLeftFormatted);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        timerIsRunning = false;
        btnStart.setText("Resume");
        tvPutAwayPhone.setVisibility(View.INVISIBLE);
        tvClassMode.setText("Break");

    }

    private void assignGlobalVariables() {

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


        LoadingDialog loadingDialog = new LoadingDialog(getActivity(), true);
        loadingDialog.setMessage("Initializing session...");
        loadingDialog.startLoadingDialog();
        timeShareApi.getLesson(lessonViewModel.getLessonId().getValue()).enqueue(new Callback<LessonDTO>() {
            @Override
            public void onResponse(Call<LessonDTO> call, Response<LessonDTO> response) {
                loadingDialog.dismissDialog();
                if(response.isSuccessful()){
                    lesson = response.body();
                    setLessonDetails();
                }

            }

            @Override
            public void onFailure(Call<LessonDTO> call, Throwable t) {
                loadingDialog.dismissDialog();

            }
        });




    }

    private void setLessonDetails() {
        Picasso.get()
                .load(lesson.getStudentImage())
                .placeholder(R.drawable.ic_account)
                .into(ivStudentPhoto);

        ivStudentPhoto.setVisibility(View.VISIBLE);

        Picasso.get()
                .load(lesson.getTeacherImage())
                .placeholder(R.drawable.ic_account)
                .into(ivTeacherPhoto);

        ivTeacherPhoto.setVisibility(View.VISIBLE);

        tvStudentName.setText(lesson.getStudentFirstName());
        tvTeacherName.setText(lesson.getTeacherFirstName());

        START_TIME_IN_MILLIS = 3600000 * lesson.getHours();

        timeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();



    }


}
