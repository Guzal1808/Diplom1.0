package fesb.papac.marin.augmented_reality_poi.Service;

import fesb.papac.marin.augmented_reality_poi.Model.ElevationHelper;
import fesb.papac.marin.augmented_reality_poi.Model.HttpHelper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dementor on 28.02.2018.
 */

public interface RetrofitMaps {

    @GET("api/place/nearbysearch/json?sensor=true")
    Call<HttpHelper> getNearbyPlaces(@Query("location") String location, @Query("radius") String radius, @Query("type") String type, @Query("key") String key);

    @GET("api/elevation/json?")
    Call<ElevationHelper> getElevation(@Query("locations") String locations, @Query("key") String key);

    @GET("api/place/details/json?")
    Call<HttpHelper> getDetailsOfPlace(@Query("placeid") String placeid,@Query("key") String key);


}
