package uk.ac.gre.aa5119a.timelearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import uk.ac.gre.aa5119a.timelearn.fragment.AcademyFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.AccountFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.HomeFragment;
import uk.ac.gre.aa5119a.timelearn.fragment.SearchFragment;
import uk.ac.gre.aa5119a.timelearn.model.User;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_account:
                    selectedFragment = new AccountFragment();
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