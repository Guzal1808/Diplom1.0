package fesb.papac.marin.augmented_reality_poi.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dementor on 01.03.2018.
 */

public class LocationJSN {

    @SerializedName("lat")
    @Expose
    private Double latitude;

    @SerializedName("lng")
    @Expose
    private Double longitude;

    public Double getLat() {
        return latitude;
    }

    public void setLat(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLng() {
        return longitude;
    }

    public void setLng(Double longitude) {
        this.longitude = longitude;
    }
}
