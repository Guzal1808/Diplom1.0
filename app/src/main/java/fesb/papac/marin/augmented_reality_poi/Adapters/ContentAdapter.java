package fesb.papac.marin.augmented_reality_poi.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fesb.papac.marin.augmented_reality_poi.R;
import fesb.papac.marin.augmented_reality_poi.View.CardFragment;

public class ContentAdapter extends RecyclerView.Adapter<CardFragment.ViewHolder> {
    // Set numbers of Card in RecyclerView.
    private int LENGTH = 0;

    private final String[] mPlaces;
    private final List<Drawable> placesPictures= new ArrayList<>();

    public ContentAdapter(Context context) {

        Resources resources = context.getResources();
        mPlaces = resources.getStringArray(R.array.places);
        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
        LENGTH = a.length();
        for (int i = 0; i < a.length(); i++) {
            placesPictures.add(a.getDrawable(i));
        }
        a.recycle();
    }

    @Override
    public CardFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CardFragment.ViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(CardFragment.ViewHolder holder, int position) {
        holder.picture.setImageDrawable(placesPictures.get(position % placesPictures.size()));
        holder.name.setText(mPlaces[position % mPlaces.length]);

    }

    @Override
    public int getItemCount() {
        return LENGTH;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}