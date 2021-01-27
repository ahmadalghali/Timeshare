package uk.ac.gre.aa5119a.timelearn.dialog;

import androidx.appcompat.app.AlertDialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import static uk.ac.gre.aa5119a.timelearn.MainActivity.timeShareApi;

public class LoginDialog2{

    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private Button signInButton;
    private TextView registerButton;


    private UserViewModel userViewModel;

    private  Dialog loginDialog;

    private View view;

    private static final String BLUE_PRESSED_COLOR = "#006fab";
    private Activity activity;

    public LoginDialog2(Activity activity, UserViewModel userViewModel){

        this.activity = activity;
        this.userViewModel = userViewModel;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_login, null);

        builder.setView(view);

        loginDialog = builder.create();

        loginDialog.setCanceledOnTouchOutside(true);
        loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        assignGlobalVariables();
        initListeners();
    }


    private void assignGlobalVariables(){
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        signInButton = view.findViewById(R.id.signInButton);
        registerButton = view.findViewById(R.id.registerButton);
    }

    private void initListeners(){

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v,BLUE_PRESSED_COLOR);
                signInButtonClicked();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showRegisterDialog();
            }
        });
    }


//    private void showRegisterDialog(){
//        RegisterDialog registerDialog = new RegisterDialog();
//        registerDialog.setTargetFragment(getTargetFragment(), 1);
//        dismiss();
//        registerDialog.show(getActivity().getSupportFragmentManager(), "RegisterDialog");
//
//    }




    private Boolean areFieldsEmpty(){

        TextInputLayout[] fields = {etEmail, etPassword};

        boolean empty = false;

        for(TextInputLayout field : fields){
            String input = field.getEditText().getText().toString().trim();

            if(input.isEmpty()){
                field.setError("Required");
                empty = true;
            } else{
                field.setError(null);
                empty = false;
            }
        }
        return empty;
    }

    private void signInButtonClicked(){
        LoadingDialog loadingDialog = new LoadingDialog(activity);
        loadingDialog.setMessage("Logging in...");
        loadingDialog.startLoadingDialog();

        if(areFieldsEmpty()){
            loadingDialog.dismissDialog();
            return;
        }

        String email = etEmail.getEditText().getText().toString().trim();
        String password = etPassword.getEditText().getText().toString().trim();

        Call<LoginResponse> call = timeShareApi.login(new User(email, password));



        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loadingDialog.dismissDialog();

                if(!response.isSuccessful()){
                    Snackbar.make(activity.findViewById(android.R.id.content), "Error: " + response.code(), BaseTransientBottomBar.LENGTH_LONG).show();
                    loadingDialog.dismissDialog();
                    return;
                }


                LoginResponse loginResponse = response.body();

                if(loginResponse.getMessage().equals("logged in")){


                    userViewModel.setUser(loginResponse.getUser());

                    dismiss();



                    bottomNavigation.getMenu().clear();
                    bottomNavigation.inflateMenu(R.menu.bottom_navigation_logged_in);

                    dismiss();

                    Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content), "Welcome " + loginResponse.getUser().getEmail(), BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.setAnchorView(bottomNavigation);
                    snackbar.show();


                } else if(loginResponse.getMessage().equals("incorrect password")){

                    etPassword.setError("Incorrect password");

                } else if(loginResponse.getMessage().equals("user does not exist")){

                    etEmail.setError("User not found, register.");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loadingDialog.dismissDialog();

                Snackbar.make(activity.findViewById(android.R.id.content),t.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
//                showSnackBar(t.getMessage());
            }
        });

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

    public void show(){
        loginDialog.show();
    }

    public void dismiss(){
        loginDialog.dismiss();
    }

}