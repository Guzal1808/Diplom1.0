package fesb.papac.marin.augmented_reality_poi.Service;

import java.util.ArrayList;

import fesb.papac.marin.augmented_reality_poi.Model.PointOfInterest;

/**
 * Created by Dementor on 24.02.2018.
 */

public interface PointOfInterestService {

    void getPlacesByType(String type, PointOfInterest poi);
     ArrayList<PointOfInterest> getListOfPOI();
}
