package fesb.papac.marin.augmented_reality_poi.View;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import fesb.papac.marin.augmented_reality_poi.Controllers.PointOfInterestController;
import fesb.papac.marin.augmented_reality_poi.Model.Geometry;
import fesb.papac.marin.augmented_reality_poi.Model.HttpHelper;
import fesb.papac.marin.augmented_reality_poi.Model.LocationJSN;
import fesb.papac.marin.augmented_reality_poi.Model.PointOfInterest;
import fesb.papac.marin.augmented_reality_poi.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static fesb.papac.marin.augmented_reality_poi.View.ViewMain.DEBUG_TAG;

public class MapActivity extends AppCompatActivity implements LocationListener {

    public static final String EXTRA_POSITION = "position";
    public static List<PointOfInterest> pointOfInterests = new ArrayList<>();
    List<PointOfInterest> listOfPOI;
    private LocationManager locationManager = null;
    private String type;
    private Context context;
    private Location lastLocation;
    double endLat, endLong, endAlti;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private GoogleMap mMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        context = getApplicationContext();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        startGPS();

        PointOfInterestController poiController = new PointOfInterestController(getApplicationContext());
        PointOfInterest poi = new PointOfInterest();
        LocationJSN locationJSN = new LocationJSN();
        locationJSN.setLng(endLong);
        locationJSN.setLat(endLat);
        Geometry geo = new Geometry();
        geo.setLocation(locationJSN);
        poi.setGeometry(geo);
        poi.setType(getIntent().getStringExtra(EXTRA_POSITION));



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            LatLng myPlace = new LatLng(endLat, endLong);
            mMap.addMarker(new MarkerOptions()
                    .position(myPlace)
                    .title("You are here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 14));

            poiController.getPlaces(poi).enqueue(new Callback<HttpHelper>() {
                @Override
                public void onResponse(Call<HttpHelper> call, Response<HttpHelper> response) {
                    if (response.body() != null) {
                        List<PointOfInterest> results = response.body().getResults();
                        for (PointOfInterest point : results) {
                            LatLng pointPlace = new LatLng(point.getLatitude(), point.getLongitude());
                            mMap.addMarker(new MarkerOptions()
                                    .position(pointPlace)
                                    .title(point.getName())).showInfoWindow();
                        }
                    }
                }

                @Override
                public void onFailure(Call<HttpHelper> call, Throwable t) {

                }
            });

        });
    }

    private void startGPS() {

        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Criteria criteria = new Criteria();
        // criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // while we want fine accuracy, it's unlikely to work indoors where we
        // do our testing. :)
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);

        String best = locationManager.getBestProvider(criteria, true);

        Log.v(DEBUG_TAG, "Best provider: " + best);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        locationManager.requestLocationUpdates(best, 50, 0, this);

        if (locationManager != null) {
            lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (lastLocation == null) {
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        if (lastLocation != null) {
            endLat = lastLocation.getLatitude();
            endLong = lastLocation.getLongitude();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        endLat = location.getLatitude();
        endLong = location.getLongitude();
        endAlti = location.getAltitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
