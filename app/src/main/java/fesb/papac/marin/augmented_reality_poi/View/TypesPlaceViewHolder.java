package fesb.papac.marin.augmented_reality_poi.View;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import fesb.papac.marin.augmented_reality_poi.Model.CategoryPlace;
import fesb.papac.marin.augmented_reality_poi.Model.TypePlace;
import fesb.papac.marin.augmented_reality_poi.Model.ViewTypes;
import fesb.papac.marin.augmented_reality_poi.R;

import static fesb.papac.marin.augmented_reality_poi.Model.ViewTypes.*;


public class TypesPlaceViewHolder extends ChildViewHolder {

    private TextView childTextView;
    private ImageView childImageView;
    static ViewTypes viewType;
    private View view;
    String childName;

    public TypesPlaceViewHolder(View itemView) {
        super(itemView);
        view=itemView;
        childTextView = (TextView) itemView.findViewById(R.id.list_item_artist_name);
        childImageView = itemView.findViewById(R.id.list_item_artist_icon);
        childTextView.setOnClickListener(v -> {
            viewType= CardActivity.viewType;
            Context context = v.getContext();
            Resources resources = context.getResources();
            switch (viewType)
            {
                case AR:{
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra(MainActivity.EXTRA_POSITION, childName);
                    context.startActivity(intent);
                    break;
                }
                case MAP:{
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra(MapActivity.EXTRA_POSITION, childName);
                    context.startActivity(intent);
                    break;
                }
            }
        });
    }

    public void setArtistName(String name) {
        childTextView.setText(name);
    }

    public void setKeyWord(String key)
    {
        try {
            childImageView.setImageBitmap(BitmapFactory.decodeResource(view.getResources(), R.drawable.class.getField(key+"_icn").getInt(null)));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            childImageView.setImageResource(R.drawable.cityscape);
        } finally {
            this.childName=key;
        }

    }
}
