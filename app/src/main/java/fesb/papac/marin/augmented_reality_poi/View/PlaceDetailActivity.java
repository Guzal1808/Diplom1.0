package fesb.papac.marin.augmented_reality_poi.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
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

import java.io.ByteArrayOutputStream;
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
    DatabaseHandler db;
    private GoogleMap mMap;

    public static final String PLACES_DB_ID = "DB_ID";
    public static final String PLACES_ID = "ID";
    public static final String MY_LAT="MY_LAT";
    public static final String MY_LONG="MY_LONG";
    double lat=0.0;
    double lon=0.0;

    PointOfInterestController poiController;
    Context context;

    public LikeButton likeButton;
    private PointOfInterest place;
    private View view;
    ImageView ivPlacePhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_detail);
        db = new DatabaseHandler(getApplicationContext());
        lat = getIntent().getDoubleExtra(MY_LAT,0.0);
        lon = getIntent().getDoubleExtra(MY_LONG,0.0);
        context = getApplicationContext();
        likeButton= findViewById(R.id.star_button);
        view = this.findViewById(android.R.id.content);
        ivPlacePhoto = view.findViewById(R.id.card_image);
        String str = getIntent().getStringExtra(PLACES_DB_ID);
        if (getIntent().getStringExtra(PLACES_ID)!=null && getIntent().getStringExtra(PLACES_ID).length()>0)
            loadFromInternet();
        else if (getIntent().getStringExtra(PLACES_DB_ID)!=null && getIntent().getStringExtra(PLACES_DB_ID).length()>0)
            loadFromDataBase();

    }


    private void loadFromInternet() {
        poiController = new PointOfInterestController(getApplicationContext());

        poiController.getResponseDetailsOfPlace(getIntent().getStringExtra(PLACES_ID), view)
                .enqueue(new Callback<HttpHelper>() {
                    @Override
                    public void onResponse(Call<HttpHelper> call, Response<HttpHelper> response) {
                        assert response.body() != null;
                        place = response.body().getResult();
                        place.setType(place.getTypes().get(0));
                        String path="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
                        String photoKey = context.getString(R.string.PHOTO_KEY);

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

                        loadData(place);
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync(googleMap -> {
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

                        });
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

                List<Favorite> fav = db.readAllFavorite();
                Gson gson = new Gson();
                Bitmap bitmap = ((BitmapDrawable)ivPlacePhoto.getDrawable()).getBitmap();
                if(db.addFavorite(new Favorite(place.getName(),place.getLocation(), gson.toJson(place),getByteArrayFromBitmap(bitmap), place.getType())))
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

    private void loadFromDataBase() {
        Gson gson = new Gson();
        likeButton.setLiked(true);
        Favorite favorite = db.getFavoriteById(getIntent().getStringExtra(PLACES_DB_ID));
        place = gson.fromJson(favorite.getDetails(),PointOfInterest.class);
        loadData(place);
       ivPlacePhoto.setImageBitmap(getBitmapFromArray(favorite.getImage()));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            LatLng pointPlace = new LatLng(place.getLatitude(), place.getLongitude());
            mMap.addMarker(new MarkerOptions().position(pointPlace).title(place.getName())).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pointPlace, 14));
        });
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                   if(db.deleteFavorite(getIntent().getStringExtra(PLACES_DB_ID)))
                   {
                       Toast.makeText(getApplicationContext(), "Удалено из избранного", Toast.LENGTH_SHORT).show();
                   }
            }
        });
    }

    private void loadData(PointOfInterest place)
    {
        TextView placeName = view.findViewById(R.id.placeName);
        TextView placeAddress = view.findViewById(R.id.placeAddress);
        TextView placeTelNo = view.findViewById(R.id.placeTelNo);
        TextView placeWebURL = view.findViewById(R.id.placeWebURL);
        RatingBar ratingBar =  view.findViewById(R.id.ratingBar);
        TextView openingHours =  view.findViewById(R.id.openingHours);

        placeName.setText(place.getName());
        placeAddress.setText(place.getFormatted_address());
        placeTelNo.setText(place.getInternational_phone_number());
        ratingBar.setRating(place.getRating()!=null ? place.getRating().floatValue() : 0.0f);
        placeWebURL.setText(place.getWebsite());
        openingHours.setText(place.getOpeningHours() != null && place.getOpeningHours().getOpenNow() ? "Opened" : "Closed");

        Linkify.addLinks(placeTelNo, Linkify.PHONE_NUMBERS);
        Linkify.addLinks(placeWebURL, Linkify.WEB_URLS);
    }

    private byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    private Bitmap getBitmapFromArray(byte[] img)
    {
        Bitmap bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
        return bmp;
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
