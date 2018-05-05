package fesb.papac.marin.augmented_reality_poi.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import fesb.papac.marin.augmented_reality_poi.Helper.Connection;
import fesb.papac.marin.augmented_reality_poi.R;

public class MainActivity extends AppCompatActivity {

    private ViewMain Content;
    public static final String EXTRA_POSITION = "position";
    Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connection = new Connection(getApplicationContext());
        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showSettingAlert();
        }
        if (!connection.isOnline()) {
            showAlert();
        }
            FrameLayout ViewPane2 = (FrameLayout) findViewById(R.id.ar_view_pane_2);
            FrameLayout ViewPane3 = (FrameLayout) findViewById(R.id.ar_view_pane_3);

            CameraView cameraView1 = new CameraView(this);
            ViewPane3.addView(cameraView1);

            Content = new ViewMain(getApplicationContext(), null, getIntent().getStringExtra(EXTRA_POSITION));

            ViewPane2.addView(Content);
    }

    @Override
    protected void onPause() {
        Content.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Content.onResume();
        super.onResume();
    }

    public void showSettingAlert() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS Not Enabled");

        alertDialog.setMessage("Do you wants to turn On GPS");


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = alertDialog.create();
        alert11.show();

    }
    private void showAlert() {
        if (!connection.isOnline()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Internet connection is Not Enabled")
                    .setMessage("Пожалуйста проверьте интернет подключение и повторите снова")
                    .setPositiveButton("Проверить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Выйти",
                            (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }


}
