package fesb.papac.marin.augmented_reality_poi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fesb.papac.marin.augmented_reality_poi.Model.Favorite;
import fesb.papac.marin.augmented_reality_poi.R;
import fesb.papac.marin.augmented_reality_poi.View.MainActivity;
import fesb.papac.marin.augmented_reality_poi.View.PlaceDetailActivity;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteInfoHolder> {

    List<Favorite> favorites;
    Context context;

    public FavoriteAdapter(List<Favorite> favorites, Context context) {
        this.favorites = favorites;
        this.context = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FavoriteInfoHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.places_view, viewGroup, false);
        FavoriteInfoHolder pvh = new FavoriteInfoHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(FavoriteInfoHolder favoriteInfoHolder, int i) {
        favoriteInfoHolder.name.setText(favorites.get(i).name);
        String type=  favorites.get(i).getPlace().getTypes().get(0)+"_icn";
        try {
            favoriteInfoHolder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.class.getField(type).getInt(null)));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            favoriteInfoHolder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.cityscape));
        }
        favoriteInfoHolder.address.setText(favorites.get(i).getPlace().getVicinity());
        favoriteInfoHolder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PlaceDetailActivity.class);
                    intent.putExtra(PlaceDetailActivity.PLACES_DB_ID,String.valueOf(favorites.get(i).getId()));
                    context.startActivity(intent);
                }

        );

    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }


    public class FavoriteInfoHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView address;
        private ImageView image;

        public FavoriteInfoHolder(final View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_title);
            image = itemView.findViewById(R.id.list_avatar);
            address = itemView.findViewById(R.id.list_desc);

            /*likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    if (db.deleteFavorite(favorites.get(getAdapterPosition()).getId()) == true) {
                        favorites.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                }
            });*/
        }
    }


}
