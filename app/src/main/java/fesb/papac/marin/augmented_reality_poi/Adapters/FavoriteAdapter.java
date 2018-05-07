package fesb.papac.marin.augmented_reality_poi.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import fesb.papac.marin.augmented_reality_poi.Model.Favorite;
import fesb.papac.marin.augmented_reality_poi.R;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteInfoHolder> {

    List<Favorite> favorites;


    public FavoriteAdapter(List<Favorite> favorites) {
        this.favorites = favorites;
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
        /*favoriteInfoHolder.description.setText(favorites.get(i).description);
        Picasso.with(favoriteInfoHolder.itemView.getContext())
                .load(favorites.get(i).photo)
                .into(favoriteInfoHolder.getPhoto());
        favoriteInfoHolder.likeButton.setLiked(true);
*/
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }


    public class FavoriteInfoHolder extends RecyclerView.ViewHolder {

        private TextView name;


        public FavoriteInfoHolder(final View itemView) {
            super(itemView);
           // cv = (CardView) itemView.findViewById(R.id.card_view);
            name = (TextView) itemView.findViewById(R.id.list_title);
            /*description = (TextView) itemView.findViewById(R.id.card_text);
            photo = (ImageView) itemView.findViewById(R.id.card_image);
            likeButton = (LikeButton) itemView.findViewById(R.id.star_button);
            final DatabaseHandler db = new DatabaseHandler(itemView.getContext());
            likeButton.setOnLikeListener(new OnLikeListener() {
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
