package com.facundoaramayo.testrappikotlin.data;

import android.app.Application;
import android.location.Location;
import com.facundoaramayo.testrappikotlin.utils.Tools;

public class ThisApplication extends Application {


    private static ThisApplication mInstance;
    private Location location = null;
    private int fcm_count = 0;
    private final int FCM_MAX_COUNT = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Tools.initImageLoader(getApplicationContext());

    }

    public static synchronized ThisApplication getInstance() {
        return mInstance;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
