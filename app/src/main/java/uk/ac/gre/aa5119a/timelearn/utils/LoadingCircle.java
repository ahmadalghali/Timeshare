package uk.ac.gre.aa5119a.timelearn.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import uk.ac.gre.aa5119a.timelearn.R;


public class LoadingCircle {

    public AlertDialog dialog;


    public LoadingCircle(Activity activity){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        View view = inflater.inflate(R.layout.loading_circle,null);
        builder.setView(view);


        builder.setCancelable(false);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    public void startLoading(){
        dialog.show();
    }
    public void dismiss(){
        dialog.dismiss();
    }
}
