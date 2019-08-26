package com.facundoaramayo.testrappikotlin.adapter

import android.app.Activity
import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.nostra13.universalimageloader.core.ImageLoader

import com.facundoaramayo.testrappikotlin.R
import com.facundoaramayo.testrappikotlin.widget.TouchImageView

class AdapterFullScreenImage(private val act: Activity, private val imagePaths: List<String>) : PagerAdapter() {
    private var inflater: LayoutInflater? = null
    private val imgloader = ImageLoader.getInstance()

    override fun getCount(): Int {
        return this.imagePaths.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imgDisplay: TouchImageView
        inflater = act.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewLayout = inflater!!.inflate(R.layout.item_fullscreen_image, container, false)

        imgDisplay = viewLayout.findViewById(R.id.imgDisplay)

        imgloader.displayImage(imagePaths[position], imgDisplay)
        container.addView(viewLayout)

        return viewLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)

    }

}
