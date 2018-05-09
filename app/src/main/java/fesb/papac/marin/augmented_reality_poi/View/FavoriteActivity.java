package fesb.papac.marin.augmented_reality_poi.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import fesb.papac.marin.augmented_reality_poi.Adapters.DatabaseHandler;
import fesb.papac.marin.augmented_reality_poi.Adapters.FavoriteAdapter;
import fesb.papac.marin.augmented_reality_poi.Model.Favorite;
import fesb.papac.marin.augmented_reality_poi.Model.PointOfInterest;
import fesb.papac.marin.augmented_reality_poi.R;

public class FavoriteActivity extends Fragment{

    public static DatabaseHandler database;
    RecyclerView recyclerView;
    List<Favorite> favorite;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        database = new DatabaseHandler(recyclerView.getContext());
        //database.deleteAllRecords();
        initData();
        return recyclerView;
    }


    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData()
    {
        Gson gson = new Gson();
        favorite = database.readAllFavorite();
        for (Favorite fav : favorite) {
            //database.deleteFavorite(fav.getId());
            fav.setPlace(gson.fromJson(fav.getDetails(), PointOfInterest.class));
        }
        FavoriteAdapter adapter = new FavoriteAdapter(favorite, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

    }
}
