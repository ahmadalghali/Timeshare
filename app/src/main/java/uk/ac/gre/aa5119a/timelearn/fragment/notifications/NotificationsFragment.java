package uk.ac.gre.aa5119a.timelearn.fragment.notifications;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.adapter.NotificationAdapter;
import uk.ac.gre.aa5119a.timelearn.dialog.LoadingDialog;
import uk.ac.gre.aa5119a.timelearn.dialog.LoginDialog2;
import uk.ac.gre.aa5119a.timelearn.fragment.academy.ListingDetailsDirections;
import uk.ac.gre.aa5119a.timelearn.model.learn.ClassBooking;
import uk.ac.gre.aa5119a.timelearn.model.notification.Notification;
import uk.ac.gre.aa5119a.timelearn.model.notification.NotificationClassBooking;
import uk.ac.gre.aa5119a.timelearn.utils.LoadingCircle;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.userViewModel;


public class NotificationsFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";

    List<Notification> notifications = new ArrayList<>();
    RecyclerView rvNotifications;
    View view;

    NotificationAdapter adapter;
    LoadingDialog loadingDialog;

//    LoadingCircle loadingCircle = new LoadingCircle(getActivity());


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notifications, container, false);

        assignGlobalVariables();
        getNotifications();

        return view;
    }

//    Runnable getNotificationsRunnable = new Runnable() {
//        public void run() {
//            getNotifications();
//        }
//    };

    private void assignGlobalVariables() {

//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        executor.scheduleAtFixedRate(getNotificationsRunnable, 0, 5, TimeUnit.SECONDS);

        rvNotifications = view.findViewById(R.id.rvNotifications);

        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotificationAdapter(getContext(), new NotificationAdapter.NotificationClickListener() {
            @Override
            public void onAcceptButtonClicked(int position, int classBookingId) {
//                Toast.makeText(getContext(), "Notification " + position + " Accepted", Toast.LENGTH_SHORT).show();
                timeShareApi.setClassBookingAccepted(classBookingId, true).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        System.out.println("response: " + response);
                        System.out.println("issuccessful: " + response.isSuccessful());
                        System.out.println("message: " + response.message());
                        System.out.println("body: " + response.body());
                        if (response.isSuccessful()) {
                            getNotifications();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
//                getNotifications();
            }

            @Override
            public void onRejectButtonClicked(int position, int classBookingId) {


                loadingDialog.startLoadingDialog();


                timeShareApi.setClassBookingAccepted(classBookingId, false).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        loadingDialog.dismissDialog();

                        getNotifications();

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        loadingDialog.dismissDialog();

                    }
                });
            }

            @Override
            public void onViewButtonClicked(int position, int classBookingId) {


                System.out.println("deleting classBookingId: " + classBookingId);

                timeShareApi.deleteNotification(classBookingId).enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            NavController navController = navHostFragment.getNavController();
                            NavDirections action = NotificationsFragmentDirections.actionNotificationsFragmentToMyLessonsFragment();
                            navController.navigate(action);
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        System.out.println(t.getMessage());

                    }
                });
            }

        });

        rvNotifications.setAdapter(adapter);

         loadingDialog = new LoadingDialog(getActivity(), true);

    }


    private void getNotifications() {


        if (userViewModel.getUser().getValue() != null) {

            loadingDialog.setMessage("Retrieving notifications...");
            loadingDialog.startLoadingDialog();


            timeShareApi.getUserNotifications(userViewModel.getUser().getValue().getId()).enqueue(new Callback<List<Notification>>() {
                @Override
                public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                    loadingDialog.dismissDialog();
                    if (response.isSuccessful()) {
                        notifications = response.body();


//                        int menuItemId = bottomNavigation.getMenu().getItem(3).getItemId();
//                        BadgeDrawable notificationBadge = bottomNavigation.getOrCreateBadge(menuItemId);
//                        notificationBadge.setVi
                        adapter.setNotifications(notifications);


                    }
                }

                @Override
                public void onFailure(Call<List<Notification>> call, Throwable t) {
                    loadingDialog.dismissDialog();


                }
            });


        }


    }


    public void promptSignIn() {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please sign in", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation);

        snackbar.setAction("SIGN IN", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog2 loginDialog = new LoginDialog2(getActivity(), userViewModel,null);
                loginDialog.show();
            }
        });
        snackbar.show();
    }
}
