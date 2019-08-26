package com.facundoaramayo.testrappikotlin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.view.View
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.facundoaramayo.testrappikotlin.fragment.FragmentHome
import com.facundoaramayo.testrappikotlin.R

class MainActivity : AppCompatActivity() {

    internal var home = false

    var actionBar: ActionBar? = null
    lateinit var toolbar: Toolbar
    private val cat: IntArray? = null
    private var fab: com.getbase.floatingactionbutton.FloatingActionsMenu? = null
    private var fab_search_by_location: com.getbase.floatingactionbutton.FloatingActionButton? = null
    private var fab_search_by_name: com.getbase.floatingactionbutton.FloatingActionButton? = null
    private var navigationView: NavigationView? = null
    private var nav_header_lyt: RelativeLayout? = null
    private var exitTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityMain = this

        fab = findViewById<View>(R.id.floating_action_button) as com.getbase.floatingactionbutton.FloatingActionsMenu
        fab_search_by_location = findViewById(R.id.fb_location)
        fab_search_by_name = findViewById(R.id.fb_restaurant_name)


        initToolbar()
        initDrawerMenu()
        //        cat = getResources().getIntArray(R.array.id_category);

        // first drawer view
        onItemSelected(R.id.nav_home, getString(R.string.title_home))

    }

    private fun initToolbar() {
        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        //toolbar.setTitleTextAppearance(this, R.style.toolbar_title_style);
        setSupportActionBar(toolbar)
        actionBar = supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
        actionBar!!.setHomeButtonEnabled(true)
    }

    private fun initDrawerMenu() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = object : ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View?) {

                super.onDrawerOpened(drawerView)
            }
        }
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        navigationView = findViewById<View>(R.id.nav_view) as NavigationView

        // navigation header
        val nav_header = navigationView!!.getHeaderView(0)
        nav_header_lyt = nav_header.findViewById<View>(R.id.nav_header_lyt) as RelativeLayout

    }

    override fun onBackPressed() {
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START)
        } else {
            doExitApp()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    fun onItemSelected(id: Int, title: String): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null
        val bundle = Bundle()
        when (id) {
            //home
            R.id.nav_home -> {
                fragment = FragmentHome()
                home = true
                actionBar!!.title = title
            }
            else -> {
            }
        }

        if (home) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_content, fragment!!)
            fragmentTransaction.commit()
        } else if (fragment != null) {
            fragment.arguments = bundle
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frame_content, fragment)
            fragmentTransaction.commit()
        }

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun doExitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    public override fun onStart() {
        super.onStart()
        active = true
    }

    override fun onDestroy() {
        super.onDestroy()
        active = false
    }

    companion object {
        internal var active = false

        internal lateinit var activityMain: MainActivity
    }

}
