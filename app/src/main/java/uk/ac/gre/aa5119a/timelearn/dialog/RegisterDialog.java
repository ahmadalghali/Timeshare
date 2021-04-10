package uk.ac.gre.aa5119a.timelearn.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.UserViewModel;
import uk.ac.gre.aa5119a.timelearn.web.response.RegisterResponse;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.bottomNavigation;
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;

public class RegisterDialog {


    private UserViewModel userViewModel;

    private Dialog registerDialog;

    private View view;


    // login dialog
    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private TextInputLayout etConfirmPassword;

    private TextView signInButton;
    private Button registerButton;

    private Activity activity;

    private LoadingDialog loadingDialog;

    public RegisterDialog(Activity activity, UserViewModel userViewModel) {

        this.activity = activity;
        this.userViewModel = userViewModel;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_register, null);

        builder.setView(view);

        registerDialog = builder.create();

        registerDialog.setCanceledOnTouchOutside(true);
        registerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        assignGlobalVariables();
        setListeners();
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        LayoutInflater inflater = requireActivity().getLayoutInflater();
//        view = inflater.inflate(R.layout.dialog_register, null);
//
//        builder.setView(view);
//
//        registerDialog = builder.create();
//
//        registerDialog.setCanceledOnTouchOutside(true);
//        registerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        assignGlobalVariables();
//        setListeners();
//        initRetrofit();
//
//
//        return registerDialog;
//    }

    private void assignGlobalVariables() {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        registerButton = view.findViewById(R.id.registerButton);
        signInButton = view.findViewById(R.id.signInButton);

    }

    private void setListeners() {

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoginDialog();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonClicked();
            }
        });
    }


    private void showLoginDialog() {
        LoginDialog2 loginDialog = new LoginDialog2(activity, userViewModel, null);
        dismiss();
        loginDialog.show();
    }

    private Boolean areFieldsEmpty() {

        TextInputLayout[] fields = {etEmail, etPassword, etConfirmPassword};

        boolean empty = false;

        for (TextInputLayout field : fields) {
            if (field.getEditText().getText().toString().trim().isEmpty()) {
                field.setError("Required");
                empty = true;
            } else {
                field.setError(null);
                empty = false;
            }
        }
        return empty;
    }


    private void registerButtonClicked() {

        etEmail.setError(null);
        etConfirmPassword.setError(null);
        etPassword.setError(null);

        if (areFieldsEmpty()) {
            return;
        }


        String email = etEmail.getEditText().getText().toString().trim();
        String password = etPassword.getEditText().getText().toString().trim();
        String confirmPassword = etConfirmPassword.getEditText().getText().toString().trim();

        if (!confirmPassword.equals(password)) {
            etConfirmPassword.setError("Password does not match");
            return;
        } else {
            etConfirmPassword.setError(null);
        }

        loadingDialog = new LoadingDialog(activity, false);
        loadingDialog.setMessage("Registering...");
        loadingDialog.startLoadingDialog();

        Call<RegisterResponse> call = timeShareApi.register(new User(email, password));


        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                loadingDialog.dismissDialog();

                if (!response.isSuccessful()) {
                    Snackbar.make(activity.findViewById(android.R.id.content), "Error: " + response.errorBody() + " Code: " + response.code(), BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }


                RegisterResponse registerResponse = response.body();

                if (registerResponse.getMessage().equals("user exists")) {

                    etEmail.setError("User exists, sign in.");

                } else if (registerResponse.getMessage().equals("registered")) {
                    etEmail.setError(null);

                    showLoginDialog();


                    Snackbar.make(activity.findViewById(android.R.id.content), "Registered successfully, sign in.", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                loadingDialog.dismissDialog();

                Snackbar.make(activity.findViewById(android.R.id.content), t.getMessage(), BaseTransientBottomBar.LENGTH_LONG).setAnchorView(bottomNavigation).show();
            }
        });

    }

    public void show(){
        registerDialog.show();
    }

    public void dismiss(){
        registerDialog.dismiss();
    }



}
