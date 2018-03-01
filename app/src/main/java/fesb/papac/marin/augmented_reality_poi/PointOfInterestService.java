package fesb.papac.marin.augmented_reality_poi;

import java.util.ArrayList;

/**
 * Created by Dementor on 24.02.2018.
 */

public interface PointOfInterestService {

    ArrayList<PointOfInterest> getAllPointsOfInterest(double lat, double lng);
    PointOfInterest getPointOfInterestByType(double lat, double lng,String type);
    void getPlacesByType(String type, PointOfInterest poi);
}
