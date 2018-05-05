package fesb.papac.marin.augmented_reality_poi.Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Connection {

    Context context;

    public Connection(Context context) {
        this.context = context;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void checkConnection() {
        if (isOnline()) {
            Toast.makeText(context, "You are connected to Internet", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "You are not connected to Internet", Toast.LENGTH_LONG).show();
        }
    }
}