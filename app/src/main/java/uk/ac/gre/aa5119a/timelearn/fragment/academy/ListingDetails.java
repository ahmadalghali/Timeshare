package uk.ac.gre.aa5119a.timelearn.fragment.academy;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.dialog.LoginDialog2;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;
import uk.ac.gre.aa5119a.timelearn.viewmodel.UserViewModel;
import uk.ac.gre.aa5119a.timelearn.web.request.ClassBookingRequest;
import uk.ac.gre.aa5119a.timelearn.web.response.TeacherListingResponse;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;

public class ListingDetails extends Fragment implements DatePickerDialog.OnDateSetListener {

    private ImageView backBtn;
    private ImageView ivInPerson;
    private ImageView ivOnline;

    private ImageView ivUserPhoto;
    private TextView tvUserName;
    private RatingBar rbUserRating;
    private TextView tvRatingCount;

    private TextView tvTitle;
    private TextView tvDescription;
    private ImageView ivSubjectIcon;

    private TextView tvCity;

    private NumberPicker npHours;
    private TextView tvTimeRate;
    private Button btnBookClass;

    private ImageView ivPickDate;
    private TextView tvClassDate;

    private View view;

    private AcademyViewModel academyViewModel;
    private UserViewModel userViewModel;

    final Calendar calendar = Calendar.getInstance();
    Calendar classDate = Calendar.getInstance();

    private static final String TAG = "ListingDetails";

    private TeacherListingResponse listingResponse;

    private boolean isRequested = false;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_listing_details, null);

        try {
            assignGlobalVariables();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setListeners();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void assignGlobalVariables() throws IOException {

        backBtn = view.findViewById(R.id.backBtn);

        ivUserPhoto = view.findViewById(R.id.ivUserPhoto);
        tvUserName = view.findViewById(R.id.tvUserName);
        rbUserRating = view.findViewById(R.id.rbUserRating);
        tvRatingCount = view.findViewById(R.id.tvRatingCount);
        tvDescription = view.findViewById(R.id.tvListingDescription);
        ivSubjectIcon = view.findViewById(R.id.ivSubjectIcon);
        tvCity = view.findViewById(R.id.tvCity);
        npHours = view.findViewById(R.id.npHours);
        tvTimeRate = view.findViewById(R.id.tvTimeRate);
        btnBookClass = view.findViewById(R.id.btnBookClass);
        ivInPerson = view.findViewById(R.id.ivInPerson);
        ivOnline = view.findViewById(R.id.ivOnline);
        tvTitle = view.findViewById(R.id.tvListingTitle);

        tvClassDate = view.findViewById(R.id.tvClassDate);

        String formattedCurrentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(System.currentTimeMillis());

        tvClassDate.setText(formattedCurrentDate);


        ivPickDate = view.findViewById(R.id.ivPickDate);

        academyViewModel = new ViewModelProvider(getActivity()).get(AcademyViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);


        listingResponse = academyViewModel.getListingResponse().getValue();

        if (!listingResponse.getUser().getProfileImageUrl().equals(null) && !listingResponse.getUser().getProfileImageUrl().equals("") && listingResponse.getUser().getProfileImageUrl() != null) {
            Picasso.get()
                    .load(listingResponse.getUser().getProfileImageUrl())
                    .placeholder(R.drawable.ic_account)
                    .into(ivUserPhoto);
        } else {
            ivUserPhoto.setImageResource(R.drawable.ic_account);
        }

        tvUserName.setText(listingResponse.getUser().getEmail());

        tvTitle.setText(listingResponse.getListing().getTitle());

        rbUserRating.isIndicator();
        rbUserRating.setRating((float) listingResponse.getUser().getRatingScore());
        tvRatingCount.setText("" + listingResponse.getUser().getRatingCount());

        tvDescription.setText(listingResponse.getListing().getDescription());

//        Picasso.get()
//                .load(listingResponse.getSubject().getIcon())
//                .into(ivSubjectIcon);

        tvCity.setText(listingResponse.getUser().getCity());


        npHours.setTextSize(40);
        npHours.setMinValue(1);
        npHours.setMaxValue(6);

        tvTimeRate.setText("" + listingResponse.getListing().getTimeRate());

        if (!listingResponse.getListing().getTeachingStyleIds().contains(1)) {
            ivInPerson.setVisibility(View.GONE);
        }
        if (!listingResponse.getListing().getTeachingStyleIds().contains(2)) {
            ivOnline.setVisibility(View.GONE);
        }

        switch (academyViewModel.getSubjectChosen().getValue().getSubjectId()) {
            case 1:
                ivSubjectIcon.setImageResource(R.drawable.ic_maths);
                break;
            case 2:
                ivSubjectIcon.setImageResource(R.drawable.ic_physics);
                break;
            case 3:
                ivSubjectIcon.setImageResource(R.drawable.ic_chemistry);
                break;
            case 4:
                ivSubjectIcon.setImageResource(R.drawable.ic_computer);
                break;
            case 5:
                ivSubjectIcon.setImageResource(R.drawable.ic_english);
                break;
        }


        if (userViewModel.getUser().getValue() != null) {
            if (isClassRequested()) {
                btnBookClass.setText("Sent Request");
                btnBookClass.setEnabled(false);
                btnBookClass.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }
        } else {
            promptSignIn();
        }


    }

    private Boolean isClassRequested() {
        int classId = academyViewModel.getListingResponse().getValue().getListing().getId();
        int studentId = userViewModel.getUser().getValue().getId();

        timeShareApi.isClassRequestedByUser(classId, studentId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: ");
                    Log.d(TAG, "isRequest = " + response.body());
                    isRequested = response.body();
                } else {
                    Log.d(TAG, "isRequest = failed " + response.body());

                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d(TAG, "isRequest = failed " + t.getMessage());


            }
        });

        return isRequested;
    }

    private void setListeners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = navHostFragment.getNavController();
                NavDirections action = ListingDetailsDirections.actionListingDetailsToLearnListingsFragment2();
                navController.navigate(action);
            }
        });

        btnBookClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean userIsLoggedIn = userViewModel.getUser().getValue() != null;

                if (userIsLoggedIn) {
                    if (isClassRequested()) {
                        btnBookClass.setText("Request Already");
                        btnBookClass.setEnabled(false);
                        btnBookClass.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    } else {
                        sendBookingRequest();
                    }

                } else {
                    promptSignIn();
                }

            }


        });

        npHours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tvTimeRate.setText("" + newVal * listingResponse.getListing().getTimeRate());
            }
        });

        ivPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                updateLabel();
