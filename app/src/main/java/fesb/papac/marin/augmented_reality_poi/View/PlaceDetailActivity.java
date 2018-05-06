package fesb.papac.marin.augmented_reality_poi.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

import fesb.papac.marin.augmented_reality_poi.Adapters.DatabaseHandler;
import fesb.papac.marin.augmented_reality_poi.Controllers.DetailTask;
import fesb.papac.marin.augmented_reality_poi.Controllers.PhotoTask;
import fesb.papac.marin.augmented_reality_poi.Controllers.PointOfInterestController;
import fesb.papac.marin.augmented_reality_poi.Model.Favorite;
import fesb.papac.marin.augmented_reality_poi.Model.HttpHelper;
import fesb.papac.marin.augmented_reality_poi.Model.PointOfInterest;
import fesb.papac.marin.augmented_reality_poi.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Гюзаль on 29.03.2018.
 */

public class PlaceDetailActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    GoogleApiClient mGoogleApiClient;
    public static final String PLACES_ID = "ID";
    public static final String MY_LAT="MY_LAT";
    public static final String MY_LONG="MY_LONG";
    double lat=0.0;
    double lon=0.0;
    private GoogleMap mMap;
    PointOfInterestController poiController;
    Context context;
    public LikeButton likeButton;
    private PointOfInterest place;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail);
        lat = getIntent().getDoubleExtra(MY_LAT,0.0);
        lon = getIntent().getDoubleExtra(MY_LONG,0.0);
        context = getApplicationContext();
        likeButton= findViewById(R.id.star_button);
        View view = this.findViewById(android.R.id.content);
        poiController = new PointOfInterestController(getApplicationContext());

        poiController.getResponseDetailsOfPlace(getIntent().getStringExtra(PLACES_ID), view)
                .enqueue(new Callback<HttpHelper>() {
            @Override
            public void onResponse(Call<HttpHelper> call, Response<HttpHelper> response) {
                assert response.body() != null;
                place = response.body().getResult();
                String path="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
                String photoKey = context.getString(R.string.PHOTO_KEY);
                ImageView ivPlacePhoto = view.findViewById(R.id.card_image);
                if (place.getPhotos().size()>0) {
                    String fullPath = path + place.getPhotos().get(0).getPhotoReference() + "&key=" + photoKey;

                    Picasso.with(context)
                            .load(fullPath)
                            .into(ivPlacePhoto);
                } else
                {
                    Picasso.with(context)
                            .load(R.drawable.city)
                            .into(ivPlacePhoto);
                }

                TextView placeName = view.findViewById(R.id.placeName);
                TextView placeAddress = view.findViewById(R.id.placeAddress);
                TextView placeTelNo = view.findViewById(R.id.placeTelNo);
                TextView placeWebURL = view.findViewById(R.id.placeWebURL);
                RatingBar ratingBar =  view.findViewById(R.id.ratingBar);
                TextView openingHours =  view.findViewById(R.id.openingHours);

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(new OnMapReadyCallback(){
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        mMap = googleMap;
                        if (poiController.getResultPlace()!=null) {
                            Double d = poiController.getResultPlace().getLongitude();
                        }
                        //seattle coordinates
                        LatLng pointPlace = new LatLng(place.getLatitude(), place.getLongitude());
                        LatLng myPlace = new LatLng(lat,lon);
                        mMap.addMarker(new MarkerOptions().position(pointPlace).title(place.getName())).showInfoWindow();
                        mMap.addMarker(new MarkerOptions()
                                .position(myPlace)
                                .title("You are here")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))).showInfoWindow();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointPlace, 14));

                    }
                });

                placeName.setText(place.getName());
                placeAddress.setText(place.getFormatted_address());
                placeTelNo.setText(place.getInternational_phone_number());
                ratingBar.setRating(place.getRating()!=null ? place.getRating().floatValue() : 0.0f);
                placeWebURL.setText(place.getWebsite());
                openingHours.setText(place.getOpeningHours() != null && place.getOpeningHours().getOpenNow() ? "Opened" : "Closed");

                Linkify.addLinks(placeTelNo, Linkify.PHONE_NUMBERS);
                Linkify.addLinks(placeWebURL, Linkify.WEB_URLS);
            }

            @Override
            public void onFailure(Call<HttpHelper> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                //TODO database insert
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                List<Favorite> fav = db.readAllFavorite();
                Gson gson = new Gson();
                if(db.addFavorite(new Favorite(place.getName(),place.getLocation(), gson.toJson(place), place.getType())))
                {
                     Toast.makeText(getApplicationContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Уже есть в вашем списке", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Toast.makeText(getApplicationContext(),"DisLiked",Toast.LENGTH_LONG).show();
            }
        });
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
