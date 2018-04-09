package fesb.papac.marin.augmented_reality_poi.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dementor on 01.03.2018.
 */

public class Geometry {
    @SerializedName("location")
    @Expose
    private LocationJSN location;

    @SerializedName("elevation")
    @Expose
    private String elevation;

    @SerializedName("resolution")
    @Expose
    private String resolution;

    public LocationJSN getLocation() {
        return location;
    }

    public void setLocation(LocationJSN location) {
        this.location = location;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
