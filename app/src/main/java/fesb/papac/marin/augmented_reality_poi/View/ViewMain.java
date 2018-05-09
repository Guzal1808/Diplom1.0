package fesb.papac.marin.augmented_reality_poi.View;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import fesb.papac.marin.augmented_reality_poi.Controllers.PointOfInterestController;
import fesb.papac.marin.augmented_reality_poi.Helper.Connection;
import fesb.papac.marin.augmented_reality_poi.Helper.PaintUtils;
import fesb.papac.marin.augmented_reality_poi.Model.Geometry;
import fesb.papac.marin.augmented_reality_poi.Model.LocationJSN;
import fesb.papac.marin.augmented_reality_poi.Model.PointOfInterest;
import fesb.papac.marin.augmented_reality_poi.R;

public class ViewMain extends View implements SensorEventListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    double canvasWidth, canvasHeight;
    float compasBearing, POIBearing;
    float radarRange = 600;
    int tries = 0;

    public static List<PointOfInterest> pointOfInterests = new ArrayList<>();

    public static final String DEBUG_TAG = "OverlayView Log";

    private final Context context;
    private Handler handler;

    private LocationManager locationManager = null;
    private SensorManager sensors = null;

    private Location lastLocation;
    private float[] lastAccelerometer, lastGyroscope, lastCompass, lastRotationVector, lastGameRotationVector, lastGravity, lastLinearAcc;
    double endLat, endLong, endAlti;
    boolean gps_enabled = false;
    boolean network_enabled = false;

    float textHeight, textOffset;
    int textlength, POIHight;

    private float verticalFOV, horizontalFOV;

    private boolean isAccelAvailable, isCompassAvailable, isGyroAvailable, isRotateVectorAvailable, isGameRotationVectorAvailable, isGravityAvailable, isLinearAccAvailable;

    private Sensor accelSensor, compassSensor, gyroSensor, RotateVectorSensor, GameRotationVector, Gravity, LinearAcc;

    private TextPaint contentPaint, textPaint;
    private Paint targetPaint, roundRec, borderRec, compassPaint, linePaint;

    private int axisX, axisY;

    PaintUtils paintUtilities = new PaintUtils(this);

    float rotation[] = new float[9];

    float cameraRotation[] = new float[9];

    List<Float> listOfBearingTo = new ArrayList<>();
    List<PointOfInterest> listOfPOI;

    float orientation[] = new float[3];
    float orientationAplha[] = new float[3];

    Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.poismaller);
    int bmpHeight = bmp.getHeight();

    Bitmap icon;
    int iconWidth = 0;
    int iconHeigth = 0;

    Bitmap bmpCompass = BitmapFactory.decodeResource(getResources(), R.drawable.kompas);
    float bmpCompassWidth = bmpCompass.getWidth();
    float bmpCompassHeight = bmpCompass.getHeight();

    Bitmap bmpPoint = BitmapFactory.decodeResource(getResources(), R.drawable.redpoint);
    float bmpPoinwWidth = bmpPoint.getWidth();

    float mathTan;
    private String type;

    public ViewMain(Context context, @Nullable AttributeSet attrs,String type) {
        super(context, attrs);
        this.context = context;
        this.handler = new Handler();
        this.type = type;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            icon = BitmapFactory.decodeResource(getResources(), R.drawable.class.getField(type+"_icn").getInt(null));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            icon = BitmapFactory.decodeResource(getResources(), R.drawable.cityscape);
        }
        iconWidth = icon.getWidth();
        iconHeigth = icon.getHeight();

        sensors = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        accelSensor = sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        compassSensor = sensors.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        gyroSensor = sensors.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        RotateVectorSensor = sensors.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        GameRotationVector = sensors.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        Gravity = sensors.getDefaultSensor(Sensor.TYPE_GRAVITY);
        LinearAcc = sensors.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        axisX = SensorManager.AXIS_X;
        axisY = SensorManager.AXIS_Z;

        startSensors();
        startGPS();

        getListOfPlaces();

        Camera camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        verticalFOV = params.getVerticalViewAngle();
        horizontalFOV = params.getHorizontalViewAngle();
        camera.release();

        contentPaint = paintUtilities.getContentPaint();
        targetPaint = paintUtilities.getTargetPaint();
        textPaint = paintUtilities.getTextPaint();
        roundRec = paintUtilities.getRoundRec();
        borderRec = paintUtilities.getBorderRec();
        compassPaint = paintUtilities.getCompassPaint();
        linePaint = paintUtilities.getLinePaint();
    }



    public void getListOfPlaces()
    {
        PointOfInterestController poiController = new PointOfInterestController(context);
        PointOfInterest poi = new PointOfInterest();
        LocationJSN locationJSN = new LocationJSN();
        locationJSN.setLng(endLong);
        locationJSN.setLat(endLat);
        Geometry geo = new Geometry();
        geo.setLocation(locationJSN);
        poi.setGeometry(geo);
        poi.setType(type);
        poiController.getPlacesByType(poi);
        listOfPOI = poiController.getListOfPOI();
    }

    private void startSensors() {

        isAccelAvailable = sensors.registerListener(this, accelSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        isCompassAvailable = sensors.registerListener(this, compassSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        isGyroAvailable = sensors.registerListener(this, gyroSensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        isRotateVectorAvailable = sensors.registerListener(this, RotateVectorSensor,
                SensorManager.SENSOR_DELAY_UI);
        isGameRotationVectorAvailable = sensors.registerListener(this, GameRotationVector,
                SensorManager.SENSOR_DELAY_FASTEST);
        isGravityAvailable = sensors.registerListener(this, Gravity,
                SensorManager.SENSOR_DELAY_FASTEST);
        isLinearAccAvailable = sensors.registerListener(this, LinearAcc,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void startGPS() {

        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Criteria criteria = new Criteria();
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pointOfInterests = listOfPOI;
        float[] dist = new float[1];
        int[] distance = new int[pointOfInterests.size()];
        int[] counter = new int[pointOfInterests.size()];

        float[] distForCompass = new float[1];
        int distanceForCompass;

        float POIWidth;

        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();

        WindowManager wm = (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
        int screenRotation = wm.getDefaultDisplay().getRotation();
        int konstx = 0;
        int konsty = 0;

        switch (screenRotation) {
            case Surface.ROTATION_0:
                konstx = (int) (canvasWidth / horizontalFOV);
                konsty = (int) (canvasHeight / verticalFOV);
                mathTan = (float) ((float) Math.tan(Math.toRadians(horizontalFOV / 2)) + 0.1);
                axisX = SensorManager.AXIS_X;
                axisY = SensorManager.AXIS_Z;
                break;
            case Surface.ROTATION_90: // rotation to left
                konstx = (int) (canvasHeight / horizontalFOV);
                konsty = (int) (canvasWidth / verticalFOV);
                mathTan = (float) ((float) Math.tan(Math.toRadians(horizontalFOV / 2)) + 0.7);
                axisX = SensorManager.AXIS_Z;
                axisY = SensorManager.AXIS_MINUS_X;
                break;
            case Surface.ROTATION_270: // rotation to right
                konstx = (int) (canvasHeight / horizontalFOV);
                konsty = (int) (canvasWidth / verticalFOV);
                mathTan = (float) ((float) Math.tan(Math.toRadians(horizontalFOV / 2)) + 0.7);
                axisX = SensorManager.AXIS_MINUS_Z;
                axisY = SensorManager.AXIS_X;
                break;

        }
        for (int i = 0; i < pointOfInterests.size(); i++) {
            Location.distanceBetween(pointOfInterests.get(i).getLatitude(),
                    pointOfInterests.get(i).getLongitude(), endLat, endLong, dist);
            counter[i] = i;
            distance[i] = (int) dist[0];

        }
        if (lastLocation == null) {
            canvas.save();

            canvas.drawRect((canvas.getWidth() / 2) - 300, (canvas.getHeight() / 2) - 80, (canvas.getWidth() / 2) + 300, (canvas.getHeight() / 2) + 80, roundRec);
            canvas.drawRect((canvas.getWidth() / 2) - 300, (canvas.getHeight() / 2) - 80, (canvas.getWidth() / 2) + 300, (canvas.getHeight() / 2) + 80, borderRec);
            canvas.drawText("Wait for GPS to locate you", canvas.getWidth() / 2, canvas.getHeight() / 2, textPaint);

            canvas.restore();
        }

        if (lastLocation != null) {
            for (int z = 0; z < pointOfInterests.size(); z++) {
                for (int j = z + 1; j < pointOfInterests.size(); j++) {
                    int a = distance[z];
                    int b = distance[j];

                    int c = counter[z];
                    int d = counter[j];
                    if (a < b) {
                        distance[z] = b;
                        distance[j] = a;

                        counter[z] = d;
                        counter[j] = c;
                    }
                }
            }
            for (int i = 0; i < pointOfInterests.size(); i++) {
                Location temp = new Location("manual");
                temp.setLatitude(pointOfInterests.get(i).getLatitude());
                temp.setLongitude(pointOfInterests.get(i).getLongitude());
                temp.setAltitude(pointOfInterests.get(i).getAltitude());

                listOfBearingTo.add(lastLocation.bearingTo(temp));

            }
            SensorManager.getRotationMatrixFromVector(rotation, lastRotationVector);
            SensorManager.remapCoordinateSystem(rotation, axisX, axisY, cameraRotation);

            SensorManager.getOrientation(cameraRotation, orientationAplha);
            orientation = lowPass(orientationAplha, orientation);


            for (int i = 0; i < pointOfInterests.size(); i++) {
                if (distance[i] < 50) {
                    POIHight = 0;
                } else if (distance[i] < 100) {
                    POIHight = 40;
                } else if (distance[i] < 150) {
                    POIHight = 80;
                } else if (distance[i] < 200) {
                    POIHight = 120;
                } else if (distance[i] > 200) {
                    POIHight = 160;
                }

                textHeight = textPaint.descent() - textPaint.ascent();
                textOffset = (textHeight / 2) - textPaint.descent();
                textlength = pointOfInterests.get(counter[i]).getName().length();

                canvas.save();
                String mytext = pointOfInterests.get(counter[i]).getName();
                String mytext2 = String.valueOf(distance[i]);
                canvas.rotate((float) (0.0f - Math.toDegrees(orientation[2])), (float) canvasWidth / 2, (float) canvasHeight / 2 + bmpHeight);
                float dx = (float) ((konstx) * (Math.toDegrees(orientation[0]) - listOfBearingTo.get(counter[i])));
                float dy = (float) ((konsty) * Math.toDegrees(orientation[1]) / 3);
                canvas.translate(0.0f, 0.0f - dy);
                canvas.translate(0.0f - dx, 0.0f);
                canvas.drawRect((float) ((canvasWidth / 2) - (textOffset * 20)) - 2, (float) ((canvasHeight / 2) - 50 - POIHight), (float) ((canvasWidth / 2) + (textOffset * 20)), (float) ((canvasHeight / 2) + 110 - POIHight), roundRec);
                canvas.drawRect((float) ((canvasWidth / 2) - (textOffset * 20)) - 2, (float) ((canvasHeight / 2) - 50 - POIHight), (float) ((canvasWidth / 2) + (textOffset * 20)), (float) ((canvasHeight / 2) + 110 - POIHight), borderRec);
                canvas.drawText(mytext, (float) (canvasWidth / 2), (float) (canvasHeight / 2 - POIHight), textPaint);
                canvas.drawText(mytext2 + " meters ", (float) (canvasWidth / 2), (float) ((canvasHeight / 2) + 40f - POIHight), textPaint);
                canvas.drawText(pointOfInterests.get(counter[i]).getOpeningHours() != null && pointOfInterests.get(counter[i]).getOpeningHours().getOpenNow() ? "Opened" : "Closed", (float) (canvasWidth / 2), (float) ((canvasHeight / 2) + 80f - POIHight), textPaint);

                canvas.drawBitmap(icon, (float) (canvasWidth / 2) - iconWidth * 2, (float) ((canvasHeight / 2) + 10f - POIHight), null);
                canvas.restore();

                float radarLineX = (bmpCompassHeight / 2) * mathTan;

                compasBearing = (float) Math.toDegrees(orientation[0]);
                if (compasBearing < 0) {
                    compasBearing += 360;
                }
                int rotateCompas = (int) (360 - compasBearing);

                canvas.save();

                Matrix transform = new Matrix();
                transform.setRotate(rotateCompas, bmpCompassWidth / 2, bmpCompassHeight / 2);
                transform.postTranslate((float) canvasWidth - bmpCompassWidth, 0);
                canvas.drawBitmap(bmpCompass, transform, compassPaint);

                canvas.drawLine((float) canvasWidth - (bmpCompassWidth / 2), bmpCompassWidth / 2, (float) canvasWidth - (bmpCompassWidth / 2) + radarLineX, 0, linePaint);
                canvas.drawLine((float) canvasWidth - (bmpCompassWidth / 2), bmpCompassWidth / 2, (float) canvasWidth - (bmpCompassWidth / 2) - radarLineX, 0, linePaint);

                canvas.restore();
                canvas.save();

                for (int j = 0; j < pointOfInterests.size(); j++) {

                    Location.distanceBetween(pointOfInterests.get(j).getLatitude(),
                            pointOfInterests.get(j).getLongitude(), endLat, endLong, distForCompass);
                    distanceForCompass = (int) distForCompass[0];


                    if (distanceForCompass < radarRange) {
                        POIBearing = (float) Math.toDegrees(orientation[0]) - listOfBearingTo.get(j);
                        if (POIBearing < 0) {
                            POIBearing += 360;
                        }


                        int rotatePOI = (int) (360 - POIBearing);
                        POIWidth = (((bmpCompassWidth / 2) / radarRange) * distanceForCompass);

                        canvas.save();

                        Matrix transformPOI = new Matrix();
                        transformPOI.setTranslate((float) (canvasWidth - (bmpCompassWidth / 2) - (bmpPoinwWidth / 2)), ((bmpCompassHeight / 2) - POIWidth));
                        transformPOI.postRotate(rotatePOI, (float) (canvasWidth - (bmpCompassWidth / 2)), ((bmpCompassHeight / 2)));
                        canvas.drawBitmap(bmpPoint, transformPOI, null);

                        canvas.restore();
                    }
                }
                canvas.restore();
            }
        }

    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < pointOfInterests.size(); i++) {

                    float dx = (float) ((canvasWidth / horizontalFOV) * (Math.toDegrees(orientation[0]) - listOfBearingTo.get(i)));
                    float dy = (float) ((canvasHeight / verticalFOV) * Math.toDegrees(orientation[1]) / 3);

                    float[] dist = new float[1];
                    int distance;

                    Location.distanceBetween(pointOfInterests.get(i).getLatitude(),
                            pointOfInterests.get(i).getLongitude(), endLat, endLong, dist);
                    distance = (int) dist[0];

                    if (distance < 50) {
                        POIHight = 0;
                    } else if (distance < 100) {
                        POIHight = 40;
                    } else if (distance < 150) {
                        POIHight = 80;
                    } else if (distance < 200) {
                        POIHight = 120;
                    } else if (distance > 200) {
                        POIHight = 160;
                    }

                    textHeight = textPaint.descent() - textPaint.ascent();
                    textOffset = (textHeight / 2) - textPaint.descent();
                    textlength = pointOfInterests.get(i).getName().length();


                    if (x >= (((canvasWidth / 2) - dx) - 36 * 5) && x < ((canvasWidth / 2) - dx - (36 * 5) + 200) && y >= ((canvasHeight / 2) - dy - (95 * 4) - POIHight - 50) && y < ((canvasHeight / 2) - dy - (95 * 4) - POIHight + 400)) {

                        Intent intent = new Intent(context, PlaceDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(PlaceDetailActivity.PLACES_ID,pointOfInterests.get(i).getPlaceId());
                        intent.putExtra(PlaceDetailActivity.MY_LAT,endLat);
                        intent.putExtra(PlaceDetailActivity.MY_LONG,endLong);
                        context.startActivity(intent);
                    }

                }
                break;

        }

        return false;
    }


    public void onAccuracyChanged(Sensor arg0, int arg1) {
        Log.d(DEBUG_TAG, "onAccuracyChanged");

    }

    public void onSensorChanged(SensorEvent event) {
        
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                lastAccelerometer = event.values.clone();
                break;
            case Sensor.TYPE_GYROSCOPE:
                lastGyroscope = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                lastCompass = event.values.clone();
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                lastRotationVector = event.values.clone();
                break;
            case Sensor.TYPE_GAME_ROTATION_VECTOR:
                lastGameRotationVector = event.values.clone();
                break;
            case Sensor.TYPE_GRAVITY:
                lastGravity = event.values.clone();
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                lastLinearAcc = event.values.clone();
                break;
        }

        this.invalidate();


    }

    public void onLocationChanged(Location location) {
        
        // store it off for use when we need it
        lastLocation = location;
        endLat = location.getLatitude();
        endLong = location.getLongitude();
        endAlti = location.getAltitude();
        getListOfPlaces();
    }

    public void onProviderDisabled(String provider) {
        // ...
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    // this is not an override
    public void onPause() {

        locationManager.removeUpdates(this);
        sensors.unregisterListener(this);
    }

    // this is not an override
    public void onResume() {
        startSensors();
        startGPS();
    }


    // static final float ALPHA = 0.05f; // if ALPHA = 1 OR 0, no filter applies.
    static final float ALPHA = 0.5f;

    protected float[] lowPass(float[] input, float[] output) {
        if (output == null) return input;
        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(context, "Connection to Google service was suspended", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "Faild connection to Google service", Toast.LENGTH_LONG).show();
    }
}
