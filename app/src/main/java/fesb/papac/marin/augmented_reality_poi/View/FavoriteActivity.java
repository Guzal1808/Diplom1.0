package fesb.papac.marin.augmented_reality_poi.View;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fesb.papac.marin.augmented_reality_poi.Adapters.DatabaseHandler;
import fesb.papac.marin.augmented_reality_poi.Adapters.FavoriteAdapter;
import fesb.papac.marin.augmented_reality_poi.Model.Favorite;
import fesb.papac.marin.augmented_reality_poi.R;

public class FavoriteActivity extends Fragment {

    public static DatabaseHandler database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        database = new DatabaseHandler(recyclerView.getContext());
        List<Favorite> favorite = database.readAllFavorite();
        FavoriteAdapter adapter = new FavoriteAdapter(favorite);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        return recyclerView;
    }

}
