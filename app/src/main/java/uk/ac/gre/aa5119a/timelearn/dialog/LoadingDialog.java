package uk.ac.gre.aa5119a.timelearn.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import uk.ac.gre.aa5119a.timelearn.R;

public class LoadingDialog {

    Activity activity;
    AlertDialog dialog;


    public LoadingDialog(Activity activity){
        this.activity = activity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.custom_dialog,null));

        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

    }

    public void dismissDialog(){

        dialog.dismiss();
    }
}