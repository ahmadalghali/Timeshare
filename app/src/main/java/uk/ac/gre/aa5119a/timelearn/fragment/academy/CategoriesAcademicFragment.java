package uk.ac.gre.aa5119a.timelearn.fragment.academy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.adapter.CategoriesAdapter;
import uk.ac.gre.aa5119a.timelearn.model.ui.Category;

public class CategoriesAcademicFragment extends Fragment {

    private RecyclerView recyclerview;

    private View view;

    private List<Category> categories;

    private CategoriesAdapter categoriesAdapter;

    private ImageView backBtn;


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

    }

    private void setListeners(){
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CategoriesFragment()).commit();

            }
        });

    }

    private void initRecyclerView(){
        recyclerview = view.findViewById(R.id.recyclerview);
        categories = new ArrayList<>();
        categories.add(new Category(R.drawable.ic_maths, "Maths"));
        categories.add(new Category(R.drawable.ic_chemistry, "Chemistry"));
        categories.add(new Category(R.drawable.ic_physics, "Physics"));
        categories.add(new Category(R.drawable.ic_computer, "Computing"));
        categories.add(new Category(R.drawable.ic_english, "English"));


        categoriesAdapter = new CategoriesAdapter();
        categoriesAdapter.setCategories(categories);


        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

        recyclerview.setLayoutManager(gridLayoutManager);
        recyclerview.setAdapter(categoriesAdapter);
    }


}
