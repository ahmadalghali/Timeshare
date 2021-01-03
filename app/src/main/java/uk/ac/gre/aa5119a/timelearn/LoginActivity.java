package uk.ac.gre.aa5119a.timelearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.web.LoginResponse;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private Button signInButton;
    private TextView registerButton;

    private Gson gson;
    private TimeShareApi timeShareApi;

    private LinearLayout loginPageLayout;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        assignGlobalVariables();
        initListeners();

        initRetrofit();

        if(getIntent().hasExtra("registeredUserEmail")){
            String registeredUserEmail = getIntent().getStringExtra("registeredUserEmail");

            etEmail.getEditText().setText(registeredUserEmail);
        }
    }

    private void assignGlobalVariables(){
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        signInButton = findViewById(R.id.signInButton);
        registerButton = findViewById(R.id.registerButton);
        loginPageLayout = findViewById(R.id.loginPageLayout);
        loadingDialog = new LoadingDialog(LoginActivity.this);
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

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);


                    intent.putExtra("user", (Parcelable) loginResponse.getUser());
                    startActivity(intent);

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

        Intent intent = new Intent(this, RegisterActivity.class);

        String email = etEmail.getEditText().getText().toString().trim();
        if(!email.isEmpty()){
            intent.putExtra("newUser", email);
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