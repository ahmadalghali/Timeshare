package uk.ac.gre.aa5119a.timelearn.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.dialog.LoginDialog;
import uk.ac.gre.aa5119a.timelearn.dialog.LoginDialog2;
import uk.ac.gre.aa5119a.timelearn.dialog.RegisterDialog;
import uk.ac.gre.aa5119a.timelearn.model.User;
import uk.ac.gre.aa5119a.timelearn.viewmodel.HomeViewModel;
import uk.ac.gre.aa5119a.timelearn.web.LoginResponse;
import uk.ac.gre.aa5119a.timelearn.web.TimeShareApi;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ImageView profileButton;

    private View view;

    private TimeShareApi timeShareApi;
    private ConstraintLayout homeFragmentLayout;

    private LoginDialog loginDialog;
    private RegisterDialog registerDialog;

    private static final String BLUE_PRESSED_COLOR = "#006fab";
    private static final String WHITE_PRESSED_COLOR = "#808080";


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
        profileButton = view.findViewById(R.id.profileButton);
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

        loginDialog = new LoginDialog();
//
        loginDialog.setTargetFragment(getTargetFragment(), 1);
//
        loginDialog.show(getActivity().getSupportFragmentManager(), "LoginDialog");
    }



//    private void showRegisterDialog(){
//
//        registerDialog = new RegisterDialog(view.getContext());
//
//        registerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        registerDialog.show();
//    }


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
                        v.getBackground().setColorFilter(Color.parseColor(hexColor),PorterDuff.Mode.SRC_ATOP);
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
