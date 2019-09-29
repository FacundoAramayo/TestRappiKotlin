package com.facundoaramayo.testrappikotlin.data

object Constant {

    var WEB_URL = "https://developers.zomato.com/"

    const val CITY_ZOOM = 10f

    // Este valor límite se utiliza para dar paginación (solicitud y visualización) para disminuir el payload
    const val LIMIT_PLACE_REQUEST = 40
    const val LIMIT_LOADMORE = 40

    fun getURLimgPlace(file_name: String): String {
        return file_name
    }


}
