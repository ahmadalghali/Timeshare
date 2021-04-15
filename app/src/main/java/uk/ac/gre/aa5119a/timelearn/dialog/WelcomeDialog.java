package uk.ac.gre.aa5119a.timelearn.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.UserViewModel;
import uk.ac.gre.aa5119a.timelearn.web.response.LoginResponse;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.userViewModel;

public class WelcomeDialog {

    private Button btnGetStarted;
    private TextView tvUserName;



    private Dialog welcomeDialog;

    private View view;

    private Activity activity;


    public WelcomeDialog(Activity activity) {

        this.activity = activity;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_welcome, null);

        builder.setView(view);

        welcomeDialog = builder.create();

        welcomeDialog.setCanceledOnTouchOutside(true);
        welcomeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        assignGlobalVariables();
        initListeners();
    }


    private void assignGlobalVariables() {
        btnGetStarted = view.findViewById(R.id.btnGetStarted);
        tvUserName = view.findViewById(R.id.tvUserName);

        tvUserName.setText("Hello " + userViewModel.getUser().getValue().getFirstname()+ ",");
    }

    private void initListeners() {
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    public void show() {


        welcomeDialog.show();
    }

    public void dismiss() {
        welcomeDialog.dismiss();
    }

}