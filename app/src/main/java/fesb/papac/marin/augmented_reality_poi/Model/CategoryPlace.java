package fesb.papac.marin.augmented_reality_poi.Model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class CategoryPlace extends ExpandableGroup<TypePlace> {
    
    private int iconResId;

    public CategoryPlace(String title, List<TypePlace> items, int iconResId) {
        super(title, items);
        this.iconResId = iconResId;
    }

    public int getIconResId() {
        return iconResId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryPlace)) return false;

        CategoryPlace CategoryPlace = (CategoryPlace) o;

        return getIconResId() == CategoryPlace.getIconResId();

    }

    @Override
    public int hashCode() {
        return getIconResId();
    }
}
