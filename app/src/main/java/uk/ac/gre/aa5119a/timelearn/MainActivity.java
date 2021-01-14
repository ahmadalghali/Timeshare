package uk.ac.gre.aa5119a.timelearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uk.ac.gre.aa5119a.timelearn.fragment.academy.AcademyFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.account.AccountFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.home.HomeFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.home.HomeFragmentLoggedIn;
import uk.ac.gre.aa5119a.timelearn.fragment.notifications.NotificationsFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.search.SearchFragment;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;

public class MainActivity extends AppCompatActivity {

    private HomeViewModel homeViewModel;

    private BottomNavigationView bottomNavigation;

    private User loggedInUser;

//    NavController navController;

    public static  NavHostFragment navHostFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        assignGlobalVariables();
        setUpNavigation();

//        if(getIntent().hasExtra("user")){
//            User user = getIntent().getParcelableExtra("user");
//
//            tvWelcomeMessage.setText("Welcome " + user.getEmail());
//        }
    }


    public void setUpNavigation(){

         navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        NavigationUI.setupWithNavController(bottomNavigation,
                navHostFragment.getNavController());
    }


    private void assignGlobalVariables(){
        bottomNavigation = findViewById(R.id.bottom_navigation);
//        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getUser().observe(this, user -> {
            loggedInUser = user; 
        });


//        NavHostFragment navHostFragment =
//                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//        NavController navController = navHostFragment.getNavController();
//        navController = Navigation.findNavController(getCurrentFocus().);


//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }


//    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            Fragment selectedFragment = null;
//
//            switch (item.getItemId()){
//                case R.id.nav_home:
//                    if(loggedInUser != null){
//                        selectedFragment = new HomeFragmentLoggedIn();
//                    } else{
//                        selectedFragment = new HomeFragment();
//                    }
//                    break;
//                case R.id.nav_account:
////                    if(loggedInUser != null){
////                        selectedFragment = new HomeFragmentLoggedIn();
//
////                    } else{
//                        selectedFragment = new AccountFragment();
////                    }
//                    break;
//                case R.id.nav_academy:
////                    NavController navController = Navigation.findNavController(getCurrentFocus());
////                    navController.navigate(R.id.action_homeFragment2_to_academyFragment2);
//
//                    selectedFragment = new AcademyFragment();
//                    break;
//                case R.id.nav_search:
//                    selectedFragment = new SearchFragment();
//                    break;
//                case R.id.nav_notifications:
//                    selectedFragment = new NotificationsFragment();
//                    break;
//
//            }
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
//            return true;
//        }
//
//    };

}