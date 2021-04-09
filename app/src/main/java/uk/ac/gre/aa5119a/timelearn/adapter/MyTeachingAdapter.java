package uk.ac.gre.aa5119a.timelearn.adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.model.LessonDTO;

public class MyTeachingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<LessonDTO> lessons = new ArrayList<>();
    Context context;

    LessonClickListener lessonClickListener;

    public MyTeachingAdapter(Context context, LessonClickListener lessonClickListener) {
        this.context = context;
        this.lessonClickListener = lessonClickListener;

    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new LessonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        LessonDTO lesson = lessons.get(position);
        ((LessonViewHolder) holder).setLessonData(lesson);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public void setLessons(List<LessonDTO> lessons) {
        this.lessons = lessons;
        notifyDataSetChanged();
    }


    class LessonViewHolder extends RecyclerView.ViewHolder {

        TextView tvNotificationMessage;
        TextView tvSubjectTitle;
        TextView tvHours;
        TextView tvTimeCreditPrice;
        TextView tvUserName;
        TextView btnStartClass;
        ImageView ivUserPhoto;
        ImageView ivSubjectImage;
        TextView tvLessonStatus;
        TextView tvLessonDate;

        LessonViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvSubjectTitle = itemView.findViewById(R.id.tvSubjectTitle);
            tvHours = itemView.findViewById(R.id.tvHours);
            tvTimeCreditPrice = itemView.findViewById(R.id.tvTimeCreditPrice);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnStartClass = itemView.findViewById(R.id.btnStartClass);
            ivUserPhoto = itemView.findViewById(R.id.ivUserPhoto);
            ivSubjectImage = itemView.findViewById(R.id.ivSubjectImage);
            tvLessonStatus = itemView.findViewById(R.id.tvLessonStatus);
            tvLessonDate = itemView.findViewById(R.id.tvLessonDate);

        }

        void setLessonData(LessonDTO lesson) {

//            tvNotificationMessage.setText("Upcoming Lesson");
            tvSubjectTitle.setText(lesson.getSubjectTitle());
            tvHours.setText(lesson.getHours() + " Hrs");
            tvTimeCreditPrice.setText("" + lesson.getLessonPrice());
            tvUserName.setText("Student: "  +  lesson.getStudentFirstName());


            try {
                Picasso.get()
                        .load(lesson.getStudentImage())
                        .error(R.drawable.default_user_image)
                        .into(ivUserPhoto);
            } catch (Exception e) {
                Picasso.get()
                        .load(R.drawable.default_user_image)
                        .into(ivUserPhoto);
            }

            try {
                ivSubjectImage.setImageResource(context.getResources().getIdentifier(lesson.getSubjectIconUrl(), "drawable", context.getPackageName()));

            } catch (Exception e) {

            }

            if (lesson.getStatus().equalsIgnoreCase("not started")) {
                tvLessonStatus.setText(Html.fromHtml("Status: " + "<font color='grey'>" + lesson.getStatus() + "</font>"));
            } else if (lesson.getStatus().equalsIgnoreCase("started")) {
                tvLessonStatus.setText(Html.fromHtml("Status: " + "<font color='amber'>" + lesson.getStatus() + "</font>"));
            } else if (lesson.getStatus().equalsIgnoreCase("ready")) {
                tvLessonStatus.setText(Html.fromHtml("Status: " + "<font color='green'>" + lesson.getStatus() + "</font>"));
            } else if (lesson.getStatus().equalsIgnoreCase("cancelled")) {
                tvLessonStatus.setText(Html.fromHtml("Status: " + "<font color='red'>" + lesson.getStatus() + "</font>"));
            }

            String formattedLessonDate = DateFormat.getDateInstance(DateFormat.FULL).format(lesson.getLessonDate());

            tvLessonDate.setText(formattedLessonDate);

            Date today = new Date(System.currentTimeMillis());

            if (!today.before(lesson.getLessonDate())) {
                btnStartClass.setVisibility(View.VISIBLE);
            } else {
                btnStartClass.setVisibility(View.INVISIBLE);
            }

            btnStartClass.setOnClickListener(v -> {
                lessonClickListener.onStartClassButtonClicked(getAdapterPosition(), lesson.getId());
            });


        }


    }


    public interface LessonClickListener {

        void onStartClassButtonClicked(int position, int lessonId);
    }


}
