package fesb.papac.marin.augmented_reality_poi.Helper;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import fesb.papac.marin.augmented_reality_poi.Model.CategoryPlace;
import fesb.papac.marin.augmented_reality_poi.Model.TypePlace;
import fesb.papac.marin.augmented_reality_poi.R;

public class PlacesDataFactory {

    static Context context;

    public PlacesDataFactory(Context context) {
        this.context = context;
    }

    public static List<CategoryPlace> makeCategoryPlaces()
    {
        List<CategoryPlace> categoryPlaceList = new ArrayList<>();
        Resources resources = context.getResources();
        String[] mPlaces = resources.getStringArray(R.array.places);
        for (String mPlace : mPlaces) {
            String[] pair = mPlace.split(":");
            int resId = context.getResources().getIdentifier(pair[0], "array", context.getPackageName());
            int imgId = context.getResources().getIdentifier(pair[0], "drawable", context.getPackageName());
            List<TypePlace> typePlaceList = new ArrayList<>();
            if (resId!=0) {

                String[] mTypes = resources.getStringArray(resId);//resources.getStringArray(resId);

                for (String mType : mTypes) {
                    String[] pairs = mType.split(":");
                    typePlaceList.add(new TypePlace(pairs[0],pairs[1], false));
                }
            }
            categoryPlaceList.add(new CategoryPlace(pair[1], typePlaceList, imgId));
        }

        return categoryPlaceList;
    }

}
