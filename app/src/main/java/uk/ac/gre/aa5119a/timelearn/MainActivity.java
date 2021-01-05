package uk.ac.gre.aa5119a.timelearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uk.ac.gre.aa5119a.timelearn.fragment.AcademyFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.AccountFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.HomeFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.HomeFragmentLoggedIn;
import uk.ac.gre.aa5119a.timelearn.fragment.SearchFragment;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;

public class MainActivity extends AppCompatActivity {

    private HomeViewModel homeViewModel;

    private BottomNavigationView bottomNavigation;

    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.getUser().observe(this, user -> {
            loggedInUser = user;
        });

        assignGlobalVariables();

//        if(getIntent().hasExtra("user")){
//            User user = getIntent().getParcelableExtra("user");
//
//            tvWelcomeMessage.setText("Welcome " + user.getEmail());
//        }
    }

    private void assignGlobalVariables(){
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNavigationListener);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_home:
                    if(loggedInUser != null){
                        selectedFragment = new HomeFragmentLoggedIn();
                    } else{
                        selectedFragment = new HomeFragment();
                    }
                    break;
                case R.id.nav_account:
//                    if(loggedInUser != null){
//                        selectedFragment = new HomeFragmentLoggedIn();

//                    } else{
                        selectedFragment = new AccountFragment();
//                    }
                    break;
                case R.id.nav_academy:
                    selectedFragment = new AcademyFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }

    };

}