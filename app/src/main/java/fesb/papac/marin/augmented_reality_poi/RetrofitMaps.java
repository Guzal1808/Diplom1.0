package fesb.papac.marin.augmented_reality_poi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dementor on 28.02.2018.
 */

public interface RetrofitMaps {

    @GET("api/place/nearbysearch/json?sensor=true")
    Call<HttpHelper> getNearbyPlaces(@Query("location") String location, @Query("radius") String radius,@Query("type") String type,  @Query("key") String key);
}
