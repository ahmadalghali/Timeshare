package uk.ac.gre.aa5119a.timelearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.gre.aa5119a.timelearn.dialog.LoginDialog2;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.model.notification.Notification;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;
import uk.ac.gre.aa5119a.timelearn.viewmodel.UserViewModel;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;


//websocket imports

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.net.URI;
import java.net.URISyntaxException;
//import tech.gusavila92.websocketclient.WebSocketClient;


public class MainActivity extends AppCompatActivity {

    public static UserViewModel userViewModel;
    public static AcademyViewModel academyViewModel;

    public static BottomNavigationView bottomNavigation;

    private User loggedInUser;

    public static NavHostFragment navHostFragment;

    public static TimeShareApi timeShareApi;




    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        assignGlobalVariables();
        setUpNavigation();

        initRetrofit();



    }

    private void getNotifications(){

        Log.d(TAG, "retrieving notifications");

        timeShareApi.getUserNotifications(userViewModel.getUser().getValue().getId()).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if(response.isSuccessful()){
                     int notificationCount = response.body().size();

                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {

            }
        });
    }


    private void initRetrofit(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://timeshare-backend.herokuapp.com/")
                .baseUrl("http://192.168.0.11:8081/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();



        timeShareApi = retrofit.create(TimeShareApi.class);


    }

    public void setUpNavigation() {

        navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        NavigationUI.setupWithNavController(bottomNavigation,
                navHostFragment.getNavController());
    }

//    public void promptSignIn() {
//        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please sign in", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation);
//
//        snackbar.setAction("SIGN IN", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginDialog2 loginDialog = new LoginDialog2(MainActivity.this, userViewModel);
//                loginDialog.show();
//            }
//        });
//        snackbar.show();
//    }

    private void assignGlobalVariables() {
        bottomNavigation = findViewById(R.id.bottom_navigation);


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, user -> {
            loggedInUser = user;
        });

        academyViewModel = new ViewModelProvider(this).get(AcademyViewModel.class);
    }


    public static void refreshUserDetails() {
        timeShareApi.getUser(userViewModel.getUser().getValue().getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    userViewModel.setUser(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }


}