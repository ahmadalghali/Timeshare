package uk.ac.gre.aa5119a.timelearn.fragment.academy;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.utils.Effects;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;

public class AcademyFragment extends Fragment {


    private Button learnButton;
    private Button teachButton;

    private View view;
    private AcademyViewModel academyViewModel;

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_academy, container, false);

        assignGlobalVariables();
        setListeners();

        return view;
    }

    private void assignGlobalVariables(){
        learnButton = view.findViewById(R.id.learnBtn);
        teachButton = view.findViewById(R.id.teachBtn);
        academyViewModel = new ViewModelProvider(requireActivity()).get(AcademyViewModel.class);

    }

    private void setListeners(){

        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v,Effects.WHITE_PRESSED_COLOR);
                academyViewModel.setEducationType("Learn");

                NavController navController = navHostFragment.getNavController();
                NavDirections action = AcademyFragmentDirections.actionAcademyFragmentToCategoriesFragment();
                navController.navigate(action);
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesFragment()).commit();

            }
        });

        teachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v,Effects.WHITE_PRESSED_COLOR);
                academyViewModel.setEducationType("Teach");

//                NavHostFragment navHostFragment =
//                        (NavHostFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
//
//                NavController navController = navHostFragment.getNavController();

//                navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
//                        .findFragmentById(R.id.fragment_container);

                NavController navController = navHostFragment.getNavController();
                NavDirections action = AcademyFragmentDirections.actionAcademyFragmentToCategoriesFragment();
                navController.navigate(action);


//                Navigation.findNavController(view).navigate(action);


//                navController = Navigation.findNavController(view);
//
//                navController.navigate();

//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesFragment()).commit();
            }
        });


    }

    public static void buttonEffect(View button, String hexColor){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
