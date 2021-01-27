package uk.ac.gre.aa5119a.timelearn.adapter;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.collection.LLRBNode;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.model.listing.TeacherListing;
import uk.ac.gre.aa5119a.timelearn.model.listing.TeachingStyle;
import uk.ac.gre.aa5119a.timelearn.web.response.TeacherListingResponse;

public class LearnClassesAdapter extends RecyclerView.Adapter<LearnClassesAdapter.LearnClassViewHolder> {

    List<TeacherListingResponse> responseList = new ArrayList<>();


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


        TeacherListingResponse response = responseList.get(position);

        TeacherListing _class = response.getListing();

        if(!response.getUser().getProfileImageUrl().equals(null) && !response.getUser().getProfileImageUrl().equals("") && response.getUser().getProfileImageUrl() != null ){
            Picasso.get()
                    .load(response.getUser().getProfileImageUrl())
                    .placeholder(R.drawable.ic_account)

                    .into(holder.ivTeacherPhoto);
        }else{

            holder.ivTeacherPhoto.setImageResource(R.drawable.default_user_image);

        }


        holder.tvTitle.setText(_class.getTitle());
        holder.tvCity.setText(response.getUser().getCity());
        holder.tvTimeRate.setText(String.valueOf(_class.getTimeRate()));
        holder.rbUserRating.setRating((float) response.getUser().getRatingScore());

        if(!_class.getTeachingStyleIds().contains(1)){
            holder.ivInPerson.setVisibility(View.GONE);
        }
        if(!_class.getTeachingStyleIds().contains(2)){
            holder.ivOnline.setVisibility(View.GONE);
        }

    }
    @Override
    public int getItemCount() {
        return responseList.size();
    }


    public void setResponseList(List<TeacherListingResponse> responseList){
        this.responseList = responseList;
        notifyDataSetChanged();

    }

    public class LearnClassViewHolder extends RecyclerView.ViewHolder {

        ImageView ivTeacherPhoto;
        RatingBar rbUserRating;
        TextView tvTitle;
        TextView tvTimeRate;
        ImageView ivInPerson;
        ImageView ivOnline;
        TextView tvCity;

        public LearnClassViewHolder(@NonNull View itemView, OnClassClickedListener onClassClickedListener) {
            super(itemView);



            ivTeacherPhoto = itemView.findViewById(R.id.ivTeacherPhoto);
            rbUserRating = itemView.findViewById(R.id.rbTeacherRating);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTimeRate = itemView.findViewById(R.id.tvTimeRate);
            ivInPerson = itemView.findViewById(R.id.ivInPerson);
            ivOnline = itemView.findViewById(R.id.ivOnline);
            tvCity = itemView.findViewById(R.id.tvCity);

            rbUserRating.setIsIndicator(true);
//


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ConstraintLayout listItemLayout = itemView.findViewById(R.id.listItemLayout);
                    listItemLayout.setBackgroundColor(Color.parseColor("#dbdbdb"));

                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 300ms
                            listItemLayout.setBackgroundColor(Color.TRANSPARENT);

                        }
                    }, 3000);


                    if(onClassClickedListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onClassClickedListener.onClassClicked(position);
                        }
                    }
                }
            });

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ConstraintLayout listItemLayout = itemView.findViewById(R.id.listItemLayout);
//                    listItemLayout.setBackgroundColor(Color.parseColor("#dbdbdb"));
//
//                    final Handler handler = new Handler(Looper.getMainLooper());
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            //Do something after 300ms
//                            listItemLayout.setBackgroundColor(Color.TRANSPARENT);
//
//                        }
//                    }, 100);
//
//
////                    if(onCategoryClickedListener != null){
////                        int position = getAdapterPosition();
////                        if (position != RecyclerView.NO_POSITION){
////                            onCategoryClickedListener.onCategoryClicked(position);
////                        }
////                    }
//                }
//            });

        }
    }

    public interface OnClassClickedListener {

        void onClassClicked(int position);

    }

}
