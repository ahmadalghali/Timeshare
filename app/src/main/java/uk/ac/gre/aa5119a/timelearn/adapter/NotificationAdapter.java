package uk.ac.gre.aa5119a.timelearn.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gre.aa5119a.timelearn.MainActivity;
import uk.ac.gre.aa5119a.timelearn.R;
import uk.ac.gre.aa5119a.timelearn.model.notification.Notification;
import uk.ac.gre.aa5119a.timelearn.model.notification.NotificationClassBooking;
import uk.ac.gre.aa5119a.timelearn.model.notification.NotificationClassConfirmation;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    List<Notification> notifications = new ArrayList<>();
    Context context;

    NotificationClickListener notificationClickListener;

    public NotificationAdapter(Context context, NotificationClickListener notificationClickListener) {
        this.context = context;
        this.notificationClickListener = notificationClickListener;

    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        if(viewType == 2){
        return new NotificationClassBookingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_class_booking_request, parent, false));
        } else  {
            return new NotificationClassBookingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_class_booking_request, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        Gson gson = new Gson();

        if (getItemViewType(position) == 2) {

            JsonObject jsonObject = gson.toJsonTree(notifications.get(position).getNotification()).getAsJsonObject();
            NotificationClassBooking notificationClassBooking = gson.fromJson(jsonObject, NotificationClassBooking.class);

            ((NotificationClassBookingViewHolder) holder).setNotificationData(notificationClassBooking);


        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return notifications.get(position).getType();
    }

    class NotificationClassBookingViewHolder extends RecyclerView.ViewHolder {

        TextView tvNotificationMessage;
        TextView tvSubjectTitle;
        TextView tvHours;
        TextView tvTimeCreditPrice;
        TextView tvUserName;
        TextView btnReject;
        TextView btnAccept;
        ImageView ivUserPhoto;
        ImageView ivSubjectImage;

        NotificationClassBookingViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvSubjectTitle = itemView.findViewById(R.id.tvSubjectTitle);
            tvHours = itemView.findViewById(R.id.tvHours);
            tvTimeCreditPrice = itemView.findViewById(R.id.tvTimeCreditPrice);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnReject = itemView.findViewById(R.id.btnReject);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            ivUserPhoto = itemView.findViewById(R.id.ivUserPhoto);
            ivSubjectImage = itemView.findViewById(R.id.ivSubjectImage);

        }

        void setNotificationData(NotificationClassBooking notification) {

            tvNotificationMessage.setText("Incoming class request");
            tvSubjectTitle.setText(notification.getSubjectTitle());
            tvHours.setText(notification.getHours() + " Hrs");
            tvTimeCreditPrice.setText("" + notification.getTimeCreditPrice());
            tvUserName.setText(notification.getStudentName());

            if (notification.getStudentProfileImage() != null && !notification.getStudentProfileImage().isEmpty() && !notification.getStudentProfileImage().equals("")) {
                Picasso.get()
                        .load(notification.getStudentProfileImage())
                        .error(R.drawable.default_user_image)
                        .into(ivUserPhoto);
            }
            if (notification.getSubjectIconUrl() != null && !notification.getSubjectIconUrl().isEmpty() && !notification.getSubjectIconUrl().equals("")) {
                ivSubjectImage.setImageResource(context.getResources().getIdentifier(notification.getSubjectIconUrl(), "drawable", context.getPackageName()));
            }

            btnAccept.setOnClickListener(v -> {
                notificationClickListener.onAcceptButtonClicked(getAdapterPosition(), notification.getClassBooking().getId());
            });

            btnReject.setOnClickListener(v -> {
                notificationClickListener.onRejectButtonClicked(getAdapterPosition(),notification.getClassBooking().getId());

            });
        }


    }

    class NotificationClassConfirmedViewHolder extends RecyclerView.ViewHolder {

        TextView tvNotificationMessage;
        TextView tvSubjectTitle;
        TextView tvUserName;
        TextView btnViewClass;
        ImageView ivUserPhoto;
        ImageView ivSubjectImage;
        TextView tvClassDate;

        NotificationClassConfirmedViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvSubjectTitle = itemView.findViewById(R.id.tvSubjectTitle);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            btnViewClass = itemView.findViewById(R.id.btnViewClass);
            ivUserPhoto = itemView.findViewById(R.id.ivUserPhoto);
            ivSubjectImage = itemView.findViewById(R.id.ivSubjectImage);
            tvClassDate = itemView.findViewById(R.id.tvClassDate);


        }

        void setNotificationData(NotificationClassConfirmation notification) {

            tvNotificationMessage.setText("Class confirmed");
            tvSubjectTitle.setText(notification.getSubjectTitle());
            tvUserName.setText(notification.getTeacherName());
            if (notification.getTeacherProfileImageUrl() != null && !notification.getTeacherProfileImageUrl().isEmpty() && !notification.getTeacherProfileImageUrl().equals("")) {
                Picasso.get()
                        .load(notification.getTeacherProfileImageUrl())
                        .error(R.drawable.default_user_image)
                        .into(ivUserPhoto);
            }
            if (notification.getSubjectIconName() != null && !notification.getSubjectIconName().isEmpty() && !notification.getSubjectIconName().equals("")) {
                ivSubjectImage.setImageResource(context.getResources().getIdentifier(notification.getSubjectIconName(), "drawable", context.getPackageName()));
            }

            btnViewClass.setOnClickListener(v -> {
                notificationClickListener.onViewButtonClicked(getAdapterPosition(), notification.getClassId());
            });

        }


    }


    public interface NotificationClickListener {

        void onAcceptButtonClicked(int position,int classId);

        void onRejectButtonClicked(int position, int classId);

        void onViewButtonClicked(int position, int classId);
    }


}
