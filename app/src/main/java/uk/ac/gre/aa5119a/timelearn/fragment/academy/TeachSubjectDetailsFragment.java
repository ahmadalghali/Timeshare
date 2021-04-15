package uk.ac.gre.aa5119a.timelearn.fragment.academy;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.dialog.LoadingDialog;
import uk.ac.gre.aa5119a.timelearn.dialog.LoginDialog2;
import uk.ac.gre.aa5119a.timelearn.model.listing.ImageUpload;
import uk.ac.gre.aa5119a.timelearn.model.listing.Subject;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;
import uk.ac.gre.aa5119a.timelearn.viewmodel.UserViewModel;
import uk.ac.gre.aa5119a.timelearn.web.response.TeacherListingResponse;
import uk.ac.gre.aa5119a.timelearn.web.request.TeacherListingRequest;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;

public class TeachSubjectDetailsFragment extends Fragment {


    private View view;
    private AcademyViewModel academyViewModel;
    private UserViewModel userViewModel;

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
    private NumberPicker npTimeRate;

//    private EditText etPhoneNumber;

    private Button addListingBtn;
    private Button cancelBtn;

    private TextView tvTeachingStyle;
    private TextView tvDescription;


    private Subject subject;
    private String title;
    private String description;
    private boolean inPersonTeaching;
    private boolean onlineTeaching;
    private boolean weekdaysAvailability;
    private boolean weekendsAvailability;
    private double timeRate;

//    private String phoneNumber;

    List<Integer> teachingStyles;
    List<Integer> daysOfAvailability;

    private TextView tvAvailability;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private StorageTask uploadtask;

    public static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;

    private ImageUpload imageUpload;

    private LoadingDialog loadingDialog;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_teach_subject_details, container, false);

        assignGlobalVariables();
        setListeners();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void assignGlobalVariables() {

        academyViewModel = new ViewModelProvider(requireActivity()).get(AcademyViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

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
        tvTeachingStyle = view.findViewById(R.id.tvTeachingStyle);
        tvAvailability = view.findViewById(R.id.textView7);
        tvDescription = view.findViewById(R.id.textView8);

//        etPhoneNumber =  view.findViewById(R.id.etPhoneNumber);
        npTimeRate = view.findViewById(R.id.nbTimeRate);

        storageReference = FirebaseStorage.getInstance().getReference("image_uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("image_uploads");


        tvSubject.setText("Subject: " + academyViewModel.getSubjectChosen().getValue().getTitle());
        npTimeRate.setTextColor(Color.WHITE);
        String[] rates = {"1", "1.25", "1.5", "1.75", "2"};

        npTimeRate.setMinValue(1);
        npTimeRate.setMaxValue(5);
        npTimeRate.setDisplayedValues(rates);
        npTimeRate.setTextSize(50);


    }

    private void setListeners() {


        uploadQualificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        addListingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v, "#b87d00");



                if (uploadtask != null && uploadtask.isInProgress()) {
                    Toast.makeText(getContext(), "Please wait...", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog = new LoadingDialog(getActivity(), false);
                    loadingDialog.startLoadingDialog();


                    uploadImage();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v, "#383838");
                NavController navController = navHostFragment.getNavController();
                NavDirections action = TeachSubjectDetailsFragmentDirections.actionTeachSubjectDetailsFragmentToCategoriesAcademicFragment();
                navController.navigate(action);

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = navHostFragment.getNavController();
                NavDirections action = TeachSubjectDetailsFragmentDirections.actionTeachSubjectDetailsFragmentToCategoriesAcademicFragment();
                navController.navigate(action);
            }
        });


    }


    private void addListing() {

        if (!areFieldsEmpty()) {

            loadingDialog.setMessage("Creating Listing...");

            getUserInput();

            if (userViewModel.getUser().getValue() != null) {

                TeacherListingRequest teacherListingRequest;
//                uploadImage();
               if(imageUri != null){
                    teacherListingRequest = new TeacherListingRequest(subject.getSubjectId(), userViewModel.getUser().getValue().getId(), title, description, imageUpload.getImageUrl(), timeRate, teachingStyles, daysOfAvailability);
               } else{
                   teacherListingRequest = new TeacherListingRequest(subject.getSubjectId(), userViewModel.getUser().getValue().getId(), title, description, null, timeRate, teachingStyles, daysOfAvailability);
               }

//               teacherListingRequest.setPhoneNumber(phoneNumber);

                Log.d("request", teacherListingRequest.toString());



                Call<TeacherListingResponse> call = timeShareApi.addListing(teacherListingRequest);

                call.enqueue(new Callback<TeacherListingResponse>() {
                    @Override
                    public void onResponse(Call<TeacherListingResponse> call, Response<TeacherListingResponse> response) {
                        loadingDialog.dismissDialog();

                        if (!response.isSuccessful()) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Error: " + response.code(), BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();
                            return;
                        }

                        TeacherListingResponse listingResponse = response.body();

                        if (listingResponse.getMessage().toLowerCase().equals("added")) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Listing created successfully.", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();

                            NavController navController = navHostFragment.getNavController();
                            NavDirections action = TeachSubjectDetailsFragmentDirections.actionTeachSubjectDetailsFragmentToAcademyFragment();
                            navController.navigate(action);

                        } else if (listingResponse.getMessage().toLowerCase().equals("failed to save in database")) {
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Failed to save listing", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TeacherListingResponse> call, Throwable t) {
                        loadingDialog.dismissDialog();

                        Snackbar.make(getActivity().findViewById(android.R.id.content), t.getMessage(), BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();

                    }
                });


            } else {
                loadingDialog.dismissDialog();

                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please sign in first", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation);

                snackbar.setAction("SIGN IN", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginDialog2 loginDialog = new LoginDialog2(getActivity(), userViewModel, null);
                        loginDialog.show();
                    }
                });
                snackbar.show();
            }


        } else {
            loadingDialog.dismissDialog();

            Snackbar.make(getActivity().findViewById(android.R.id.content), "Please fill in the required fields", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();
        }
    }

    private void uploadImage() {


        if (imageUri != null) {
            loadingDialog.setMessage("Uploading Image...");

            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            uploadtask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    loadingDialog.setMessage("Creating listing...");


                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();


                    imageUpload = new ImageUpload(downloadUrl.toString());

                    String imageKey = databaseReference.push().getKey();

                    databaseReference.child(imageKey).setValue(imageUpload);

                    addListing();


                }
            }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            //No image selected
            addListing();
