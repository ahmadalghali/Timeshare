package uk.ac.gre.aa5119a.timelearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.web.RegisterResponse;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private TextInputLayout etConfirmPassword;

    private Button registerButton;
    private TextView signInButton;

    private TimeShareApi timeShareApi;

    private LinearLayout registerPageLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_register);

        assignGlobalVariables();
        initListeners();

        initRetrofit();

        if(getIntent().hasExtra("newUser")){
            String newUserEmail = getIntent().getStringExtra("newUser");

            etEmail.getEditText().setText(newUserEmail);
        }

    }

    private void assignGlobalVariables(){
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        registerButton = findViewById(R.id.registerButton);
        signInButton = findViewById(R.id.signInButton);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
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

        TextInputLayout[] fields = {etEmail, etPassword, etConfirmPassword};

        boolean empty = false;

        for(TextInputLayout field : fields){
            if(field.getEditText().getText().toString().trim().isEmpty()){
                field.setError("Required");
                empty = true;
            } else{
                field.setError(null);
                empty = false;
            }
        }
        return empty;
    }

    private void registerButtonClicked(){

        if(areFieldsEmpty()){
            return;
        }


        String email = etEmail.getEditText().getText().toString().trim();
        String password = etPassword.getEditText().getText().toString().trim();
        String confirmPassword = etConfirmPassword.getEditText().getText().toString().trim();

        if(!confirmPassword.equals(password) ){
            etConfirmPassword.setError("Passwords do not match");
            return;
        }else{
            etConfirmPassword.setError(null);
        }

        Call<RegisterResponse> call = timeShareApi.register(new User(email, password));

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if(!response.isSuccessful()){
                    Snackbar.make(registerPageLayout, "Error: " + response.errorBody() + " Code: " + response.code(), BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }


                RegisterResponse registerResponse = response.body();

                if(registerResponse.getMessage().equals("user exists")){

                    etEmail.setError("User exists, sign in.");

                } else if(registerResponse.getMessage().equals("registered")){
                    etEmail.setError(null);

                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("registeredUser", (Parcelable) registerResponse.getUser());
                    startActivity(intent);

//                    Snackbar.make(loginPageLayout, "Registered successfully, sign in.", BaseTransientBottomBar.LENGTH_LONG).show();
                    Toast.makeText(RegisterActivity.this, "Registered successfully, sign in.", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Snackbar.make(registerPageLayout,t.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });

    }

    private void signInButtonClicked(){

        Intent intent = new Intent(this, LoginActivity.class);

        String registeredUserEmail = etEmail.getEditText().getText().toString().trim();
        if(!registeredUserEmail.isEmpty()){
            intent.putExtra("registeredUserEmail", registeredUserEmail);
        }
        startActivity(intent);
    }

//    private void verifyLoginAsyncTask(View v){
//
//    }
//
//    private class LoginAsyncTask extends AsyncTask<Void, Void, Void>{
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            loadingDialog.startLoadingDialog();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            Call<LoginResponse> call = timeShareApi.login(new User(email, password));
//
//            call.enqueue(new Callback<LoginResponse>() {
//                @Override
//                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//
//                    if(!response.isSuccessful()){
//                        Snackbar.make(loginPageLayout, "Error: " + response.code(), BaseTransientBottomBar.LENGTH_LONG).show();
//                        return;
//                    }
//
//
//                    LoginResponse loginResponse = response.body();
//
//                    if(loginResponse.getMessage().equals("logged in")){
//
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//
//
//                        intent.putExtra("user", (Parcelable) loginResponse.getUser());
//                        startActivity(intent);
//
//                    } else if(loginResponse.getMessage().equals("incorrect password")){
//                        etPassword.setError("Incorrect password");
//
//                    } else if(loginResponse.getMessage().equals("user does not exist")){
//                        etEmail.setError("User not found, register.");
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<LoginResponse> call, Throwable t) {
//                    Snackbar.make(loginPageLayout,t.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
//                }
//            });
//            return null;
//        }
//    }
}