//            }
//
//        };

    }


    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 604800000);


        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        classDate.set(Calendar.YEAR, year);
        classDate.set(Calendar.MONTH, month);
        classDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String formattedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(classDate.getTimeInMillis()));

        tvClassDate.setText(formattedDate);
    }

    private void sendBookingRequest() {

        int classId = academyViewModel.getListingResponse().getValue().getListing().getId();
        int studentId = userViewModel.getUser().getValue().getId();
        int hours = npHours.getValue();


        ClassBookingRequest classBooking = new ClassBookingRequest(studentId, classId, false, hours, classDate.getTimeInMillis());

        Log.d(TAG, classBooking.toString());

        Call<Boolean> call = timeShareApi.bookClass(classBooking);
        Log.d(TAG, "sending call waiting for response");

        call.enqueue(new Callback<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d(TAG, "response is here");

                if (response.isSuccessful()) {
                    Log.d(TAG, "response is successful");

                    Log.d(TAG, "response.body() " + response.body());

                    if (response.body()) {
//                                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Sent class request", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();


                        btnBookClass.setText("Sent Request");
                        btnBookClass.setEnabled(false);
                        btnBookClass.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

                        Log.d(TAG, "response.body() " + response.body());

//                                    NavController navController = navHostFragment.getNavController();
//                                    NavDirections action = ListingDetailsDirections.actionListingDetailsToLearnListingsFragment2();
//                                    navController.navigate(action);
                    } else {

                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Class request could not be made", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();
                    }

                } else {
                    Log.d(TAG, "response unsuccessful");

                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Class request could not be made", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d(TAG, "response failed");

                Snackbar.make(getActivity().findViewById(android.R.id.content), "failed", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();

            }
        });

    }

    private void promptSignIn() {
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
