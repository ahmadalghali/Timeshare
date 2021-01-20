package uk.ac.gre.aa5119a.timelearn.fragment.academy;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;
import uk.ac.gre.aa5119a.timelearn.web.response.TeacherListingResponse;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;

public class ListingDetails extends Fragment {

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

    private View view;

    private AcademyViewModel academyViewModel;

    private TeacherListingResponse listingResponse;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_listing_details,null);

        assignGlobalVariables();
        setListeners();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void assignGlobalVariables(){
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

        academyViewModel = new ViewModelProvider(getActivity()).get(AcademyViewModel.class);



        listingResponse = academyViewModel.getListingResponse().getValue();

        if(!listingResponse.getUser().getProfileImageUrl().equals(null) && !listingResponse.getUser().getProfileImageUrl().equals("") && listingResponse.getUser().getProfileImageUrl() != null ){
            Picasso.get()
                    .load(listingResponse.getUser().getProfileImageUrl())
                    .placeholder(R.drawable.ic_account)
                    .into(ivUserPhoto);
        }else{
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

        if(!listingResponse.getListing().getTeachingStyleIds().contains(1)){
            ivInPerson.setVisibility(View.GONE);
        }
        if(!listingResponse.getListing().getTeachingStyleIds().contains(2)){
            ivOnline.setVisibility(View.GONE);
        }

        switch(academyViewModel.getSubjectChosen().getValue().getSubjectId()){
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


    }

    private void setListeners(){
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

            }
        });

        npHours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                tvTimeRate.setText("" + newVal * listingResponse.getListing().getTimeRate());
            }
        });

    }
}
