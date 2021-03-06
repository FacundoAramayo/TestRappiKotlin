package com.facundoaramayo.testrappikotlin.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.ImageView;
import android.widget.Toast;
import com.facundoaramayo.testrappikotlin.R;
import com.facundoaramayo.testrappikotlin.data.AppConfig;
import com.facundoaramayo.testrappikotlin.data.SharedPref;
import com.facundoaramayo.testrappikotlin.data.ThisApplication;
import com.facundoaramayo.testrappikotlin.model.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Tools {

    public static boolean needRequestPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public static boolean isLollipopOrHigher() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public static void systemBarLollipop(Activity act) {
        if (isLollipopOrHigher()) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Tools.colorDarker(new SharedPref(act).getThemeColorInt()));
        }
    }

    public static boolean checkConnection(Context context) {
        ConnectionDetector conn = new ConnectionDetector(context);
        return conn.isConnectingToInternet();
    }

    public static void initImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(AppConfig.IMAGE_CACHE)
                .cacheOnDisk(AppConfig.IMAGE_CACHE)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .threadPoolSize(3)
                .memoryCache(new WeakMemoryCache())
                .build();

        ImageLoader.getInstance().init(config);
    }

    public static DisplayImageOptions getGridOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(AppConfig.IMAGE_CACHE)
                .cacheOnDisk(AppConfig.IMAGE_CACHE)
                .build();

        return options;
    }



    public static int getGridSpanCount(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.item_place_width);
        return Math.round(screenWidth / cellWidth);
    }

    public static GoogleMap configStaticMap(Activity act, GoogleMap googleMap, Place place) {
        // set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Enable / Disable zooming controls
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        // Enable / Disable my location button
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        // Enable / Disable Compass icon
        googleMap.getUiSettings().setCompassEnabled(false);
        // Enable / Disable Rotate gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        // enable traffic layer
        googleMap.isTrafficEnabled();
        googleMap.setTrafficEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View marker_view = inflater.inflate(R.layout.maps_marker, null);
        ((ImageView) marker_view.findViewById(R.id.marker_bg)).setColorFilter(act.getResources().getColor(R.color.colorPrimaryDark));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(place.getPosition()).zoom(12).build();
        MarkerOptions markerOptions = new MarkerOptions().position(place.getPosition());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Tools.createBitmapFromView(act, marker_view)));
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        return googleMap;
    }

    public static GoogleMap configActivityMaps(GoogleMap googleMap) {
        // set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Enable / Disable zooming controls
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // Enable / Disable Compass icon
        googleMap.getUiSettings().setCompassEnabled(true);
        // Enable / Disable Rotate gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        return googleMap;
    }

    public static void dialNumber(Context ctx, String phone) {
        try {
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + phone));
            ctx.startActivity(i);
        } catch (Exception e) {
            Toast.makeText(ctx, "Cannot dial number", Toast.LENGTH_SHORT);
        }
    }

    public static void directUrl(Context ctx, String website) {
        String url = website;
        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "http://" + url;
        }
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        ctx.startActivity(i);
    }

    public static Bitmap createBitmapFromView(Activity act, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    public static int colorDarker(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f; // value component
        return Color.HSVToColor(hsv);
    }



    private static float calculateDistance(LatLng from, LatLng to) {
        Location start = new Location("");
        start.setLatitude(from.latitude);
        start.setLongitude(from.longitude);

        Location end = new Location("");
        end.setLatitude(to.latitude);
        end.setLongitude(to.longitude);

        float distInMeters = start.distanceTo(end);
        float resultDist = 0;
        if (AppConfig.DISTANCE_METRIC_CODE.equals("KILOMETER")) {
            resultDist = distInMeters / 1000;
        } else {
            resultDist = (float) (distInMeters * 0.000621371192);
        }
        return resultDist;
    }

    public static List<Place> filterItemsWithDistance(Activity act, List<Place> items) {
        if (AppConfig.SORT_BY_DISTANCE) { // checking for distance sorting
            LatLng curLoc = Tools.getCurLocation(act);
            if (curLoc != null) {
                return Tools.getSortedDistanceList(items, curLoc);
            }
        }
        return items;
    }

    public static List<Place> itemsWithDistance(Context ctx, List<Place> items) {
        if (AppConfig.SORT_BY_DISTANCE) { // checking for distance sorting
            LatLng curLoc = Tools.getCurLocation(ctx);
            if (curLoc != null) {
                return Tools.getDistanceList(items, curLoc);
            }
        }
        return items;
    }

    public static List<Place> getDistanceList(List<Place> places, LatLng curLoc) {
        if (places.size() > 0) {
            for (Place p : places) {
//                p.distance = calculateDistance(curLoc, p.getPosition());
            }
        }
        return places;
    }

    public static List<Place> getSortedDistanceList(List<Place> places, LatLng curLoc) {
        List<Place> result = new ArrayList<>();
        if (places.size() > 0) {
//            for (int i = 0; i < places.size(); i++) {
//                Place p = places.get(i);
//                p.distance = calculateDistance(curLoc, p.getPosition());
//                result.add(p);
//            }
//            Collections.sort(result, new Comparator<Place>() {
//                @Override
//                public int compare(final Place p1, final Place p2) {
//                    return Float.compare(p1.distance, p2.distance);
//                }
//            });
        } else {
            return places;
        }
        return result;
    }

    public static LatLng getCurLocation(Context ctx) {
        if (PermissionUtil.isLocationGranted(ctx)) {
            LocationManager manager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Location loc = ThisApplication.getInstance().getLocation();
                if (loc == null) {
                    loc = getLastKnownLocation(ctx);
                    ThisApplication.getInstance().setLocation(loc);
                }
                if (loc != null) {
                    return new LatLng(loc.getLatitude(), loc.getLongitude());
                }
            }
        }
        return null;
    }

    public static Location getLastKnownLocation(Context ctx) {
        LocationManager mLocationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = Tools.requestLocationUpdate(mLocationManager);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        mLocationManager.removeUpdates(locationListener);
        return bestLocation;
    }

    private static LocationListener requestLocationUpdate(LocationManager manager) {
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        return locationListener;
    }

    public static String getFormatedDistance(float distance) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(1);
        return df.format(distance) + " " + AppConfig.DISTANCE_METRIC_STR;
    }

    public static List<Place> convertRestaurantContainerToPlace(List<RestaurantContainer> listContainer){
        List<Place> places = new ArrayList<>();
        int listSize = listContainer.size();
        Log.d("LOG-CONVERSION", "Container size: " + listSize);
        Log.d("LOG-CONVERSION", "data: " + listContainer.get(0).getRestaurant().getName());
        for (int i = 0; i < listSize; i++){
            RestaurantItem resItem = listContainer.get(i).getRestaurant();
            ResLocation resLocation = resItem.getLocation();

            Log.d("LOG-CONVERSION " + i, "RestaurantItem id: " + resItem.getId());
            Log.d("LOG-CONVERSION " + i, "RestaurantItem name: " + resItem.getName());

            String priceCategory = "";
            switch (resItem.getPrice_range()) {
                case 1:
                    priceCategory = "$";
                    break;
                case 2:
                    priceCategory = "$$";
                    break;
                case 3:
                    priceCategory = "$$$";
                    break;
                case 4:
                    priceCategory = "$$$$";
                    break;
            }
            String hasOnlineDelivery;
            String isDeliveringNow;
            String hasTableBooking;
            if (resItem.getHas_online_delivery() == 1){
                hasOnlineDelivery = "Available";
            } else {
                hasOnlineDelivery = "Not available";
            }
            if (resItem.is_delivering_now() == 1){
                isDeliveringNow = "Delivering... call now!";
            } else {
                isDeliveringNow = "Not available right now";
            }
            if (resItem.getHas_table_booking() == 1){
                hasTableBooking = "Book a table now!";
            } else {
                hasTableBooking = "Has not available tables for booking";
            }


            Place localPlace = new Place();
            localPlace.setPlace_id(resItem.getId());
            localPlace.setName(resItem.getName());
            localPlace.setImage(resItem.getThumb());
            localPlace.setAddress(resLocation.getAddress());
            localPlace.setLat(Double.parseDouble(String.valueOf(resLocation.getLatitude())));
            localPlace.setLng(Double.parseDouble(String.valueOf(resLocation.getLongitude())));
            localPlace.setPhone(resItem.getPhone_numbers());
            localPlace.setWebsite(resItem.getUrl());
            localPlace.setDescription(
                    "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "<title>" + resItem.getCuisines() + "</title>"+
                            "</head>" +
                            "<body>\n" +
                            "<p><b>Cuisiness: </b>" + resItem.getCuisines() + "</p>"+
                            "<p><b>Price range: </b>" + priceCategory + "</p>"+
                            "<p><b>Currency: </b>" + resItem.getCurrency() + "</p>"+
                            "<p><b>Average cost for two: </b>" + resItem.getCurrency() + resItem.getAverage_cost_for_two() +  "</p>"+
                            "<p><b>Online delivery: </b>" + hasOnlineDelivery + "</p>"+
                            "<p><b>Delivery: </b>" + isDeliveringNow + "</p>"+
                            "<p><b>Bookings: </b>" + hasTableBooking + "</p>"+
                            "</body>\n" +
                            "</html>");

            //TODO: Post - Agregar fotos y reviews

            places.add(localPlace);
        }

        return places;
    }


    public static String getStringResource(Context context, int id){
        return (String) context.getResources().getString(id);
    }


    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
