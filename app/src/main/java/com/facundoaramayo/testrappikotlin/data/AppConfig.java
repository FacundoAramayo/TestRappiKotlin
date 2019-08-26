package com.facundoaramayo.testrappikotlin.data;

public class AppConfig {

    // flag for save image offline
    public static final boolean IMAGE_CACHE = true;

    // if place data more than 200 items set TRUE
    public static final boolean LAZY_LOAD = false;

    // when user enable gps, places will sort by distance
    public static final boolean SORT_BY_DISTANCE = true;

    // distance metric, fill with KILOMETER or MILE only
    public static final String DISTANCE_METRIC_CODE = "KILOMETER";

    // related to UI display string
    public static final String DISTANCE_METRIC_STR = "Km";


}
