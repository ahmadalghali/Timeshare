package uk.ac.gre.aa5119a.timelearn.fragment.classroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.model.LessonDTO;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.LessonViewModel;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.userViewModel;

public class RateUserFragment extends Fragment {

    View view;
    ImageView btnExit;

    TextView tvHowMuchWouldYouRate;
    ImageView ivUserPhoto;

    TextView tvUserName;

    RatingBar rbRating;

    EditText etComments;

    Button btnSubmit;

    LessonViewModel lessonViewModel;

    LessonDTO lesson;

    User user;

    boolean isStudent;

    int userIdToRate;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rate_user, null);

        bottomNavigation.setVisibility(View.GONE);

        assignGlobalVariables();
        setDetails();
        setListeners();

        return view;
    }

    private void setDetails() {
         isStudent = lesson.getStudentId() == user.getId();

        if (isStudent) {
            tvHowMuchWouldYouRate.setText("How much would you rate " + lesson.getTeacherFirstName() + "?");
            Picasso.get().load(lesson.getTeacherImage()).into(ivUserPhoto);
            tvUserName.setText(lesson.getTeacherFirstName());
            userIdToRate = lesson.getTeacherId();

        } else {
            tvHowMuchWouldYouRate.setText("How much would you rate " + lesson.getStudentFirstName() + "?");
            Picasso.get().load(lesson.getStudentImage()).into(ivUserPhoto);
            tvUserName.setText(lesson.getStudentFirstName());
            userIdToRate = lesson.getStudentId();

        }
    }

    private void setListeners() {
        btnExit.setOnClickListener(v -> {
            navigateToHome();
        });

        btnSubmit.setOnClickListener(v -> {
            rateUser();
            navigateToHome();
        });
    }

    private void rateUser() {
        float rating = rbRating.getRating();
        String comments = etComments.getText().toString();

//        Toast.makeText(getContext(),"rating: " +  rating + "comments: " + comments + "user: " + user.getId(), Toast.LENGTH_SHORT).show();
        timeShareApi.rateUser(userIdToRate, rating, comments).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()){
                    if(response.body()){
//                        Toast.makeText(getContext(),"Thank you for your feedback", Toast.LENGTH_LONG).show();
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Thank you for the feedback!", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();

                    }else {
                        Toast.makeText(getContext(),"Feedback didnt go through", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(),"Failed " + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void navigateToHome() {
        NavController navController = navHostFragment.getNavController();
        NavDirections action = RateUserFragmentDirections.actionRateUserFragmentToAccountFragment();
        navController.navigate(action);
    }

    private void assignGlobalVariables() {
        btnExit = view.findViewById(R.id.btnExit);
        tvHowMuchWouldYouRate = view.findViewById(R.id.tvHowMuchWouldYouRate);
        ivUserPhoto = view.findViewById(R.id.ivUserPhoto);
        tvUserName = view.findViewById(R.id.tvUserName);
        rbRating = view.findViewById(R.id.rbRating);
        etComments = view.findViewById(R.id.etComments);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        lessonViewModel = new ViewModelProvider(getActivity()).get(LessonViewModel.class);

        lesson = lessonViewModel.getLesson().getValue();

        user = userViewModel.getUser().getValue();




    }


}
