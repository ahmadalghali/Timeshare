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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.adapter.NotificationAdapter;
import uk.ac.gre.aa5119a.timelearn.dialog.LoadingDialog;
import uk.ac.gre.aa5119a.timelearn.dialog.LoginDialog2;
import uk.ac.gre.aa5119a.timelearn.model.learn.ClassBooking;
import uk.ac.gre.aa5119a.timelearn.model.notification.Notification;
import uk.ac.gre.aa5119a.timelearn.model.notification.NotificationClassBooking;
import uk.ac.gre.aa5119a.timelearn.utils.LoadingCircle;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.userViewModel;


public class NotificationsFragment extends Fragment {

    private static final String TAG = "NotificationsFragment";

    List<Notification> notifications = new ArrayList<>();
    RecyclerView rvNotifications;
    View view;

    NotificationAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notifications, container, false);

        assignGlobalVariables();
        getNotifications();

        return view;
    }

    private void assignGlobalVariables() {
        rvNotifications = view.findViewById(R.id.rvNotifications);

        rvNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new NotificationAdapter(getContext(), new NotificationAdapter.NotificationClickListener() {
            @Override
            public void onAcceptButtonClicked(int position, int classBookingId) {
                Toast.makeText(getContext(), "Notification " + position + " Accepted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRejectButtonClicked(int position, int classBookingId) {

                timeShareApi.setClassBookingAccepted(classBookingId, false).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        getNotifications();

                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        getNotifications();

                    }
                });
            }

            @Override
            public void onViewButtonClicked(int position, int classId) {

            }

        });

        rvNotifications.setAdapter(adapter);
    }


    private void getNotifications() {

        Log.d(TAG, "retrieving notifications");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //TODO your background code

                if (userViewModel.getUser().getValue() != null) {

                    timeShareApi.getUserNotifications(userViewModel.getUser().getValue().getId()).enqueue(new Callback<List<Notification>>() {
                        @Override
                        public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {

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

                        }
                    });

                    try {
                        Thread.sleep(500);
                        getNotifications();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                }

            }
        });


    }


    public void promptSignIn() {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please sign in", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation);

        snackbar.setAction("SIGN IN", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginDialog2 loginDialog = new LoginDialog2(getActivity(), userViewModel);
                loginDialog.show();
            }
        });
        snackbar.show();
    }
}
