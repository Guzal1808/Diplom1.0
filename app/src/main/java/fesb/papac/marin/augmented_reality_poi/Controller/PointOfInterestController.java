package fesb.papac.marin.augmented_reality_poi.Controller;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.places.PlacePhotoMetadata;

import java.util.ArrayList;

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

    public PointOfInterestController(Context context) {
        this.context = context;
    }

    @Override
    public void getPlacesByType(PointOfInterest poi) {

        final String url = "https://maps.googleapis.com/maps/";
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
    public ArrayList<PointOfInterest> getListOfPOI() {
        return listOfPOI;
    }

}
