package com.facundoaramayo.testrappikotlin.data;

public class Constant {

    public static String WEB_URL = "https://developers.zomato.com/";

    public static final float city_zoom = 10;

    public static String getURLimgPlace(String file_name) {
        return file_name;
    }

    // Este valor límite se utiliza para dar paginación (solicitud y visualización) para disminuir el payload
    public static final int LIMIT_PLACE_REQUEST = 40;
    public static final int LIMIT_LOADMORE = 40;



}
