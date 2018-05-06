package fesb.papac.marin.augmented_reality_poi.View;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import fesb.papac.marin.augmented_reality_poi.Model.ViewTypes;
import fesb.papac.marin.augmented_reality_poi.R;

public class MainCardActivity extends Navigation {
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
