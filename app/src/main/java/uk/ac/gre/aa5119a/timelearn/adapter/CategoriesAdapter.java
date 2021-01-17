package uk.ac.gre.aa5119a.timelearn.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.model.listing.Subject;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

     List<Subject> categories = new ArrayList<>();

    private OnCategoryClickedListener onCategoryClickedListener;

    public void setOnCategoryClickedListener(OnCategoryClickedListener onCategoryClickedListener) {
        this.onCategoryClickedListener = onCategoryClickedListener;
    }


    public CategoriesAdapter(){
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);

        return new CategoriesViewHolder(itemView, onCategoryClickedListener);
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

    public void setCategories(List<Subject> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {

        ImageView catIcon;
        TextView catTitle;

        public CategoriesViewHolder(@NonNull View itemView,OnCategoryClickedListener onCategoryClickedListener) {
            super(itemView);
            catIcon = itemView.findViewById(R.id.cat_icon);
            catTitle = itemView.findViewById(R.id.cat_title);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(onCategoryClickedListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onCategoryClickedListener.onCategoryClicked(position);
                        }
                    }
                }
            });

        }
    }



    public interface OnCategoryClickedListener {

        void onCategoryClicked(int position);

    }

}
