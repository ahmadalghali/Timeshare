package uk.ac.gre.aa5119a.timelearn.fragment.account;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.adapter.MyLessonsAdapter;
import uk.ac.gre.aa5119a.timelearn.adapter.MyTeachingAdapter;
import uk.ac.gre.aa5119a.timelearn.dialog.LoadingDialog;
import uk.ac.gre.aa5119a.timelearn.model.LessonDTO;
import uk.ac.gre.aa5119a.timelearn.viewmodel.LessonViewModel;
import uk.ac.gre.aa5119a.timelearn.viewmodel.UserViewModel;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.userViewModel;


public class MyTeachingFragment extends Fragment {


    List<LessonDTO> lessons = new ArrayList<>();
    RecyclerView rvLessons;
    View view;
    ImageView backBtn;

    MyTeachingAdapter adapter;

    LessonViewModel lessonViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_my_teaching, container, false);

        assignGlobalVariables();
        getUserTeachingLessons();

        return view;
    }


    private void assignGlobalVariables() {

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        lessonViewModel = new ViewModelProvider(getActivity()).get(LessonViewModel.class);

        backBtn = view.findViewById(R.id.backBtn);
        rvLessons = view.findViewById(R.id.rvLessons);

        rvLessons.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyTeachingAdapter(getContext(), new MyTeachingAdapter.LessonClickListener() {
            @Override
            public void onStartClassButtonClicked(int position, int lessonId) {
                showConfirmDialog(lessonId);
            }
        });
        rvLessons.setAdapter(adapter);

        backBtn.setOnClickListener(v ->  {
            NavController navController = navHostFragment.getNavController();
            NavDirections action = MyTeachingFragmentDirections.actionMyTeachingFragmentToAccountFragment();
            navController.navigate(action);
        });
    }

    private void showConfirmDialog(int lessonId) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Start Class");
        alertDialog.setMessage("Are you ready to start this class?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Start",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startClass(lessonId);
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

    private void startClass(int lessonId) {
        lessonViewModel.setLessonId(lessonId);

        NavController navController = navHostFragment.getNavController();
        NavDirections action = MyTeachingFragmentDirections.actionMyTeachingFragmentToClassroomFragment();
        navController.navigate(action);
    }


    private void getUserTeachingLessons() {


        if (userViewModel.getUser().getValue() != null) {

            LoadingDialog loadingDialog = new LoadingDialog(getActivity(), true);
            loadingDialog.setMessage("Loading classes...");
            loadingDialog.startLoadingDialog();

            timeShareApi.getUserTeachingLessons(userViewModel.getUser().getValue().getId()).enqueue(new Callback<List<LessonDTO>>() {
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