//            Snackbar.make(view, "Please add a qualification", BaseTransientBottomBar.LENGTH_LONG).show();

        }
    }


    private void getUserInput() {


        subject = academyViewModel.getSubjectChosen().getValue();
        title = etTitle.getEditText().getText().toString();
        description = etDescription.getText().toString();
        inPersonTeaching = cbInPerson.isChecked();
        onlineTeaching = cbOnline.isChecked();
        weekdaysAvailability = cbWeekdays.isChecked();
        weekendsAvailability = cbWeekends.isChecked();
//        phoneNumber = etPhoneNumber.getText().toString();

        switch (npTimeRate.getValue()) {
            case 1:
                timeRate = 1.0;
                break;
            case 2:
                timeRate = 1.25;
                break;
            case 3:
                timeRate = 1.5;
                break;
            case 4:
                timeRate = 1.75;
                break;
            case 5:
                timeRate = 2.0;
                break;
        }


        teachingStyles = new ArrayList<>();
        daysOfAvailability = new ArrayList<>();

        if (inPersonTeaching) {
            teachingStyles.add(1);
        }

        if (onlineTeaching) {
            teachingStyles.add(2);
        }

        if (weekdaysAvailability) {
            daysOfAvailability.add(1);
        }

        if (weekendsAvailability) {
            daysOfAvailability.add(2);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();


            Picasso.get().load(imageUri).resize(400, 400)
                    .centerCrop()
                    .into(ivQualification);


        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);


    }

    private Boolean areFieldsEmpty() {
        boolean empty = false;


        if (etTitle.getEditText().getText().toString().trim().equals("")) {
            etTitle.setError("Required");
            empty = true;
        }

        if (etDescription.getText().toString().trim().equals("")) {

            tvDescription.setError("Required");
            empty = true;

        }

        if (!cbInPerson.isChecked() && !cbOnline.isChecked()) {
            tvTeachingStyle.setError("Please choose at least one");
            empty = true;

        }


        if (!cbWeekends.isChecked() && !cbWeekdays.isChecked()) {
            tvAvailability.setError("Please choose at least one");
            empty = true;
        }

        return empty;
    }

    public static void buttonEffect(View button, String hexColor) {
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

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}
