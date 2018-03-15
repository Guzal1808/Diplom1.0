package fesb.papac.marin.augmented_reality_poi.View;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


public class SplashPermissionsActivity extends Activity {

    private int timeoutMillis = 0;

    private long startTimeMillis = 0;

    private static final int PERMISSIONS_REQUEST = 1234;

    private static final Random random = new Random();

    private TextView textView = null;
    private static final int textViewID = View.generateViewId();


    public int getTimeoutMillis() {
        return timeoutMillis;
    }


    public Class getNextActivityClass() {
        return CardActivity.class;
    }

    public String[] getRequiredPermissions() {
        String[] permissions = null;
        try {
            permissions = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (permissions == null) {
            return new String[0];
        } else {
            return permissions.clone();
        }
    }

    @TargetApi(23)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));


        textView = new TextView(this);
        textView.setTextSize(50);
        textView.setId(textViewID);
        textView.setText("Waiting for permissions...");
        mainLayout.addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


        int off = 128;
        int rest = 256 - off;
        int color = Color.argb(255, off + random.nextInt(rest), off + random.nextInt(rest), off + random.nextInt(rest));
        mainLayout.setBackgroundColor(color);


        setContentView(mainLayout);
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        } else {
            startNextActivity();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            checkPermissions();
        }
    }

    private void startNextActivity() {
        runOnUiThread(() -> textView.setText("Permissions granted..."));
        long delayMillis = getTimeoutMillis() - (System.currentTimeMillis() - startTimeMillis);
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashPermissionsActivity.this, CardActivity.class));
            finish();
        }, delayMillis);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        String[] ungrantedPermissions = requiredPermissionsStillNeeded();
        if (ungrantedPermissions.length == 0) {
            startNextActivity();
        } else {
            requestPermissions(ungrantedPermissions, PERMISSIONS_REQUEST);
        }
    }

    @TargetApi(23)
    private String[] requiredPermissionsStillNeeded() {

        Set<String> permissions = new HashSet<String>();
        for (String permission : getRequiredPermissions()) {
            permissions.add(permission);
        }
        for (Iterator<String> i = permissions.iterator(); i.hasNext(); ) {
            String permission = i.next();
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(SplashPermissionsActivity.class.getSimpleName(),
                        "Permission: " + permission + " already granted.");
                i.remove();
            } else {
                Log.d(SplashPermissionsActivity.class.getSimpleName(),
                        "Permission: " + permission + " not yet granted.");
            }
        }
        return permissions.toArray(new String[permissions.size()]);
    }
}
