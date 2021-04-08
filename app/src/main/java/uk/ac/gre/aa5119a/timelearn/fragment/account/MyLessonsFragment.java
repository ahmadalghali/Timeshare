package uk.ac.gre.aa5119a.timelearn.fragment.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import uk.ac.gre.aa5119a.timelearn.dialog.LoadingDialog;
import uk.ac.gre.aa5119a.timelearn.model.LessonDTO;
import uk.ac.gre.aa5119a.timelearn.viewmodel.UserViewModel;


import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.userViewModel;


public class MyLessonsFragment extends Fragment {


    List<LessonDTO> lessons = new ArrayList<>();
    RecyclerView rvLessons;
    View view;
    ImageView backBtn;

    MyLessonsAdapter adapter;

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

        backBtn = view.findViewById(R.id.backBtn);
        rvLessons = view.findViewById(R.id.rvLessons);

        rvLessons.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyLessonsAdapter(getContext(), new MyLessonsAdapter.LessonClickListener() {
            @Override
            public void onJoinButtonClicked(int position, int lessonId) {

            }
        });
        rvLessons.setAdapter(adapter);

        backBtn.setOnClickListener(v ->  {
            NavController navController = navHostFragment.getNavController();
            NavDirections action = MyLessonsFragmentDirections.actionMyLessonsFragmentToAccountFragment();
            navController.navigate(action);
        });
    }


    private void getUserLessons() {


        if (userViewModel.getUser().getValue() != null) {

            LoadingDialog loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.setMessage("Retrieving Lessons...");
            loadingDialog.startLoadingDialog();

            timeShareApi.getUserLessons(userViewModel.getUser().getValue().getId()).enqueue(new Callback<List<LessonDTO>>() {
                @Override
                public void onResponse(Call<List<LessonDTO>> call, Response<List<LessonDTO>> response) {
                    loadingDialog.dismissDialog();

                    if (response.isSuccessful()) {
                        lessons = response.body();

                        System.out.println(response.body());

                        adapter.setLessons(lessons);

                    }
                }

                @Override
                public void onFailure(Call<List<LessonDTO>> call, Throwable t) {
                    loadingDialog.dismissDialog();

                }
            });


        }

    }
}
