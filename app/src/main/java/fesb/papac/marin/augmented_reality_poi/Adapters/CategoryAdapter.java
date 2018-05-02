package fesb.papac.marin.augmented_reality_poi.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import fesb.papac.marin.augmented_reality_poi.Model.CategoryPlace;
import fesb.papac.marin.augmented_reality_poi.Model.TypePlace;
import fesb.papac.marin.augmented_reality_poi.R;
import fesb.papac.marin.augmented_reality_poi.View.CategoryViewHolder;
import fesb.papac.marin.augmented_reality_poi.View.TypesPlaceViewHolder;

public class CategoryAdapter  extends ExpandableRecyclerViewAdapter<CategoryViewHolder, TypesPlaceViewHolder> {


    public CategoryAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public CategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public TypesPlaceViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_type, parent, false);
        return new TypesPlaceViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(TypesPlaceViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final TypePlace artist = ((CategoryPlace) group).getItems().get(childIndex);
        holder.setArtistName(artist.getName());
        holder.setKeyWord(artist.getKeyWord());
    }

    @Override
    public void onBindGroupViewHolder(CategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setCategoryTitle(group);
    }
}
