package info.weigandt.goalacademy.classes;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;

import info.weigandt.goalacademy.R;

public class AlertDialogFactory {
    public static AlertDialog createFailDialog(String message, FragmentActivity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String mainMessage =  activity.getResources().getString(R.string.fail_message);
        mainMessage += " " + message + ".";
        builder.setMessage(mainMessage)
                .setTitle(R.string.fail_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        return builder.create();
    }
    public static AlertDialog createFailDialog2(String message, FragmentActivity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        String mainMessage =  activity.getResources().getString(R.string.fail_message);
        mainMessage += " " + message + ".";
        builder.setMessage(mainMessage)
                .setTitle(R.string.fail_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        return builder.create();
    }
}
