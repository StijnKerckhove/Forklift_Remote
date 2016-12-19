package stijnkerckhove.forklift_remote;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by User on 17/12/2016.
 */

public class DialogController {
    private Context context;

    public DialogController(Context context) {
        this.context = context;
    }

    public void showUnableToConnectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Unable to connect to device")
                .setTitle("Connection error");

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
