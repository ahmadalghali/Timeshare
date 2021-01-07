package uk.ac.gre.aa5119a.timelearn.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.gre.aa5119a.timelearn.LoadingDialog;
import uk.ac.gre.aa5119a.timelearn.LoginActivity;
import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.RegisterActivity;
import uk.ac.gre.aa5119a.timelearn.fragment.HomeFragmentLoggedIn;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;
import uk.ac.gre.aa5119a.timelearn.web.LoginResponse;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;

public class LoginDialog2 extends DialogFragment {

    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private Button signInButton;
    private TextView registerButton;

    private TimeShareApi timeShareApi;

    private LinearLayout loginPageLayout;

    private HomeViewModel homeViewModel;

    private  Dialog loginDialog;

    private View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_login, null);

        builder.setView(view);

        loginDialog = builder.create();

        loginDialog.setCanceledOnTouchOutside(true);
        loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        assignGlobalVariables();
        initListeners();
        initRetrofit();


        return loginDialog;
    }




    private void assignGlobalVariables(){
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        signInButton = view.findViewById(R.id.signInButton);
        registerButton = view.findViewById(R.id.registerButton);
        loginPageLayout = view.findViewById(R.id.loginPageLayout);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    private void initListeners(){

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInButtonClicked();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerButtonClicked();
            }
        });
    }


    private void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://timeshare-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        timeShareApi = retrofit.create(TimeShareApi.class);


    }


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

        if(areFieldsEmpty()){
            return;
        }

        String email = etEmail.getEditText().getText().toString().trim();
        String password = etPassword.getEditText().getText().toString().trim();

        Call<LoginResponse> call = timeShareApi.login(new User(email, password));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(!response.isSuccessful()){
                    Snackbar.make(loginPageLayout, "Error: " + response.code(), BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }


                LoginResponse loginResponse = response.body();

                if(loginResponse.getMessage().equals("logged in")){

                    homeViewModel.setUser(loginResponse.getUser());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragmentLoggedIn()).commit();
                    dismiss();

                } else if(loginResponse.getMessage().equals("incorrect password")){
                    etPassword.setError("Incorrect password");

                } else if(loginResponse.getMessage().equals("user does not exist")){
                    etEmail.setError("User not found, register.");
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Snackbar.make(loginPageLayout,t.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

    }

    private void registerButtonClicked(){
//
//        Intent intent = new Intent(this, RegisterActivity.class);
//
//        String email = etEmail.getEditText().getText().toString().trim();
//        if(!email.isEmpty()){
//            intent.putExtra("newUser", email);
//        }
//        startActivity(intent);
    }

}