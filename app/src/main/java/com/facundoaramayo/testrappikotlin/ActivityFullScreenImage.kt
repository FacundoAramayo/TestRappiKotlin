package com.facundoaramayo.testrappikotlin

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView

import com.nostra13.universalimageloader.core.ImageLoader

import java.util.ArrayList

import com.facundoaramayo.testrappikotlin.adapter.AdapterFullScreenImage
import com.facundoaramayo.testrappikotlin.utils.Tools

class ActivityFullScreenImage : AppCompatActivity() {

    val EXTRA_POS = "key.EXTRA_POS"
    val EXTRA_IMGS = "key.EXTRA_IMGS"

    private val imgloader = ImageLoader.getInstance()
    private var adapter: AdapterFullScreenImage? = null
    private var viewPager: ViewPager? = null
    private var text_page: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        if (!imgloader.isInited) Tools.initImageLoader(this)
        viewPager = findViewById(R.id.pager)
        text_page = findViewById(R.id.text_page)

        var items = ArrayList<String>()
        val i = intent
        val position = i.getIntExtra(EXTRA_POS, 0)
        items = i.getStringArrayListExtra(EXTRA_IMGS)
        adapter = AdapterFullScreenImage(this@ActivityFullScreenImage, items)
        val total = adapter!!.count
        viewPager!!.adapter = adapter

        text_page!!.text = String.format(getString(R.string.image_of), position + 1, total)

        viewPager!!.currentItem = position
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(pos: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(pos: Int) {
                text_page!!.text = String.format(getString(R.string.image_of), pos + 1, total)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })


        findViewById<View>(R.id.btnClose).setOnClickListener { finish() }

        Tools.systemBarLollipop(this)
    }

    override fun onResume() {
        if (!imgloader.isInited) Tools.initImageLoader(this)
        super.onResume()
    }

    fun getExtraPos(): String {
        return EXTRA_POS;
    }

    fun getExtraImgs(): String {
        return EXTRA_IMGS;
    }

    companion object {

        val EXTRA_POS = "key.EXTRA_POS"
        val EXTRA_IMGS = "key.EXTRA_IMGS"
    }

}
