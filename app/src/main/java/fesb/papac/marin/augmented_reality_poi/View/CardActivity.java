package fesb.papac.marin.augmented_reality_poi.View;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import fesb.papac.marin.augmented_reality_poi.Model.ViewTypes;
import fesb.papac.marin.augmented_reality_poi.R;

public class CardActivity extends Navigation {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private RecyclerView recyclerView;
    protected static ViewTypes viewType = ViewTypes.AR;

    FloatingActionButton fabType, fabAr, fabMap, fabList;
    Animation FabClose, FabOpen, RotateBackward, RotateForward;

    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getLayoutInflater().inflate(R.layout.container, (ViewGroup) findViewById(R.id.ly_content));

        initCategory();

    }


    public void initCategory() {

        fabAr =  findViewById(R.id.fab_ar);
        fabMap = findViewById(R.id.fab_map);
        fabType = findViewById(R.id.fab_type);
        fabType.setVisibility(View.VISIBLE);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        RotateBackward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        RotateForward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);

        fabType.setOnClickListener(v -> {
            if (isOpen) {
                changeFABAnimation(FabClose);
                fabType.startAnimation(RotateBackward);
                changeFABState(false);
                isOpen = false;
            } else {
                changeFABAnimation(FabOpen);
                fabType.startAnimation(RotateForward);
                changeFABState(true);
                isOpen = true;
            }
        });

        fabAr.setOnClickListener(v ->
        {
            viewType = ViewTypes.AR;
            fabType.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.my_amber)));
            closeFAB();

        });

        fabMap.setOnClickListener(v -> {
            viewType = ViewTypes.MAP;
            fabType.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.my_cyan)));
            closeFAB();
        });

        frameLayout = (FrameLayout) findViewById(R.id.container);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CardFragment fragment = new CardFragment();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void changeFABState(boolean state)
    {
        fabAr.setClickable(state);
        fabMap.setClickable(state);
    }

    private void changeFABAnimation(Animation animation)
    {
        fabAr.startAnimation(animation);
        fabMap.startAnimation(animation);
    }

    private void closeFAB()
    {
        changeFABAnimation(FabClose);
        fabType.startAnimation(RotateBackward);
        changeFABState(false);
        changeFABAnimation(FabClose);
        fabType.startAnimation(RotateBackward);
        changeFABState(false);
        isOpen = false;
    }
}


/*
package fesb.papac.marin.augmented_reality_poi.View;

import android.content.Context;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import fesb.papac.marin.augmented_reality_poi.Adapters.CategoryAdapter;
import fesb.papac.marin.augmented_reality_poi.Helper.Connection;
import fesb.papac.marin.augmented_reality_poi.Helper.PlacesDataFactory;
import fesb.papac.marin.augmented_reality_poi.Model.ViewTypes;
import fesb.papac.marin.augmented_reality_poi.R;

*/
/**
 * Created by Dementor on 04.03.2018.
 *//*


public class CardActivity extends AppCompatActivity {

    public CategoryAdapter adapter;


    protected static ViewTypes viewType = ViewTypes.AR;
    FloatingActionButton fabType, fabAr, fabMap, fabList;
    Animation FabClose, FabOpen, RotateBackward, RotateForward;

    boolean isOpen = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        PlacesDataFactory data = new PlacesDataFactory(getApplicationContext());
        adapter = new CategoryAdapter(data.makeCategoryPlaces());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        fabAr = findViewById(R.id.fab_ar);
        fabMap = findViewById(R.id.fab_map);
        fabType = findViewById(R.id.fab_type);

        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        RotateBackward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        RotateForward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);

        fabType.setOnClickListener(v -> {
            if (isOpen) {
                changeFABAnimation(FabClose);
                fabType.startAnimation(RotateBackward);
                changeFABState(false);
                isOpen = false;
            } else {
                changeFABAnimation(FabOpen);
                fabType.startAnimation(RotateForward);
                changeFABState(true);
                isOpen = true;
            }
        });

        fabAr.setOnClickListener(v ->
        {
            viewType = ViewTypes.AR;
            fabType.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.my_amber)));
            closeFAB();

        });

        fabMap.setOnClickListener(v -> {
            viewType = ViewTypes.MAP;
            fabType.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.my_cyan)));
            closeFAB();
        });

        ActionBar supportActionBar = getSupportActionBar();


        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(), R.color.white, getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        Connection connection = new Connection(getApplicationContext());
        connection.checkConnection();

    }

    private void changeFABState(boolean state)
    {
        fabAr.setClickable(state);
        fabMap.setClickable(state);
    }

    private void changeFABAnimation(Animation animation)
    {
        fabAr.startAnimation(animation);
        fabMap.startAnimation(animation);
    }

    private void closeFAB()
    {
        changeFABAnimation(FabClose);
        fabType.startAnimation(RotateBackward);
        changeFABState(false);
        changeFABAnimation(FabClose);
        fabType.startAnimation(RotateBackward);
        changeFABState(false);
        isOpen = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
        adapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        adapter.onRestoreInstanceState(savedInstanceState);
    }


}
*/
