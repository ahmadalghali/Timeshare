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


//    private WebSocketClient webSocketClient;


//    PubNub pubnub;

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        assignGlobalVariables();
        setUpNavigation();

        initRetrofit();

//        createWebSocketClient();
//        initPubNub();


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
                .baseUrl("https://timeshare-backend.herokuapp.com/")
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

//    public void publishMessage(String animal_sound){
//        // Publish message to the global chanel
//        pubnub.publish()
//                .message(animal_sound)
//                .channel("global_channel")
//                .async(new PNCallback<PNPublishResult>() {
//                    @Override
//                    public void onResponse(PNPublishResult result, PNStatus status) {
//                        // status.isError() to see if error happened and print status code if error
//                        if(status.isError()) {
//                            System.out.println("pub status code: " + status.getStatusCode());
//                        }
//                    }
//                });
//    }
//
//    public void initPubNub(){
//        PNConfiguration pnConfiguration = new PNConfiguration();
//        pnConfiguration.setPublishKey("pub-c-6f535bd8-eb59-44fa-8611-e67668b64c3b"); // REPLACE with your pub key
//        pnConfiguration.setSubscribeKey("sub-c-d2fa4514-5db1-11eb-8c5b-2e0ea7dd7f9a"); // REPLACE with your sub key
//        pnConfiguration.setSecure(true);
//        pubnub = new PubNub(pnConfiguration);
//        // Listen to messages that arrive on the channel
//        pubnub.addListener(new SubscribeCallback() {
//            @Override
//            public void status(PubNub pub, PNStatus status) {
//            }
//            @Override
//            public void message(PubNub pub, final PNMessageResult message) {
//                // Replace double quotes with a blank space
//                final String msg = message.getMessage().toString().replace("\"", "");
//                textView = findViewById(R.id.animalSound);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                            // Display the message on the app
//                            textView.setText(msg);
//                        } catch (Exception e){
//                            System.out.println("Error");
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//            @Override
//            public void presence(PubNub pub, PNPresenceEventResult presence) {
//            }
//        });
//        // Subscribe to the global channel
//        pubnub.subscribe()
//                .channels(Arrays.asList("global_channel"))
//                .execute();
//    }


//    public void sendMessage(View view) {
//        Log.i("WebSocket", "Button was clicked");
//        // Send button id string to WebSocket Server
//        switch(view.getId()){
//            case(R.id.dogButton):
//                webSocketClient.send("1");
//                break;
//            case(R.id.catButton):
//                webSocketClient.send("2");
//                break;
//            case(R.id.pigButton):
//                webSocketClient.send("3");
//                break;
//            case(R.id.foxButton):
//                webSocketClient.send("4");
//                break;
//        }
//    }


//    private void createWebSocketClient(){
//        URI uri;
//        try {
//            // Connect to local host
//            uri = new URI("ws://10.0.2.2:8080/websocket");
//        }
//        catch (URISyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//        webSocketClient = new WebSocketClient(uri) {
//            @Override
//            public void onOpen() {
//                Log.i("WebSocket", "Session is starting");
//                webSocketClient.send("Hello World!");
//            }
//            @Override
//            public void onTextReceived(String s) {
//                Log.i("WebSocket", "Message received");
//                final String message = s;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try{
//                            TextView textView = findViewById(R.id.animalSound);
//                            textView.setText(message);
//                        } catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//            @Override
//            public void onBinaryReceived(byte[] data) {
//            }
//            @Override
//            public void onPingReceived(byte[] data) {
//            }
//            @Override
//            public void onPongReceived(byte[] data) {
//            }
//            @Override
//            public void onException(Exception e) {
//                System.out.println(e.getMessage());
//            }
//            @Override
//            public void onCloseReceived() {
//                Log.i("WebSocket", "Closed ");
//                System.out.println("onCloseReceived");
//            }
//        };
//        webSocketClient.setConnectTimeout(10000);
//        webSocketClient.setReadTimeout(60000);
//        webSocketClient.enableAutomaticReconnection(5000);
//        webSocketClient.connect();
//
//    }


}