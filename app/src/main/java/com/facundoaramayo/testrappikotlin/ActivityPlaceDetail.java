package com.facundoaramayo.testrappikotlin;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import com.facundoaramayo.testrappikotlin.adapter.AdapterImageList;
import com.facundoaramayo.testrappikotlin.connection.callbacks.CallbackPlaceDetails;
import com.facundoaramayo.testrappikotlin.data.Constant;
import com.facundoaramayo.testrappikotlin.data.DatabaseHandler;
import com.facundoaramayo.testrappikotlin.data.SharedPref;
import com.facundoaramayo.testrappikotlin.model.Images;
import com.facundoaramayo.testrappikotlin.model.Place;
import com.facundoaramayo.testrappikotlin.utils.Tools;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.nostra13.universalimageloader.core.ImageLoader;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

public class ActivityPlaceDetail extends AppCompatActivity {

    private static final String EXTRA_OBJ = "key.EXTRA_OBJ";
    private static final String EXTRA_NOTIF_FLAG = "key.EXTRA_NOTIF_FLAG";

    public static void navigate(AppCompatActivity activity, View sharedView, Place p) {
        Intent intent = new Intent(activity, ActivityPlaceDetail.class);
        intent.putExtra(EXTRA_OBJ, p);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedView, EXTRA_OBJ);
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }

    private Place place = null;
    private ImageLoader imgloader = ImageLoader.getInstance();
    private FloatingActionButton fab;
    private WebView description = null;
    private View parent_view = null;
    private GoogleMap googleMap;
    private DatabaseHandler db;

    private boolean onProcess = false;
    private boolean isFromNotif = false;
    private Call<CallbackPlaceDetails> callback;
    private View lyt_progress;
    private View lyt_distance;
    private float distance = -1;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        parent_view = findViewById(android.R.id.content);

        if (!imgloader.isInited()) Tools.initImageLoader(this);

        db = new DatabaseHandler(this);
        // animación
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJ);

        place = (Place) getIntent().getSerializableExtra(EXTRA_OBJ);
        isFromNotif = getIntent().getBooleanExtra(EXTRA_NOTIF_FLAG, false);

        fab = findViewById(R.id.fab);
        lyt_progress = findViewById(R.id.lyt_progress);
        lyt_distance = findViewById(R.id.lyt_distance);
        imgloader.displayImage(Constant.getURLimgPlace(place.getImage()), (ImageView) findViewById(R.id.image));
        distance = place.getDistance();

        fabToggle();
        setupToolbar(place.getName());
        initMap();

        // botón agregar a favoritos
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.isFavoritesExist(place.getPlace_id())) {
                    db.deleteFavorites(place.getPlace_id());
                    Snackbar.make(parent_view, place.getName() + " " + getString(R.string.remove_favorite), Snackbar.LENGTH_SHORT).show();
                } else {
                    db.addFavorites(place.getPlace_id());
                    Snackbar.make(parent_view, place.getName() + " " + getString(R.string.add_favorite), Snackbar.LENGTH_SHORT).show();
                }
                fabToggle();
            }
        });

        // para versión lollipop
        Tools.systemBarLollipop(this);
    }


    private void displayData(Place p) {
        ((TextView) findViewById(R.id.address)).setText(p.getAddress());
        ((TextView) findViewById(R.id.phone)).setText(p.getPhone().equals("-") || p.getPhone().trim().equals("") ? getString(R.string.no_phone_number) : p.getPhone());
        ((TextView) findViewById(R.id.website)).setText(p.getWebsite().equals("-") || p.getWebsite().trim().equals("") ? getString(R.string.no_website) : p.getWebsite());

        description = findViewById(R.id.description);
        String html_data = "<style>img{max-width:100%;height:auto;} iframe{width:100%;}</style> ";
        html_data += p.getDescription();
        description.getSettings().setBuiltInZoomControls(true);
        description.setBackgroundColor(Color.TRANSPARENT);
        description.setWebChromeClient(new WebChromeClient());
        description.loadData(html_data, "text/html; charset=UTF-8", null);
        description.getSettings().setJavaScriptEnabled(true);
        description.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        if (distance == -1) {
            lyt_distance.setVisibility(View.GONE);
        } else {
            lyt_distance.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.distance)).setText(Tools.getFormatedDistance(distance));
        }

        setImageGallery(db.getListImageByPlaceId(p.getPlace_id()));
    }

    @Override
    protected void onResume() {
        if (!imgloader.isInited()) Tools.initImageLoader(getApplicationContext());
        loadPlaceData();
        if (description != null) description.onResume();
        super.onResume();
    }

    // this method name same with android:onClick="clickLayout" at layout xml
    public void clickLayout(View view) {
        switch (view.getId()) {
            case R.id.lyt_address:
                //TODO: Abrir mapa en la ubicación del restaurant
//                if (!place.isDraft()) {
//                    Uri uri = Uri.parse("http://maps.google.com/maps?q=loc:" + place.lat + "," + place.lng);
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//                }
                break;
            case R.id.lyt_phone:
                //TODO: Call intent para el restaurant, revisar aquellos que tienen varios teléfonos
//                if (!place.isDraft() && !place.phone.equals("-") && !place.phone.trim().equals("")) {
//                    Tools.dialNumber(this, place.phone);
//                } else {
//                    Snackbar.make(parent_view, R.string.fail_dial_number, Snackbar.LENGTH_SHORT).show();
//                }
                break;
            case R.id.lyt_website:
                //TODO: Web browser intent al sitio del restaurant
//                if (!place.isDraft() && !place.website.equals("-") && !place.website.trim().equals("")) {
//                    Tools.directUrl(this, place.website);
//                } else {
//                    Snackbar.make(parent_view, R.string.fail_open_website, Snackbar.LENGTH_SHORT).show();
//                }
                break;
        }
    }

    private void setImageGallery(List<Images> images) {
        // add optional image into list
        List<Images> new_images = new ArrayList<>();
        final ArrayList<String> new_images_str = new ArrayList<>();
        new_images.add(new Images(place.getPlace_id(), place.getImage()));
        new_images.addAll(images);
        for (Images img : new_images) {
            new_images_str.add(Constant.getURLimgPlace(img.getImageUrl()));
        }

        RecyclerView galleryRecycler = findViewById(R.id.galleryRecycler);
        galleryRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        AdapterImageList adapter = new AdapterImageList(new_images);
        galleryRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterImageList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String viewModel, int pos) {
                Intent i = new Intent(ActivityPlaceDetail.this, ActivityFullScreenImage.class);
                i.putExtra(ActivityFullScreenImage.Companion.getEXTRA_POS(), pos);
                i.putStringArrayListExtra(ActivityFullScreenImage.Companion.getEXTRA_IMGS(), new_images_str);
                startActivity(i);
            }
        });
    }

    private void fabToggle() {
        if (db.isFavoritesExist(place.getPlace_id())) {
            fab.setImageResource(R.drawable.ic_nav_favorites);
        } else {
            fab.setImageResource(R.drawable.ic_nav_favorites_outline);
        }
    }

    private void setupToolbar(String name) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        ((TextView) findViewById(R.id.toolbar_title)).setText(name);

        final CollapsingToolbarLayout collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        collapsing_toolbar.setContentScrimColor(new SharedPref(this).getThemeColorInt());
        ((AppBarLayout) findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (collapsing_toolbar.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapsing_toolbar)) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_details, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            backAction();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initMap() {
        if (googleMap == null) {
            MapFragment mapFragment1 = (MapFragment) getFragmentManager().findFragmentById(R.id.mapPlaces);
            mapFragment1.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap gMap) {
                    googleMap = gMap;
                    if (googleMap == null) {
                        Snackbar.make(parent_view, R.string.unable_create_map, Snackbar.LENGTH_SHORT).show();
                    } else {
                        // config map
                        googleMap = Tools.configStaticMap(ActivityPlaceDetail.this, googleMap, place);
                    }
                }
            });
        }

        findViewById(R.id.bt_navigate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Implementar aquí los cambios dinamicos de ubicación en maps
                //Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + place.lat + "," + place.lng));
                Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + 0 + "," + 0));

                startActivity(navigation);
            }
        });
        findViewById(R.id.bt_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlaceInMap();
            }
        });
        findViewById(R.id.map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlaceInMap();
            }
        });
    }

    private void openPlaceInMap() {
        Intent intent = new Intent(ActivityPlaceDetail.this, ActivityMaps.class);
        intent.putExtra(ActivityMaps.EXTRA_OBJ, place);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (callback != null && callback.isExecuted()) callback.cancel();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        backAction();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (description != null) description.onPause();
    }

    private void backAction() {
        if (isFromNotif) {
            Intent i = new Intent(this, ActivityMainPlaces.class);
            startActivity(i);
        }
        finish();
    }

    // places detail load with lazy scheme
    private void loadPlaceData() {
        place = db.getPlace(place.getPlace_id());
        if (place.isDraft()) {
//            if (Tools.checkConnection(this)) {
//                requestDetailsPlace(place.place_id);
//            } else {
//                onFailureRetry(getString(R.string.no_internet));
//            }
        } else {
            displayData(place);
        }
    }

}
