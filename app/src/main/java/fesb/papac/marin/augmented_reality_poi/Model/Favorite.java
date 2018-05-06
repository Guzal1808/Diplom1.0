package fesb.papac.marin.augmented_reality_poi.Model;

public class Favorite {

    private int id;
    public String name;
    public String location;
    public String details;
    private String type;

    public Favorite() {
    }

    public Favorite(String name, String location, String details, String type) {
        this.name = name;
        this.location = location;
        this.details = details;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
