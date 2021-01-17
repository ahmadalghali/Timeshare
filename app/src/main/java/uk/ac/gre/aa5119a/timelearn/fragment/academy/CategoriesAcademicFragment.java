package uk.ac.gre.aa5119a.timelearn.fragment.academy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.adapter.CategoriesAdapter;
import uk.ac.gre.aa5119a.timelearn.model.listing.Subject;
import uk.ac.gre.aa5119a.timelearn.viewmodel.AcademyViewModel;

import static uk.ac.gre.aa5119a.timelearn.MainActivity.navHostFragment;

public class CategoriesAcademicFragment extends Fragment {

    private RecyclerView recyclerview;

    private View view;

    private List<Subject> categories;

    private CategoriesAdapter categoriesAdapter;

    private ImageView backBtn;

    private AcademyViewModel academyViewModel;

    private TextView tvEduType;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_academic_categories, container, false);

        assignGlobalVariables();
        initRecyclerView();
        setListeners();

        return view;
    }


    private void assignGlobalVariables(){


        backBtn = view.findViewById(R.id.backBtn);
        academyViewModel = new ViewModelProvider(getActivity()).get(AcademyViewModel.class);
        tvEduType = view.findViewById(R.id.tvEduType);

        tvEduType.setText(academyViewModel.getEducationType().getValue());

    }

    private void setListeners(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesFragment()).commit();
                NavController navController = navHostFragment.getNavController();
                NavDirections action = CategoriesAcademicFragmentDirections.actionCategoriesAcademicFragmentToCategoriesFragment();
                navController.navigate(action);
            }
        });

    }

    private void initRecyclerView(){
        recyclerview = view.findViewById(R.id.recyclerview);
        categories = new ArrayList<>();
        categories.add(new Subject(1,R.drawable.ic_maths, "Maths"));
        categories.add(new Subject(2,R.drawable.ic_chemistry, "Chemistry"));
        categories.add(new Subject(3,R.drawable.ic_physics, "Physics"));
        categories.add(new Subject(4,R.drawable.ic_computer, "Computing"));
        categories.add(new Subject(5,R.drawable.ic_english, "English"));


        categoriesAdapter = new CategoriesAdapter();
        categoriesAdapter.setCategories(categories);


        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        recyclerview.setLayoutManager(gridLayoutManager);

        categoriesAdapter.setOnCategoryClickedListener(new CategoriesAdapter.OnCategoryClickedListener() {
            @Override
            public void onCategoryClicked(int position) {

                Subject subject = categories.get(position);

                academyViewModel.setSubjectChosen(subject);

                if(academyViewModel.getEducationType().getValue().toLowerCase().equals("learn")){

                    NavController navController = navHostFragment.getNavController();
                    NavDirections action = CategoriesAcademicFragmentDirections.actionCategoriesAcademicFragmentToTeachSubjectDetailsFragment();
                    navController.navigate(action);

                }else if(academyViewModel.getEducationType().getValue().toLowerCase().equals("teach")){

                    NavController navController = navHostFragment.getNavController();
                    NavDirections action = CategoriesAcademicFragmentDirections.actionCategoriesAcademicFragmentToTeachSubjectDetailsFragment();
                    navController.navigate(action);

                }

//                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TeachSubjectDetailsFragment()).commit();
            }
        });

        recyclerview.setAdapter(categoriesAdapter);

    }


}
