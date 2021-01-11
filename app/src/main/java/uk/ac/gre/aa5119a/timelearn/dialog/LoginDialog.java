package uk.ac.gre.aa5119a.timelearn.dialog;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

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

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.fragment.home.HomeFragmentLoggedIn;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;
import uk.ac.gre.aa5119a.timelearn.web.LoginResponse;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;

public class LoginDialog extends DialogFragment {

    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private Button signInButton;
    private TextView registerButton;

    private TimeShareApi timeShareApi;

    private FrameLayout mainActivityFrameLayout;

    private HomeViewModel homeViewModel;

    private  Dialog loginDialog;

    private View view;

    private static final String BLUE_PRESSED_COLOR = "#006fab";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
        mainActivityFrameLayout = view.findViewById(R.id.fragment_container);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
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
                showRegisterDialog();
            }
        });
    }


    private void showRegisterDialog(){
        RegisterDialog registerDialog = new RegisterDialog();
        registerDialog.setTargetFragment(getTargetFragment(), 1);
        dismiss();
        registerDialog.show(getActivity().getSupportFragmentManager(), "RegisterDialog");

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
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Error: " + response.code(), BaseTransientBottomBar.LENGTH_LONG).show();
//                    showSnackBar("Error: " + response.code());
                    return;
                }


                LoginResponse loginResponse = response.body();

                if(loginResponse.getMessage().equals("logged in")){

                    homeViewModel.setUser(loginResponse.getUser());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragmentLoggedIn()).commit();
                    dismiss();

                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Welcome " + loginResponse.getUser().getEmail(), BaseTransientBottomBar.LENGTH_LONG).show();
//                    showSnackBar("Welcome " + loginResponse.getUser().getEmail());

//                    Toast.makeText(getActivity(), "Welcome " + loginResponse.getUser().getEmail() , Toast.LENGTH_LONG).show();


                } else if(loginResponse.getMessage().equals("incorrect password")){
                    etPassword.setError("Incorrect password");

                } else if(loginResponse.getMessage().equals("user does not exist")){
                    etEmail.setError("User not found, register.");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Snackbar.make(getActivity().findViewById(android.R.id.content),t.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
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

//    private void showSnackBar(String message){
//        Snackbar snack = Snackbar.make(getActivity().findViewById(android.R.id.content),
//                "Your message", Snackbar.LENGTH_LONG);
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
//                snack.getView().getLayoutParams();
//        params.setMargins(0, 0, 0, 50);
//        snack.getView().setLayoutParams(params);
//        snack.setAnchorView(view);
//        snack.show();
//    }

}