package uk.ac.gre.aa5119a.timelearn.fragment.academy;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.dialog.LoginDialog;
import uk.ac.gre.aa5119a.timelearn.model.ImageUpload;
import uk.ac.gre.aa5119a.timelearn.model.TeacherListing;
import uk.ac.gre.aa5119a.timelearn.model.ui.Subject;
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

    private TextView tvTeachingStyle;
    private TextView tvDescription;


    private TextView tvAvailability;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;


    public static final int PICK_IMAGE_REQUEST = 1;

    private Uri imageUri;

    String imageId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_teach_subject_details, container, false);

        assignGlobalVariables();
        setListeners();

        return view;
    }

    private void assignGlobalVariables() {

        academyViewModel = new ViewModelProvider(requireActivity()).get(AcademyViewModel.class);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

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

        storageReference = FirebaseStorage.getInstance().getReference("image_uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("image_uploads");


        tvSubject.setText("Subject: " + academyViewModel.getSubjectChosen().getValue().getTitle());

    }


    private void setListeners() {


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

                buttonEffect(v, "#b87d00");

                addListing();
//                TeacherListing listing = new TeacherListing(academyViewModel.getSubjectChosen().getValue(),
//                       homeViewModel.getUser().getValue(),etTitle.getEditText().getText().toString(), )
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v, "#383838");
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

    private void addListing() {

        if (!areFieldsEmpty()) {

            getUserInput();


        } else {
            Snackbar.make(view, "Please fill in the required fields", BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    private void getUserInput() {


        Subject subject = academyViewModel.getSubjectChosen().getValue();
        String title = etTitle.getEditText().getText().toString();
        String description = etDescription.getText().toString();
        boolean inPersonTeaching = cbInPerson.isChecked();
        boolean onlineTeaching = cbOnline.isChecked();

        boolean weekdaysAvailability = cbWeekdays.isChecked();
        boolean weekendsAvailability = cbWeekends.isChecked();

        imageId = null;

        List<String> teachingStyles = new ArrayList<>();
        List<String> daysOfAvailability = new ArrayList<>();

        if (inPersonTeaching) {
            teachingStyles.add("In-Person");
        }

        if (onlineTeaching) {
            teachingStyles.add("Online");
        }

        if (weekdaysAvailability) {
            daysOfAvailability.add("Weekdays");
        }

        if (weekendsAvailability) {
            daysOfAvailability.add("Weekends");
        }


        if (homeViewModel.getUser().getValue() != null) {


            uploadImage();
            TeacherListing teacherListing = new TeacherListing(subject, homeViewModel.getUser().getValue(), title, description, imageId, teachingStyles, daysOfAvailability);

            System.out.println(teacherListing.toString());
            NavController navController = navHostFragment.getNavController();
            NavDirections action = TeachSubjectDetailsFragmentDirections.actionTeachSubjectDetailsFragmentToAcademyFragment();
            navController.navigate(action);

        } else {
            Snackbar snackbar = Snackbar.make(view, "Please sign in first", BaseTransientBottomBar.LENGTH_INDEFINITE);

            snackbar.setAction("SIGN IN", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginDialog loginDialog = new LoginDialog();
                    loginDialog.setTargetFragment(getTargetFragment(), 1);
                    loginDialog.show(getActivity().getSupportFragmentManager(), "LoginDialog");
                }
            });
            snackbar.show();
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        ImageUpload imageUpload = new ImageUpload(
                                downloadUri.toString());

//                        imageId = databaseReference.push().getKey();
//                        databaseReference.child(imageId).setValue(imageUpload);

                        databaseReference.push().setValue(imageUpload);
                        imageId = databaseReference.getKey();


                    } else {
                        Toast.makeText(getContext(), "Upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } else {
            //No image selected
        }
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


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();


            Picasso.get().load(imageUri).resize(400,400)
                    .centerCrop()
                    .into(ivQualification);


        }
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
}
