package uk.ac.gre.aa5119a.timelearn.fragment.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.fragment.academy.ListingDetailsDirections;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.UserViewModel;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;

public class AccountFragment extends Fragment {


    TextView btnMyClass;
    TextView btnMyTeaching;

    Button btnClassCount;
    Button btnTeachingClassCount;

    CircleImageView ivUserPhoto;
    private UserViewModel userViewModel;


    TextView tvUserName;
    TextView tvRatingCount;
    RatingBar rbUserRating;

    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_account, container, false);

        assignGlobalVariables();
        setListeners();
        boolean userIsLoggedIn = userViewModel.getUser().getValue() != null;

        if (userIsLoggedIn) {
            setUserDetails();
        }

        bottomNavigation.setVisibility(View.VISIBLE);
        return view;
    }

    private void setUserDetails() {
        User user = userViewModel.getUser().getValue();

        Picasso.get()
                .load(user.getProfileImageUrl())
                .placeholder(R.drawable.ic_account)
                .into(ivUserPhoto);

        tvUserName.setText(user.getFirstname());

        tvRatingCount.setText("" + user.getRatingCount());

        rbUserRating.isIndicator();
        rbUserRating.setRating((float) user.getRatingScore());

        timeShareApi.getUserLessonCount(user.getId()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int classCount = response.body();
                if (classCount == 0) {
                    btnClassCount.setVisibility(View.INVISIBLE);
                } else {
                    btnClassCount.setVisibility(View.VISIBLE);
                    btnClassCount.setText("" + classCount);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                btnClassCount.setVisibility(View.INVISIBLE);

            }
        });

        timeShareApi.getUserTeachingLessonCount(user.getId()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int classCount = response.body();
                    if (classCount == 0) {
                        btnTeachingClassCount.setVisibility(View.INVISIBLE);
                    } else {
                        btnTeachingClassCount.setVisibility(View.VISIBLE);
                        btnTeachingClassCount.setText("" + classCount);
                    }
                } else {
                    System.out.println("could not get teacher class count  " + response);
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                btnTeachingClassCount.setVisibility(View.INVISIBLE);

            }
        });

    }

    private void setListeners() {
        btnMyClass.setOnClickListener(v -> {
            NavController navController = navHostFragment.getNavController();
            NavDirections action = AccountFragmentDirections.actionAccountFragmentToMyLessonsFragment();
            navController.navigate(action);
        });

        btnMyTeaching.setOnClickListener(v -> {
            NavController navController = navHostFragment.getNavController();
            NavDirections action = AccountFragmentDirections.actionAccountFragmentToMyTeachingFragment();
            navController.navigate(action);
        });


    }

    private void assignGlobalVariables() {
        btnMyClass = view.findViewById(R.id.btnMyClass);
        btnMyTeaching = view.findViewById(R.id.btnMyTeaching);

        ivUserPhoto = view.findViewById(R.id.ivUserPhoto);
        tvUserName = view.findViewById(R.id.tvUserName);

        btnClassCount = view.findViewById(R.id.btnClassCount);
        btnTeachingClassCount = view.findViewById(R.id.btnTeachingClassCount);


        tvRatingCount = view.findViewById(R.id.tvRatingCount);
        rbUserRating = view.findViewById(R.id.rbUserRating);

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

    }

}
