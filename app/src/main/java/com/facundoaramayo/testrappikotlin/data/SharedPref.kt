package com.facundoaramayo.testrappikotlin.data

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager

import com.facundoaramayo.testrappikotlin.R

class SharedPref(private val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val prefs: SharedPreferences

    /**
     * Refresh user data
     * When phone receive GCM notification this flag will be enable.
     * so when user open the app all data will be refresh
     */
    var isRefreshPlaces: Boolean
        get() = sharedPreferences.getBoolean(REFRESH_PLACES, false)
        set(need_refresh) = sharedPreferences.edit().putBoolean(REFRESH_PLACES, need_refresh).apply()

    val themeColor: String?
        get() = sharedPreferences.getString(THEME_COLOR_KEY, "")

    val themeColorInt: Int
        get() = if (themeColor == "") {
            context.resources.getColor(R.color.colorPrimary)
        } else Color.parseColor(themeColor)

    /**
     * To save last state request
     */
    var lastPlacePage: Int
        get() = sharedPreferences.getInt(LAST_PLACE_PAGE, 1)
        set(page) = sharedPreferences.edit().putInt(LAST_PLACE_PAGE, page).apply()

    init {
        sharedPreferences = context.getSharedPreferences("MAIN_PREF", Context.MODE_PRIVATE)
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    companion object {

        private val THEME_COLOR_KEY = "ciudadanovirtualdemo.data.THEME_COLOR_KEY"
        private val LAST_PLACE_PAGE = "LAST_PLACE_PAGE_KEY"

        // need refresh
        val REFRESH_PLACES = "ciudadanovirtualdemo.data.REFRESH_PLACES"
    }


}
