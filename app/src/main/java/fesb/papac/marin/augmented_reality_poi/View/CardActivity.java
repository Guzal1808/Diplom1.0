package fesb.papac.marin.augmented_reality_poi.View;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

import fesb.papac.marin.augmented_reality_poi.Model.ViewTypes;
import fesb.papac.marin.augmented_reality_poi.R;

/**
 * Created by Dementor on 04.03.2018.
 */

public class CardActivity extends AppCompatActivity {

    protected static ViewTypes viewType = ViewTypes.AR;
    FloatingActionButton fabType, fabAr, fabMap, fabList;
    Animation FabClose, FabOpen, RotateBackward, RotateForward;

    private DrawerLayout mDrawerLayout;
    boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity);
        // Adding Toolbar to Main screen
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        // Setting ViewPager for each Tabs
        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
//        // Set Tabs inside Toolbar

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
    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new CardFragment(), "Card");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

}
