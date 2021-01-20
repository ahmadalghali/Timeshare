package uk.ac.gre.aa5119a.timelearn.fragment.academy;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.adapter.LearnClassesAdapter;
import uk.ac.gre.aa5119a.timelearn.dialog.LoadingDialog;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;
import uk.ac.gre.aa5119a.timelearn.web.response.TeacherListingResponse;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;

public class LearnListingsFragment extends Fragment {

    private View view;
    private AcademyViewModel academyViewModel;
    private HomeViewModel homeViewModel;


    private ImageView backBtn;
    private ImageView ivSubjectIcon;
    private TextView tvSubjectTitle;
    private TextView tvClassCount;

    private RecyclerView rvListings;

    private LearnClassesAdapter adapter;

    private List<TeacherListingResponse> responseList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_learn_listings, container, false);

        assignGlobalVariables();
        setListeners();

        return view;
    }

    private void assignGlobalVariables(){

        academyViewModel = new ViewModelProvider(getActivity()).get(AcademyViewModel.class);
        homeViewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        backBtn = view.findViewById(R.id.backBtn);
        ivSubjectIcon = view.findViewById(R.id.ivSubjectIcon);
        tvSubjectTitle = view.findViewById(R.id.tvSubject);
        tvClassCount = view.findViewById(R.id.tvListingCount);
        rvListings = view.findViewById(R.id.rvListings);

        switch(academyViewModel.getSubjectChosen().getValue().getSubjectId()){
            case 1:
                ivSubjectIcon.setImageResource(R.drawable.ic_maths);
                tvSubjectTitle.setText("Maths");
                break;
            case 2:
                ivSubjectIcon.setImageResource(R.drawable.ic_physics);
                tvSubjectTitle.setText("Physics");
                break;
            case 3:
                ivSubjectIcon.setImageResource(R.drawable.ic_chemistry);
                tvSubjectTitle.setText("Chemistry");
                break;
            case 4:
                ivSubjectIcon.setImageResource(R.drawable.ic_computer);
                tvSubjectTitle.setText("Computing");
                break;
            case 5:
                ivSubjectIcon.setImageResource(R.drawable.ic_english);
                tvSubjectTitle.setText("English");
                break;
        }

        adapter = new LearnClassesAdapter();

        initRecyclerView();
    }

    private void initRecyclerView(){

        int subjectId = academyViewModel.getSubjectChosen().getValue().getSubjectId();
        getClassesBySubject(subjectId);

//        tvClassCount.setText("Showing " + classes.size() + " classes");


        adapter.setOnClassClickedListener(new LearnClassesAdapter.OnClassClickedListener() {
            @Override
            public void onClassClicked(int position) {

                ConstraintLayout listItemLayout = view.findViewById(R.id.listItemLayout);
                listItemLayout.setBackgroundColor(Color.parseColor("#dbdbdb"));

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 300ms
                        listItemLayout.setBackgroundColor(Color.TRANSPARENT);

                    }
                }, 100);

                TeacherListingResponse listingResponse = responseList.get(position);

                academyViewModel.setListingResponse(listingResponse);

                NavController navController = navHostFragment.getNavController();
                NavDirections action = LearnListingsFragmentDirections.actionLearnListingsFragment2ToListingDetails();
                navController.navigate(action);

            }
        });

        rvListings.setLayoutManager(new LinearLayoutManager(getContext()));

        rvListings.setAdapter(adapter);
    }

    private void getClassesBySubject(int subjectId){

        Call<List<TeacherListingResponse>> call =  timeShareApi.getClassesBySubject(subjectId);
        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.setMessage("Loading classes...");
        loadingDialog.startLoadingDialog();

        call.enqueue(new Callback<List<TeacherListingResponse>>() {
            @Override
            public void onResponse(Call<List<TeacherListingResponse>> call, Response<List<TeacherListingResponse>> response) {
                loadingDialog.dismissDialog();

                if(!response.isSuccessful()){
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Error: " + response.message(), BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();
                    return;
                }

                responseList = response.body();

                adapter.setResponseList(responseList);
                tvClassCount.setText("Showing " + responseList.size() + " classes");
            }

            @Override
            public void onFailure(Call<List<TeacherListingResponse>> call, Throwable t) {
                loadingDialog.dismissDialog();
                Snackbar.make(getActivity().findViewById(android.R.id.content), t.getMessage() , BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();

            }
        });

    }

    private void setListeners(){

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = navHostFragment.getNavController();
                NavDirections action = LearnListingsFragmentDirections.actionLearnListingsFragment2ToCategoriesAcademicFragment();
                navController.navigate(action);
            }
        });


    }
}
