package com.facundoaramayo.testrappikotlin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.facundoaramayo.testrappikotlin.data.DatabaseHandler;
import com.facundoaramayo.testrappikotlin.fragment.FragmentCategory;
import com.facundoaramayo.testrappikotlin.fragment.FragmentHome;
import com.facundoaramayo.testrappikotlin.utils.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ActivityMainPlaces extends AppCompatActivity {

    boolean home = false;

    private ImageLoader imgloader = ImageLoader.getInstance();

    public ActionBar actionBar;
    public Toolbar toolbar;
    private int[] cat;
    private com.getbase.floatingactionbutton.FloatingActionsMenu fab;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_search_by_location;
    private com.getbase.floatingactionbutton.FloatingActionButton fab_search_by_name;
    private NavigationView navigationView;
    private DatabaseHandler db;
    private RelativeLayout nav_header_lyt;

    static ActivityMainPlaces activityMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_places);
        activityMain = this;

        if (!imgloader.isInited()) Tools.initImageLoader(this);
        fab = findViewById(R.id.floating_action_button);
        fab_search_by_location = findViewById(R.id.fb_location);
        fab_search_by_name = findViewById(R.id.fb_restaurant_name);
        db = new DatabaseHandler(this);

        /*
        Código para configurar las preferencias una vez que el usuario realizó con éxito su registro
         */
        final String PREFS_NAME = "MAIN_PREF";
        final String PREF_VERSION_CODE_KEY = "testrappi.build.VERSION_CODE_KEY";
        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;
        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();


        initToolbar();
        initDrawerMenu();
        prepareImageLoader();
        cat = getResources().getIntArray(R.array.id_category);

        // first drawer view
        onItemSelected(R.id.nav_home, getString(R.string.title_home));

        fab_search_by_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityMainPlaces.this, ActivitySearch.class);
                startActivity(i);
            }
        });

        fab_search_by_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityMainPlaces.this, ActivityMaps.class);
                startActivity(i);
            }
        });

        // for system bar in lollipop
        Tools.systemBarLollipop(this);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initDrawerMenu() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return onItemSelected(item.getItemId(), item.getTitle().toString());
            }
        });

        View nav_header = navigationView.getHeaderView(0);
        nav_header_lyt = nav_header.findViewById(R.id.nav_header_lyt);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            doExitApp();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            Intent i = new Intent(getApplicationContext(), ActivitySetting.class);
//            startActivity(i);
//        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onItemSelected(int id, String title) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch (id) {
            //home
            case R.id.nav_home:
                fragment = new FragmentHome();
                home = true;
                actionBar.setTitle(title);
                break;
            //all restaurants
            case R.id.nav_all:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, -1);
                actionBar.setTitle(title);
                break;
            // favorites
            case R.id.nav_favorites:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, -2);
                actionBar.setTitle(title);
                break;
            // map
            case R.id.nav_map:
                Intent j = new Intent(this, ActivityMaps.class);
                home = false;
                startActivity(j);
                break;
            //categories sub menu
            case R.id.nav_delivery:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[10]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_dine_out:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[0]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_nightlife:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[1]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_catching_up:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[2]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_takeaway:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[3]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_cafes:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[4]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_daily_menus:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[5]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_breakfast:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[6]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_lunch:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[7]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_dinner:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[8]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_pubs_bars:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[9]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_pocket_friendly_delivery:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[11]);
                actionBar.setTitle(title);
                break;
            case R.id.nav_club_lounges:
                fragment = new FragmentCategory();
                home = false;
                bundle.putInt(FragmentCategory.TAG_CATEGORY, cat[11]);
                actionBar.setTitle(title);
                break;
            default:
                break;

            /* IMPORTANTE : cat[index_array], index is start from 0
             */
        }

        if (home) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        } else if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_content, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, R.string.press_again_exit_app, Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    private void prepareImageLoader() {
        Tools.initImageLoader(this);
    }


    @Override
    protected void onResume() {
        if (!imgloader.isInited()) Tools.initImageLoader(this);
        if (actionBar != null) {
            Tools.systemBarLollipop(this);
        }
        super.onResume();
    }

    static boolean active = false;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        active = false;
    }

    public static void animateFab(final boolean hide) {
        com.getbase.floatingactionbutton.FloatingActionsMenu f_ab = activityMain.findViewById(R.id.floating_action_button);
        int moveY = hide ? (2 * f_ab.getHeight()) : 0;
        f_ab.animate().translationY(moveY).setStartDelay(100).setDuration(400).start();
    }
}
