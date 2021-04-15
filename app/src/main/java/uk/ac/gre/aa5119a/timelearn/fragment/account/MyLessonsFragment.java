package uk.ac.gre.aa5119a.timelearn.fragment.account;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.adapter.MyLessonsAdapter;
import uk.ac.gre.aa5119a.timelearn.dialog.LoadingDialog;
import uk.ac.gre.aa5119a.timelearn.model.LessonDTO;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;
import uk.ac.gre.aa5119a.timelearn.viewmodel.LessonViewModel;
import uk.ac.gre.aa5119a.timelearn.viewmodel.UserViewModel;


import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.userViewModel;


public class MyLessonsFragment extends Fragment {


    List<LessonDTO> lessons = new ArrayList<>();
    RecyclerView rvLessons;
    View view;
    ImageView backBtn;
    SwipeRefreshLayout refreshLayout;

    MyLessonsAdapter adapter;

    LessonViewModel lessonViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_lessons, container, false);

        assignGlobalVariables();
        getUserLessons();

        return view;
    }


    private void assignGlobalVariables() {

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        lessonViewModel = new ViewModelProvider(getActivity()).get(LessonViewModel.class);

        refreshLayout = view.findViewById(R.id.refreshLayout);

        backBtn = view.findViewById(R.id.backBtn);
        rvLessons = view.findViewById(R.id.rvLessons);

        rvLessons.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyLessonsAdapter(getContext(), new MyLessonsAdapter.LessonClickListener() {
            @Override
            public void onJoinButtonClicked(int position, int lessonId) {
                showConfirmDialog(lessonId);
            }
        });
        rvLessons.setAdapter(adapter);

        backBtn.setOnClickListener(v -> {
            NavController navController = navHostFragment.getNavController();
            NavDirections action = MyLessonsFragmentDirections.actionMyLessonsFragmentToAccountFragment();
            navController.navigate(action);
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserLessons();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void showConfirmDialog(int lessonId) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Join Lesson");
        alertDialog.setMessage("Are you ready to join this lesson?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Join",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        joinLesson(lessonId);
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

    private void joinLesson(int lessonId) {

        timeShareApi.joinLesson(lessonId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        lessonViewModel.setLessonId(lessonId);

                        NavController navController = navHostFragment.getNavController();
                        NavDirections action = MyLessonsFragmentDirections.actionMyLessonsFragmentToClassroomFragment();
                        navController.navigate(action);
                    } else {
                        Snackbar.make(getActivity().findViewById(android.R.id.content), "You do not have sufficient balance.", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();

//                        Toast.makeText(getContext(), "You do not have sufficient balance for this lesson", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();

            }
        });

    }


    private void getUserLessons() {


        if (userViewModel.getUser().getValue() != null) {

            LoadingDialog loadingDialog = new LoadingDialog(getActivity(), true);
            loadingDialog.setMessage("Retrieving Lessons...");
            loadingDialog.startLoadingDialog();

            timeShareApi.getUserLessons(userViewModel.getUser().getValue().getId()).enqueue(new Callback<List<LessonDTO>>() {
                @Override
                public void onResponse(Call<List<LessonDTO>> call, Response<List<LessonDTO>> response) {
                    loadingDialog.dismissDialog();

                    if (response.isSuccessful()) {
                        lessons = response.body();

                        System.out.println(response.body());
                        System.out.println("SUCCESS");

                        adapter.setLessons(lessons);

                    }
                }

                @Override
                public void onFailure(Call<List<LessonDTO>> call, Throwable t) {
                    loadingDialog.dismissDialog();

                    System.out.println("failed");
                    System.out.println(t.getMessage());

                }
            });


        }

    }
}
