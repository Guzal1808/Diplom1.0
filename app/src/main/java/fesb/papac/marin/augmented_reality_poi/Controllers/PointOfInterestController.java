package fesb.papac.marin.augmented_reality_poi.Controllers;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.maps.SupportMapFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fesb.papac.marin.augmented_reality_poi.Model.ElevationHelper;
import fesb.papac.marin.augmented_reality_poi.Model.Geometry;
import fesb.papac.marin.augmented_reality_poi.Model.HttpHelper;
import fesb.papac.marin.augmented_reality_poi.Model.PointOfInterest;
import fesb.papac.marin.augmented_reality_poi.R;
import fesb.papac.marin.augmented_reality_poi.Service.PointOfInterestService;
import fesb.papac.marin.augmented_reality_poi.Service.RetrofitMaps;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dementor on 24.02.2018.
 */

public class PointOfInterestController implements PointOfInterestService {

    ArrayList<PointOfInterest> listOfPOI=new ArrayList<>();
    Context context;
    PlacePhotoMetadata photo;
    PointOfInterest resultPlace;
    final String url = "https://maps.googleapis.com/maps/";

    public PointOfInterestController(Context context) {
        this.context = context;
    }



    @Override
    public void getPlacesByType(PointOfInterest poi) {


        final String KEY = context.getString(R.string.GOOGLEAPI_KEY);//"AIzaSyBPkUvg6Xz17H4uK5rBl-Hf7K7ItOvjCUA";

        final String RADIUS= context.getString(R.string.SEARCHING_RADIUS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<HttpHelper> call = service.getNearbyPlaces(poi.getGeometry().getLocation().getLat() + "," + poi.getGeometry().getLocation().getLng(), RADIUS,poi.getType(), KEY);

        call.enqueue(new Callback<HttpHelper>() {

            @Override
            public void onResponse(Call<HttpHelper> call, Response<HttpHelper> response) {
                try {
                    Log.d("onResponse", "Parse data");
                    listOfPOI.addAll(response.body().getResults());
                    for (PointOfInterest point: listOfPOI) {
                        setElevation(point);
                    }


                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<HttpHelper> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }

    @Override
    public Call<HttpHelper> getPlaces(PointOfInterest poi) {
        final String KEY = context.getString(R.string.GOOGLEAPI_KEY);//"AIzaSyBPkUvg6Xz17H4uK5rBl-Hf7K7ItOvjCUA";

        final String RADIUS= context.getString(R.string.SEARCHING_RADIUS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<HttpHelper> call = service.getNearbyPlaces(poi.getGeometry().getLocation().getLat() + "," + poi.getGeometry().getLocation().getLng(), RADIUS,poi.getType(), KEY);

        return call;
    }

    @Override
    public ArrayList<PointOfInterest> getListOfPOI() {

        return listOfPOI;
    }

    @Override
    public Geometry setElevation(PointOfInterest poi) {
        final String KEY = context.getString(R.string.ELEVATION_KEY);
        String locations = poi.getLatitude()+","+poi.getLongitude();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<ElevationHelper> call = service.getElevation(locations, KEY);

        call.enqueue(new Callback<ElevationHelper>() {
            @Override
            public void onResponse(Call<ElevationHelper> call, Response<ElevationHelper> response) {
                Geometry geometry = response.body().getResults();
                String g = geometry.getElevation();
            }

            @Override
            public void onFailure(Call<ElevationHelper> call, Throwable t) {

            }
        });

        return null;
    }

    @Override
    public Call<HttpHelper> getResponseDetailsOfPlace(String placeID, View view) {
        final String KEY = context.getString(R.string.DETAILS_KEY);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<HttpHelper> call = service.getDetailsOfPlace(placeID, KEY);

        return call;
    }

    @Override
    public PointOfInterest getDetailsOfPlace(String placeID, View view) {
        final String KEY = context.getString(R.string.DETAILS_KEY);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<HttpHelper> call = service.getDetailsOfPlace(placeID, KEY);

        call.enqueue(new Callback<HttpHelper>() {
            @Override
            public void onResponse(Call<HttpHelper> call, Response<HttpHelper> response) {
                assert response.body() != null;
                PointOfInterest place = response.body().getResult();
                resultPlace = place;
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

        return null;
    }

    public PointOfInterest getResultPlace() {
        return resultPlace;
    }

    public void setResultPlace(PointOfInterest resultPlace) {
        this.resultPlace = resultPlace;
    }
}
