package fesb.papac.marin.augmented_reality_poi.View;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fesb.papac.marin.augmented_reality_poi.Adapters.ContentAdapter;
import fesb.papac.marin.augmented_reality_poi.Model.ViewTypes;
import fesb.papac.marin.augmented_reality_poi.R;


public class CardFragment extends Fragment {

    static ViewTypes viewType;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_card, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            name = (TextView) itemView.findViewById(R.id.card_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewType= CardActivity.viewType;
                    Context context = v.getContext();
                    Resources resources = context.getResources();
                    switch (viewType)
                    {
                        case AR:{
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra(MainActivity.EXTRA_POSITION, resources.getStringArray(R.array.placesType)[getAdapterPosition()]);
                            context.startActivity(intent);
                            break;
                        }
                        case MAP:{
                            Intent intent = new Intent(context, MapActivity.class);
                            intent.putExtra(MapActivity.EXTRA_POSITION, resources.getStringArray(R.array.placesType)[getAdapterPosition()]);
                            context.startActivity(intent);
                            break;
                        }
                    }
                }
            });
        }
    }
}
