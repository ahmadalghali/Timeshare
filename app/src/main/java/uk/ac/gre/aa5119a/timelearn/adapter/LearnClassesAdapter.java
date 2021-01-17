package uk.ac.gre.aa5119a.timelearn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.model.learn.LearnClass;
import uk.ac.gre.aa5119a.timelearn.model.listing.Subject;
import uk.ac.gre.aa5119a.timelearn.model.listing.TeacherListing;

public class LearnClassesAdapter extends RecyclerView.Adapter<LearnClassesAdapter.LearnClassViewHolder> {

    List<TeacherListing> classes = new ArrayList<>();

    private OnClassClickedListener onClassClickedListener;

    public void setOnClassClickedListener(OnClassClickedListener onClassClickedListener){
        this.onClassClickedListener = onClassClickedListener;
    }


    public LearnClassesAdapter(){

    }


    @NonNull
    @Override
    public LearnClassesAdapter.LearnClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_listing,parent
        ,false);

        return new LearnClassViewHolder(itemView, onClassClickedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull LearnClassesAdapter.LearnClassViewHolder holder, int position) {

//        holder.catIcon.setImageResource(categories.get(position).getIcon());
//        holder.catTitle.setText(categories.get(position).getTitle());
        TeacherListing learnClass = classes.get(position);

        holder.ivTeacherPhoto.setImageResource(R.drawable.user_photo); // hard coded
        holder.tvTitle.setText(learnClass.getTitle());
        holder.tvTitle.setText(classes.get(position).getTitle());
        holder.tvTitle.setText(classes.get(position).getTitle());

        if(!learnClass.getTeachingStyles().contains(1)){
            holder.ivInPerson.setVisibility(View.GONE);
        }
        if(!learnClass.getTeachingStyles().contains(2)){
            holder.ivOnline.setVisibility(View.GONE);
        }

    }
    @Override
    public int getItemCount() {
        return classes.size();
    }

    public void setClasses(List<TeacherListing> classes) {
        this.classes = classes;
        notifyDataSetChanged();
    }

    public class LearnClassViewHolder extends RecyclerView.ViewHolder {

        ImageView ivTeacherPhoto;
        RatingBar rbUserRating;
        TextView tvTitle;
        TextView tvTimeCredits;
        ImageView ivInPerson;
        ImageView ivOnline;
        TextView tvCity;




        public LearnClassViewHolder(@NonNull View itemView, OnClassClickedListener onClassClickedListener) {
            super(itemView);

            ivTeacherPhoto = itemView.findViewById(R.id.ivTeacherPhoto);
            rbUserRating = itemView.findViewById(R.id.rbTeacherRating);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTimeCredits = itemView.findViewById(R.id.tvTimeCredits);
            ivInPerson = itemView.findViewById(R.id.ivInPerson);
            ivOnline = itemView.findViewById(R.id.ivOnline);
            tvCity = itemView.findViewById(R.id.tvCity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if(onCategoryClickedListener != null){
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION){
//                            onCategoryClickedListener.onCategoryClicked(position);
//                        }
//                    }
                }
            });

        }
    }

    public interface OnClassClickedListener {

        void onClassClicked(int position);

    }

}
