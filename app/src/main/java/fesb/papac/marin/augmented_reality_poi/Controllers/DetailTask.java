package fesb.papac.marin.augmented_reality_poi.Controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.squareup.picasso.Picasso;

import fesb.papac.marin.augmented_reality_poi.R;

public class DetailTask extends AsyncTask<String, Void, Place> {

    GoogleApiClient mGoogleApiClient;
    Context context;
    ImageView ivPlacePhoto;
    View rootView;
    Place currentPlace;

    public DetailTask(GoogleApiClient mGoogleApiClient, View rootView) {
        this.mGoogleApiClient = mGoogleApiClient;
        this.context = context;
        this.rootView = rootView;
        this.context=rootView.getContext();
        this.ivPlacePhoto = (ImageView) rootView.findViewById(R.id.card_image);
    }

    @Override
    protected Place doInBackground(String... params) {
        if (params.length != 1) {
            return null;
        }
        String placeId = params[0];

        if (placeId == null){
            placeId ="ChIJrw7QBK9YXIYRvBagEDvhVgg";
        }
        PlaceBuffer place=  Places.GeoDataApi.
                getPlaceById(mGoogleApiClient, placeId)
                .await();
        currentPlace = place.get(0);
        place.release();

        return currentPlace;
    }

    @Override
    protected void onPreExecute() {
        Picasso.with( context )
                .load( R.drawable.progress_animation  )
                .into( ivPlacePhoto );
        // Display a temporary image to show while bitmap is loading.
        //ivPlacePhoto.setImageResource(R.drawable.redpoint);
    }
    @Override
    protected void onPostExecute(Place place) {
        String path = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
        final String KEY = context.getString(R.string.PHOTO_KEY);
        if (place != null) {
            // Photo has been loaded, display it.
            Picasso.with(context).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyBq8yJ4I36zePlooPdERim4id66b6apSSg").into(ivPlacePhoto);

            /* ivPlacePhoto.setImageBitmap(attributedPhoto.bitmap);*/
        }
        else {
            Picasso.with(context).load("static.pexels.com/photos/9579/pexels-photo.jpeg").into(ivPlacePhoto);
        }
    }
}
