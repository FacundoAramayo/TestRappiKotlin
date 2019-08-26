package com.facundoaramayo.testrappikotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.facundoaramayo.testrappikotlin.utils.Tools

import java.util.Timer
import java.util.TimerTask

class ActivitySplash : AppCompatActivity() {

    private var parent_view: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        parent_view = findViewById(R.id.parent_view)

        Tools.initImageLoader(applicationContext)

        startActivityMainDelay()
        Tools.systemBarLollipop(this)
    }

    private fun startActivityMainDelay() {
        // Show splash screen for 2 seconds
        val task = object : TimerTask() {
            override fun run() {
                val i = Intent(this@ActivitySplash, ActivityMainPlaces::class.java)
                startActivity(i)
                finish() // kill current activity
            }
        }
        Timer().schedule(task, 2000)
    }

}
