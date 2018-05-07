package fesb.papac.marin.augmented_reality_poi.View;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import fesb.papac.marin.augmented_reality_poi.R;

public class Navigation extends AppCompatActivity

{
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private FrameLayout frameLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_view);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toogle);
        toogle.syncState();

        nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        nvDrawer.setItemIconTintList(null);
        setupDrawerContent(nvDrawer);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void selectDrawerItem(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.menu_discover:
                initCategory();
                break;
            case R.id.menu_favorite:
                initFavorite();
                break;
        }
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    public void initCategory() {
        frameLayout = (FrameLayout) findViewById(R.id.container);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        CardFragment fragment = new CardFragment();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    public void initFavorite() {
        frameLayout = (FrameLayout) findViewById(R.id.container);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FavoriteActivity fragment = new FavoriteActivity();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }


}
