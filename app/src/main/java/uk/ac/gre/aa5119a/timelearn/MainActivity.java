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

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.gre.aa5119a.timelearn.fragment.academy.AcademyFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.account.AccountFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.home.HomeFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.home.HomeFragmentLoggedIn;
import uk.ac.gre.aa5119a.timelearn.fragment.notifications.NotificationsFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.search.SearchFragment;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;

public class MainActivity extends AppCompatActivity {

    private HomeViewModel homeViewModel;

    public static BottomNavigationView bottomNavigation;

    private User loggedInUser;

    public static NavHostFragment navHostFragment;

    public static TimeShareApi timeShareApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        assignGlobalVariables();
        setUpNavigation();

        initRetrofit();

    }

    private void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://timeshare-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        timeShareApi = retrofit.create(TimeShareApi.class);


    }

    public void setUpNavigation() {

        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        NavigationUI.setupWithNavController(bottomNavigation,
                navHostFragment.getNavController());
    }

    private void assignGlobalVariables() {
        bottomNavigation = findViewById(R.id.bottom_navigation);


        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getUser().observe(this, user -> {
            loggedInUser = user;
        });
    }
}