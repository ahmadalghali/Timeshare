package uk.ac.gre.aa5119a.timelearn.fragment.classroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import org.jetbrains.annotations.NotNull;

import uk.ac.gre.aa5119a.timelearn.R;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;

public class RateUserFragment extends Fragment {

    View view;
    ImageView btnExit;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rate_user, null);

        bottomNavigation.setVisibility(View.GONE);

        assignGlobalVariables();
        setListeners();

        return view;
    }

    private void setListeners() {
        btnExit.setOnClickListener(v -> {

            NavController navController = navHostFragment.getNavController();
            NavDirections action = RateUserFragmentDirections.actionRateUserFragmentToAccountFragment();
            navController.navigate(action);

        });
    }

    private void assignGlobalVariables() {
        btnExit = view.findViewById(R.id.btnExit);
    }


}
