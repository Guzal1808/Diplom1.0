package fesb.papac.marin.augmented_reality_poi.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class TypePlace implements Parcelable {

    private String name;
    private String keyWord;
    private boolean isFavorite;

    public TypePlace(String name, String keyWord, boolean isFavorite) {
        this.name = name;
        this.keyWord = keyWord;
        this.isFavorite = isFavorite;
    }

    public TypePlace(String name, boolean isFavorite) {
        this.name = name;
        this.isFavorite = isFavorite;
    }

    protected TypePlace(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypePlace)) return false;

        TypePlace TypePlace = (TypePlace) o;

        if (isFavorite() != TypePlace.isFavorite()) return false;
        return getName() != null ? getName().equals(TypePlace.getName()) : TypePlace.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (isFavorite() ? 1 : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TypePlace> CREATOR = new Creator<TypePlace>() {
        @Override
        public TypePlace createFromParcel(Parcel in) {
            return new TypePlace(in);
        }

        @Override
        public TypePlace[] newArray(int size) {
            return new TypePlace[size];
        }
    };

    
}
