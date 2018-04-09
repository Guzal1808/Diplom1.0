package fesb.papac.marin.augmented_reality_poi.View;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.squareup.picasso.Picasso;

import java.util.zip.Inflater;

import fesb.papac.marin.augmented_reality_poi.Controllers.DetailTask;
import fesb.papac.marin.augmented_reality_poi.Controllers.PhotoTask;
import fesb.papac.marin.augmented_reality_poi.Controllers.PointOfInterestController;
import fesb.papac.marin.augmented_reality_poi.R;

/**
 * Created by Гюзаль on 29.03.2018.
 */

public class PlaceDetailActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    GoogleApiClient mGoogleApiClient;
    public static final String PLACES_ID = "ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail);

        final ImageView ivPlacePhoto = (ImageView) findViewById(R.id.card_image);

        PointOfInterestController poiController = new PointOfInterestController(getApplicationContext());
        poiController.getDetailsOfPlace(getIntent().getStringExtra(PLACES_ID));

      /*  mGoogleApiClient = new GoogleApiClient
                .Builder( this )
                .enableAutoManage( this, 0, this )
                .addApi( Places.GEO_DATA_API )
                .addApi( Places.PLACE_DETECTION_API )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .build();

        View view = this.findViewById(android.R.id.content);*/

      /*  new DetailTask(mGoogleApiClient,view).execute(getIntent().getStringExtra(PLACES_ID));*/

        /*new DetailTask(mGoogleApiClient){
            @Override
            protected void onPreExecute() {
                Picasso.with( getApplicationContext() )
                        .load( R.drawable.progress_animation  )
                        .into( ivPlacePhoto );
                // Display a temporary image to show while bitmap is loading.
                //ivPlacePhoto.setImageResource(R.drawable.redpoint);
            }
            @Override
            protected void onPostExecute(Place place) {
                if (place != null) {
                    // Photo has been loaded, display it.
                    Picasso.with(getApplicationContext()).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyBq8yJ4I36zePlooPdERim4id66b6apSSg").into(ivPlacePhoto);

                    *//* ivPlacePhoto.setImageBitmap(attributedPhoto.bitmap);*//*
                }
                else {
                    Picasso.with(getApplicationContext()).load("static.pexels.com/photos/9579/pexels-photo.jpeg").into(ivPlacePhoto);
                }
            }
        }.execute(getIntent().getStringExtra(PLACES_ID));*/

    }

    @Override
    protected void onStart() {
        super.onStart();
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
