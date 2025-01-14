package oop.project.foodjar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityDetector {
    Context ctx;

    // Constructor
    public ConnectivityDetector(Context ctx){
        this.ctx = ctx;
    }

    //Check internet status
    public boolean checkConnectivityStatus(){
        ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        // boolean wifi=connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        // boolean internet=connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        assert connectivity != null;
        NetworkInfo internetInfo = connectivity.getActiveNetworkInfo();

        if ( internetInfo != null) {
            return true;
        }
        return false;
    }

    //Show alert
    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
