package uk.ac.gre.aa5119a.timelearn.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import uk.ac.gre.aa5119a.timelearn.R;

public class LoadingDialog {

    private Activity activity;
    public AlertDialog dialog;
    private TextView tvMessage;

    public LoadingDialog(Activity activity) {


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.custom_dialog,null);
        builder.setView(view);

        tvMessage = view.findViewById(R.id.tvLoadingMessage);

        builder.setCancelable(false);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    public void setMessage(String message){
        this.tvMessage.setText(message);
    }

    public void startLoadingDialog(){
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
