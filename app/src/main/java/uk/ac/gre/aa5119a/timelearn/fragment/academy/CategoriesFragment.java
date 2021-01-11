package uk.ac.gre.aa5119a.timelearn.fragment.academy;

import android.graphics.Color;
import android.graphics.PorterDuff;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;

public class CategoriesFragment extends Fragment {

    private Button academicBtn;
    private Button sportsBtn;
    private Button hobbiesBtn;
    private Button languagesBtn;
    private TextView tvEducationType;

    private ImageView backButton;

    private View view;

    private AcademyViewModel academyViewModel;

    private static final String WHITE_PRESSED_COLOR = "#b3b3b3";


    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_categories, container, false);

        assignGlobalVariables();
        setListeners();

        return view;
    }

    private void assignGlobalVariables(){
        backButton = view.findViewById(R.id.backBtn);

        academicBtn = view.findViewById(R.id.btnAcademic);
        sportsBtn = view.findViewById(R.id.btnSports);
        hobbiesBtn = view.findViewById(R.id.btnHobbies);
        languagesBtn = view.findViewById(R.id.btnLanguages);
        tvEducationType = view.findViewById(R.id.tvEduType);

        academyViewModel = new ViewModelProvider(requireActivity()).get(AcademyViewModel.class);

        switch (academyViewModel.getEducationType().getValue()){
            case 1: tvEducationType.setText("Learn");
                break;
            case 2: tvEducationType.setText("Teach");
                break;
        }

    }

    private void setListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AcademyFragment()).commit();
            }
        });

        academicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v, WHITE_PRESSED_COLOR);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesAcademicFragment()).commit();

            }
        });

        languagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v, WHITE_PRESSED_COLOR);

            }
        });
        sportsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v, WHITE_PRESSED_COLOR);

            }
        });

        hobbiesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEffect(v, WHITE_PRESSED_COLOR);

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


}
