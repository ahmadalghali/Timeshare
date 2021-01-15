package uk.ac.gre.aa5119a.timelearn.fragment.home;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.dialog.LoginDialog;
import uk.ac.gre.aa5119a.timelearn.dialog.LoginDialogDirections;
import uk.ac.gre.aa5119a.timelearn.dialog.RegisterDialog;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ImageView profileButton;

    private View view;

    private TimeShareApi timeShareApi;
    private ConstraintLayout homeFragmentLayout;

    private LoginDialog loginDialog;
    private RegisterDialog registerDialog;

    private static final String BLUE_PRESSED_COLOR = "#006fab";
    private static final String WHITE_PRESSED_COLOR = "#808080";

//    NavController navController;



    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_home, container, false);
        assignGlobalVariables();
//        if(homeViewModel.getUser().getValue() != null){
//            NavController navController = navHostFragment.getNavController();
//            NavDirections action = HomeFragmentDirections.actionHomeFragmentToHomeFragmentLoggedIn();
//            navController.navigate(action);
//        }

        setListeners();
        initRetrofit();

        return view;
    }




    private void assignGlobalVariables(){
        homeFragmentLayout = view.findViewById(R.id.homeFragmentLayout);
        profileButton = view.findViewById(R.id.profileButton);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

//        navController = Navigation.findNavController(view);

    }

    private void setListeners(){
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog(){

        NavController navController = navHostFragment.getNavController();
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToLoginDialog();
        navController.navigate(action);

//        loginDialog = new LoginDialog();
////
//        loginDialog.setTargetFragment(getTargetFragment(), 1);
////
//        loginDialog.show(getActivity().getSupportFragmentManager(), "LoginDialog");
    }



    private void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://timeshare-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        timeShareApi = retrofit.create(TimeShareApi.class);


    }

    public static void buttonEffect(View button, String hexColor){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(Color.parseColor(hexColor),PorterDuff.Mode.SRC_ATOP);
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
