package fesb.papac.marin.augmented_reality_poi.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ElevationHelper {

    @SerializedName("results")
    @Expose
    private Geometry results;

    @SerializedName("status")
    @Expose
    private String status;


    public Geometry getResults() {
        return results;
    }

    public void setResults(Geometry results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
