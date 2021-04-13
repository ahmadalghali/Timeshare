package uk.ac.gre.aa5119a.timelearn.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.model.LessonDTO;
import uk.ac.gre.aa5119a.timelearn.model.notification.Notification;
import uk.ac.gre.aa5119a.timelearn.model.notification.NotificationClassBooking;
import uk.ac.gre.aa5119a.timelearn.model.notification.NotificationClassConfirmation;

public class MyLessonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<LessonDTO> lessons = new ArrayList<>();
    Context context;

    LessonClickListener lessonClickListener;

    public MyLessonsAdapter(Context context, LessonClickListener lessonClickListener) {
        this.context = context;
        this.lessonClickListener = lessonClickListener;

    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new LessonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false));
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
        TextView btnJoinLesson;
        CircleImageView ivUserPhoto;
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
            btnJoinLesson = itemView.findViewById(R.id.btnJoinLesson);
            ivUserPhoto = itemView.findViewById(R.id.ivUserPhoto);
            ivSubjectImage = itemView.findViewById(R.id.ivSubjectImage);
            tvLessonStatus = itemView.findViewById(R.id.tvLessonStatus);
            tvLessonDate = itemView.findViewById(R.id.tvLessonDate);

        }

        void setLessonData(LessonDTO lesson) {

            tvNotificationMessage.setText("Upcoming Lesson");
            tvSubjectTitle.setText(lesson.getSubjectTitle());
            tvHours.setText(lesson.getHours() + " Hrs");
            tvTimeCreditPrice.setText("" + lesson.getLessonPrice());
            tvUserName.setText("Teacher: " + lesson.getTeacherFirstName());


            try {
                Picasso.get()
                        .load(lesson.getTeacherImage())
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
            } else if (lesson.getStatus().equalsIgnoreCase("Started")) {
                tvLessonStatus.setText(Html.fromHtml("Status: " + "<font color='#d49c11'>" + lesson.getStatus() + "</font>"));
            } else if (lesson.getStatus().equalsIgnoreCase("Ready")) {
                tvLessonStatus.setText(Html.fromHtml("Status: " + "<font color='#5bc75b'>" + lesson.getStatus() + "</font>"));
            } else if (lesson.getStatus().equalsIgnoreCase("Cancelled")) {
                tvLessonStatus.setText(Html.fromHtml("Status: " + "<font color='red'>" + lesson.getStatus() + "</font>"));
            }

            String formattedLessonDate = DateFormat.getDateInstance(DateFormat.FULL).format(lesson.getLessonDate());

            tvLessonDate.setText(formattedLessonDate);

            Date today = new Date(System.currentTimeMillis());
//            LocalDate today = LocalDate.now();
            if (!today.before(lesson.getLessonDate())  && lesson.getStatus().equalsIgnoreCase("started")) {
                btnJoinLesson.setVisibility(View.VISIBLE);
            } else {
                btnJoinLesson.setVisibility(View.INVISIBLE);
            }

            btnJoinLesson.setOnClickListener(v -> {
                lessonClickListener.onJoinButtonClicked(getAdapterPosition(), lesson.getId());
            });


        }


    }


    public interface LessonClickListener {

        void onJoinButtonClicked(int position, int lessonId);
    }


}
