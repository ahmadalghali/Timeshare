package uk.ac.gre.aa5119a.timelearn.fragment.home;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.dialog.LoginDialogDirections;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;

public class HomeFragmentLoggedIn extends Fragment{

    private ImageView profileButton;
    private View view;

    private TextView tvTimeCredits;
    private TextView tvEmail;

    private User loggedInUser;


    private Dialog logoutDialog;
    private TimeShareApi timeShareApi;
    private LinearLayout loginPageLayout;

    private Button cancelButton;
    private Button logoutButton;

    private HomeViewModel homeViewModel;



    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fragment_home_logged_in, container, false);
        assignGlobalVariables();
        setListeners();

       tvEmail = logoutDialog.findViewById(R.id.tvEmail);


        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        homeViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if(user != null){
                tvEmail.setText(user.getEmail());
            }
        });

        return view;
    }





    private void assignGlobalVariables(){
        profileButton = view.findViewById(R.id.profileButton);
        tvTimeCredits = view.findViewById(R.id.timeCredits);
        logoutDialog = new Dialog(view.getContext());
        logoutDialog.setContentView(R.layout.dialog_logout);


    }

    private void setListeners(){
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });
    }

    private void showLogoutDialog(){
//        TODO:
//        logoutDialog.setContentView(R.layout.dialog_logout);

        initLogoutDialogVariables();
        initLogoutDialogListeners();
//        tvEmail.setText(loggedInUser.getEmail());



        logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        logoutDialog.show();

    }

    private void initLogoutDialogVariables(){

        logoutButton = logoutDialog.findViewById(R.id.logoutDialog_logoutBtn);
        cancelButton = logoutDialog.findViewById(R.id.logoutDialog_cancelBtn);
//        tvEmail = logoutDialog.findViewById(R.id.tvEmail);

    }

    private void initLogoutDialogListeners(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutButtonClicked();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.dismiss();
            }
        });
    }


    private void logoutButtonClicked(){

//        TODO: logout logic
        homeViewModel.setUser(null);
        bottomNavigation.getMenu().clear();
        bottomNavigation.inflateMenu(R.menu.bottom_navigation);

        logoutDialog.dismiss();

        NavController navController = navHostFragment.getNavController();
        NavDirections action = HomeFragmentLoggedInDirections.actionHomeFragmentLoggedInToHomeFragment();
        navController.navigate(action);
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }


//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
//
//        homeViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
//            @Override
//            public void onChanged(User loggedInUser) {
//                tvEmail.setText(loggedInUser.getEmail());
//            }
//
//
//        });
//    }

//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
//        homeViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
//            loggedInUser = user;
//        });
//    }
}
