package uk.ac.gre.aa5119a.timelearn.fragment.academy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.model.Listing;
import uk.ac.gre.aa5119a.timelearn.model.ui.Subject;
import uk.ac.gre.aa5119a.timelearn.utils.Effects;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;

public class TeachSubjectDetailsFragment extends Fragment {


    private View view;
    private AcademyViewModel academyViewModel;
    private HomeViewModel homeViewModel;

    private TextView tvSubject;


    private ImageView backBtn;
    private TextInputLayout etTitle;
    private EditText etDescription;
    private ImageView ivQualification;
    private Button uploadQualificationBtn;
    private CheckBox cbInPerson;
    private CheckBox cbOnline;
    private CheckBox cbWeekdays;
    private CheckBox cbWeekends;
    private Button addListingBtn;
    private Button cancelBtn;

    public static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;


    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_teach_subject_details, container, false);

        assignGlobalVariables();
        setListeners();

        return view;
    }

    private void assignGlobalVariables(){

        academyViewModel = new ViewModelProvider(requireActivity()).get(AcademyViewModel.class);
        tvSubject = view.findViewById(R.id.tvSubject);
        backBtn = view.findViewById(R.id.backBtn);
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        ivQualification = view.findViewById(R.id.ivQualification);
        uploadQualificationBtn = view.findViewById(R.id.uploadQualificationBtn);
        cbInPerson = view.findViewById(R.id.cbInperson);
        cbOnline = view.findViewById(R.id.cbOnline);
        cbWeekdays = view.findViewById(R.id.cbWeekdays);
        cbWeekends = view.findViewById(R.id.cbWeekends);
        addListingBtn = view.findViewById(R.id.addListingButton);
        cancelBtn = view.findViewById(R.id.cancelBtn);



        tvSubject.setText("Subject: " + academyViewModel.getSubjectChosen().getValue().getTitle());

    }



    private void setListeners(){


        uploadQualificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // TODO: UPLOAD IMAGE LOGIC

                chooseImage();
            }
        });

        addListingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: ADD LISTING LOGIC

                buttonEffect(v,"#b87d00");
//                Listing listing = new Listing(academyViewModel.getSubjectChosen().getValue(),
//                       homeViewModel.getUser().getValue(),etTitle.getEditText().getText().toString(), )
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v,"#383838");
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesAcademicFragment()).commit();
                NavController navController = navHostFragment.getNavController();
                NavDirections action = TeachSubjectDetailsFragmentDirections.actionTeachSubjectDetailsFragmentToCategoriesAcademicFragment();
                navController.navigate(action);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesAcademicFragment()).commit();
                NavController navController = navHostFragment.getNavController();
                NavDirections action = TeachSubjectDetailsFragmentDirections.actionTeachSubjectDetailsFragmentToCategoriesAcademicFragment();
                navController.navigate(action);
            }
        });


    }


    private void getUserInput(){

        Subject subject = academyViewModel.getSubjectChosen().getValue();
        String title = etTitle.getEditText().getText().toString();
        String description = etDescription.getText().toString();
        boolean inPersonTeaching = cbInPerson.isChecked();
        boolean onlineTeaching = cbOnline.isChecked();
        boolean weekdaysAvailability = cbWeekdays.isChecked();
        boolean weekendsAvailability = cbWeekends.isChecked();




    }


    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null ){

            imageUri = data.getData();


            Picasso.get().load(imageUri)
                    .into(ivQualification);
        }
    }

    public static void buttonEffect(View button, String hexColor){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
