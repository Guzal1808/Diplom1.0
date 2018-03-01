package fesb.papac.marin.augmented_reality_poi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dementor on 01.03.2018.
 */

public class Geometry {
    @SerializedName("location")
    @Expose
    private LocationJSN location;

    public LocationJSN getLocation() {
        return location;
    }

    public void setLocation(LocationJSN location) {
        this.location = location;
    }
}
