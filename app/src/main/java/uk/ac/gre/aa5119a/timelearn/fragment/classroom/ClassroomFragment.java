package uk.ac.gre.aa5119a.timelearn.fragment.classroom;

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
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.fragment.account.AccountFragmentDirections;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;

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

    private long START_TIME_IN_MILLIS = 15000;
    private boolean timerIsRunning;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;




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

            NavController navController = navHostFragment.getNavController();
            NavDirections action = ClassroomFragmentDirections.actionClassroomFragmentToRateUserFragment();
            navController.navigate(action);

        });

        btnStart.setOnClickListener(v -> {
            if(timerIsRunning){
                pauseTimer();
            }else{
                startTimer();
            }
        });
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

        timeLeftInMillis = START_TIME_IN_MILLIS;

    }


}
