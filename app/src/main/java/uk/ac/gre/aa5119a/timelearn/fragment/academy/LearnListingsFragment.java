package uk.ac.gre.aa5119a.timelearn.fragment.academy;

import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;

public class LearnListingsFragment extends Fragment {

    private View view;
    private AcademyViewModel academyViewModel;
    private HomeViewModel homeViewModel;


    private ImageView backBtn;
    private ImageView ivSubjectIcon;
    private TextView tvSubjectTitle;
    private TextView tvClassCount;

    private RecyclerView rvListings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_learn_listings, container, false);

        assignGlobalVariables();
        setListeners();

        return view;
    }

    private void assignGlobalVariables(){

        backBtn = view.findViewById(R.id.backBtn);
        ivSubjectIcon = view.findViewById(R.id.ivSubjectIcon);
        tvSubjectTitle = view.findViewById(R.id.tvSubject);
        tvClassCount = view.findViewById(R.id.tvListingCount);
        rvListings = view.findViewById(R.id.rvListings);

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
