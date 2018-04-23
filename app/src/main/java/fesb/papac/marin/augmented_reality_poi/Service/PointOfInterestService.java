package fesb.papac.marin.augmented_reality_poi.Service;

import android.view.View;

import java.util.ArrayList;

import fesb.papac.marin.augmented_reality_poi.Model.Geometry;
import fesb.papac.marin.augmented_reality_poi.Model.HttpHelper;
import fesb.papac.marin.augmented_reality_poi.Model.PointOfInterest;
import retrofit2.Call;

/**
 * Created by Dementor on 24.02.2018.
 */

public interface PointOfInterestService {

    void getPlacesByType(PointOfInterest poi);

    Call<HttpHelper> getPlaces(PointOfInterest poi);

     ArrayList<PointOfInterest> getListOfPOI();

     PointOfInterest getDetailsOfPlace(String placeID, View view);

    Call<HttpHelper> getResponseDetailsOfPlace(String placeID, View view);

    Geometry setElevation(PointOfInterest poi);
}
