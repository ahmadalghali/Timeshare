package uk.ac.gre.aa5119a.timelearn.dialog;

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
import android.widget.Toast;

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
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;
import uk.ac.gre.aa5119a.timelearn.web.RegisterResponse;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;

public class RegisterDialog extends DialogFragment {



    private HomeViewModel homeViewModel;

    private Dialog registerDialog;

    private View view;


    // login dialog
    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private TextInputLayout etConfirmPassword;

    private TextView signInButton;
    private Button registerButton;

    private TimeShareApi timeShareApi;

    private static final String BLUE_PRESSED_COLOR = "#006fab";

    private FrameLayout mainActivityFrameLayout;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_register, null);

        builder.setView(view);

        registerDialog = builder.create();

        registerDialog.setCanceledOnTouchOutside(true);
        registerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        assignGlobalVariables();
        setListeners();
        initRetrofit();


        return registerDialog;
    }

    private void assignGlobalVariables(){
         etEmail = view.findViewById(R.id.etEmail);
         etPassword = view.findViewById(R.id.etPassword);
         etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
         registerButton = view.findViewById(R.id.registerButton);
         signInButton = view.findViewById(R.id.signInButton);
        mainActivityFrameLayout = view.findViewById(R.id.fragment_container);

    }

    private void setListeners(){

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLoginDialog();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v, BLUE_PRESSED_COLOR);
                registerButtonClicked();
            }
        });
    }


    private void showLoginDialog(){
        LoginDialog loginDialog = new LoginDialog();
        loginDialog.setTargetFragment(getTargetFragment(), 1);
        dismiss();
        loginDialog.show(getActivity().getSupportFragmentManager(), "LoginDialog");
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

        etEmail.setError(null);
        etConfirmPassword.setError(null);
        etPassword.setError(null);

        if(areFieldsEmpty()){
            return;
        }


        String email = etEmail.getEditText().getText().toString().trim();
        String password = etPassword.getEditText().getText().toString().trim();
        String confirmPassword = etConfirmPassword.getEditText().getText().toString().trim();

        if(!confirmPassword.equals(password) ){
            etConfirmPassword.setError("Password does not match");
            return;
        }else{
            etConfirmPassword.setError(null);
        }

        Call<RegisterResponse> call = timeShareApi.register(new User(email, password));

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if(!response.isSuccessful()){
                    Snackbar.make(mainActivityFrameLayout, "Error: " + response.errorBody() + " Code: " + response.code(), BaseTransientBottomBar.LENGTH_LONG).show();
                    return;
                }


                RegisterResponse registerResponse = response.body();

                if(registerResponse.getMessage().equals("user exists")){

                    etEmail.setError("User exists, sign in.");

                } else if(registerResponse.getMessage().equals("registered")){
                    etEmail.setError(null);

//                    show registered toast message
                    showLoginDialog();
                    Toast.makeText(getActivity(), "Registered successfully, sign in.", Toast.LENGTH_LONG).show();


                    Snackbar.make(mainActivityFrameLayout, "Registered successfully, sign in.", BaseTransientBottomBar.LENGTH_LONG).show();
//                    Toast.makeText(RegisterActivity.this, "Registered successfully, sign in.", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

                Snackbar.make(mainActivityFrameLayout,t.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
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
