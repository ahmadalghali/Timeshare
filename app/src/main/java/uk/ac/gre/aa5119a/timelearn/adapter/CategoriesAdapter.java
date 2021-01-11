package uk.ac.gre.aa5119a.timelearn.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.model.ui.Category;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

     List<Category> categories = new ArrayList<>();


    public CategoriesAdapter(){
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);

        return new CategoriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdapter.CategoriesViewHolder holder, int position) {

        holder.catIcon.setImageResource(categories.get(position).getIcon());
        holder.catTitle.setText(categories.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {

        ImageView catIcon;
        TextView catTitle;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            catIcon = itemView.findViewById(R.id.cat_icon);
            catTitle = itemView.findViewById(R.id.cat_title);


        }
    }

}
