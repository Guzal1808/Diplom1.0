package fesb.papac.marin.augmented_reality_poi;

/**
 * Created by Marin on 26.4.2017..
 */

public class PointOfInterest {

    public double latitude;
    public double longitude;
    public double altitude;
    public String place;
    public String data;

    public Geometry geometry;
    public Photo photo;
    public OpeningHours openingHours;

    public PointOfInterest() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public String getPlaces() {
        return place;
    }

    public String getData() { return data;}

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHours openingHours) {
        this.openingHours = openingHours;
    }
}
