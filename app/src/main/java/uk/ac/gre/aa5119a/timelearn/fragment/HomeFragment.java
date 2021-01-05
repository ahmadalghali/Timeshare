package uk.ac.gre.aa5119a.timelearn.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider;

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
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;
import uk.ac.gre.aa5119a.timelearn.web.LoginResponse;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;

public class HomeFragment extends Fragment  {

    private HomeViewModel homeViewModel;
    private ImageView profileButton;
    private Dialog loginDialog;
    private View view;


    // login dilaog
    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private Button signInButton;
    private TextView registerButton;

    private TimeShareApi timeShareApi;
    private ConstraintLayout homeFragmentLayout;
    private LinearLayout loginPageLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        assignGlobalVariables();
        setListeners();
        initRetrofit();


        return view;
    }



    private void assignGlobalVariables(){
        homeFragmentLayout = view.findViewById(R.id.homeFragmentLayout);
        loginPageLayout = view.findViewById(R.id.loginPageLayout);
        profileButton = view.findViewById(R.id.profileButton);
        loginDialog = new Dialog(view.getContext());
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    private void setListeners(){
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog(){
        loginDialog.setContentView(R.layout.activity_login);


        initLoginPopUpVariables();
        initLoginPopUpListeners();


        loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loginDialog.show();

    }

    private void initLoginPopUpVariables(){
        etEmail = loginDialog.findViewById(R.id.etEmail);
        etPassword = loginDialog.findViewById(R.id.etPassword);
        signInButton = loginDialog.findViewById(R.id.signInButton);
        registerButton = loginDialog.findViewById(R.id.registerButton);

    }

    private void initLoginPopUpListeners(){
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
                    Snackbar.make(homeFragmentLayout, "Error: " + response.code(), BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }


                LoginResponse loginResponse = response.body();

                if(loginResponse.getMessage().equals("logged in")){


//                    TODO: logged in logic

                    homeViewModel.setUser(loginResponse.getUser());


                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragmentLoggedIn()).commit();

                    loginDialog.dismiss();


                } else if(loginResponse.getMessage().equals("incorrect password")){
                    etPassword.setError("Incorrect password");

                } else if(loginResponse.getMessage().equals("user does not exist")){
                    etEmail.setError("User not found, register.");
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Snackbar.make(homeFragmentLayout,t.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

    }

    private void registerButtonClicked(){

        //   TODO: open register layout

//        Intent intent = new Intent(this, RegisterActivity.class);
//
//        String email = etEmail.getEditText().getText().toString().trim();
//        if(!email.isEmpty()){
//            intent.putExtra("newUser", email);
//        }
//        startActivity(intent);
    }

    private void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://timeshare-backend.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        timeShareApi = retrofit.create(TimeShareApi.class);


    }



}
