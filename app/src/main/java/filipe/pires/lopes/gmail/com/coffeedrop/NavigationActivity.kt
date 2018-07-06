package filipe.pires.lopes.gmail.com.coffeedrop

import android.app.Fragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import filipe.pires.lopes.gmail.com.coffeedrop.Fragments.CalculateCashBack
import filipe.pires.lopes.gmail.com.coffeedrop.Fragments.CoffeeDropLocations
import filipe.pires.lopes.gmail.com.coffeedrop.Fragments.FindClossestCoffeeDrop
import filipe.pires.lopes.gmail.com.coffeedrop.Fragments.Home
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.app_bar_navigation.*

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, CoffeeDropLocations.OnFragmentInteractionListener, Home.OnFragmentInteractionListener, FindClossestCoffeeDrop.OnFragmentInteractionListener, CalculateCashBack.OnFragmentInteractionListener {


    private val TAG_HOME = "home"
    private val TAG_LOCATIONS = "locations"
    private val TAG_NE_LOCATION = "nelocation"
    private val TAG_CASHBACK = "cashback"

    //current tag for the fragment thats being presented
   private var CURRENT_TAG = TAG_HOME

    // Tag for the fragment that will be displayed
    var showTag = ""

    // index for the item clicked on the nav menu
   private var navItemIndex = 0

    // current Fragment instance
    private var currentFragmentInstance: Fragment? = null

    //handler use to make the transition of fragments
    private var mHandler: Handler? = null


    //on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_navigation)

        //define Support action bar
        setSupportActionBar(toolbar)

        // give Support action background Black color
        toolbar.setBackgroundColor(Color.BLACK)

        // define title for the toolbar
        supportActionBar!!.title = resources.getString(R.string.home_title)

        //define handler
        mHandler = Handler()

        //load HomeFragment
        if (savedInstanceState == null) {
            navItemIndex = 0
            CURRENT_TAG = TAG_HOME
            loadHomeFragment()
        }





        // add listenner to open/colse nav bar
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //define callback for when an item is selected
        nav_view.setNavigationItemSelectedListener(this)

        // add a listenner to the drawer for loading the fragment only when the nav bar is closed,
        // This prevents lag when loading heavy fragments
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerClosed(drawerView: View) {
                if(showTag != CURRENT_TAG){
                    loadHomeFragment()
                }
            }


            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })
    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            //super.onBackPressed()
            fragmentManager.popBackStackImmediate()
        }
    }



    //intantiate fragments
    private fun getHomeFragment(): Fragment {
        when (navItemIndex) {
            0 -> {
                // home
                currentFragmentInstance = Home.newInstance("blah","blah")
                return currentFragmentInstance!!
            }
            1 -> {
                //Locations
                currentFragmentInstance = CoffeeDropLocations.newInstance()

                return currentFragmentInstance!!
            }
            2 -> {
                //nerest location
                currentFragmentInstance = FindClossestCoffeeDrop.newInstance()
                return currentFragmentInstance!!

            }
          3 -> {
              //cashback
              currentFragmentInstance = CalculateCashBack.newInstance()
              return currentFragmentInstance!!
           }

            else -> return Home.newInstance("blah","blah")
        }
    }


    // to comply to the interface
    override  public fun onFragmentInteraction(uri: Uri) {

    }

    //make fragment ransaction
    private fun loadHomeFragment() {

        //create code for handler
        val mPendingRunnable = Runnable {
            // update the main content by replacing fragments

            //get fragment instance
            val fragment = getHomeFragment()

            //Create transaction obj
            val fragmentTransaction = fragmentManager.beginTransaction()

            //define transaction animation
            fragmentTransaction.setCustomAnimations( android.R.animator.fade_in,
                    android.R.animator.fade_out)


            fragmentTransaction.replace(R.id.content_frame, fragment, CURRENT_TAG)

            //commit transaction
            fragmentTransaction.commit()
        }

        // update future tag
        showTag = CURRENT_TAG

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            //send request for fragment transaction to handler
            mHandler!!.post(mPendingRunnable)
        }


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {

                navItemIndex = 0
                CURRENT_TAG = TAG_HOME
                supportActionBar!!.title = "Coffee Drop App"
            }
            R.id.nav_locations -> {
                navItemIndex = 1
                CURRENT_TAG = TAG_LOCATIONS
                supportActionBar!!.title = "Locations"

            }
            R.id.nav_ne_location -> {
                navItemIndex = 2
                CURRENT_TAG = TAG_NE_LOCATION
                supportActionBar!!.title = "Nearest Location"
            }
            R.id.nav_cashback -> {
                navItemIndex = 3
                CURRENT_TAG = TAG_CASHBACK
                supportActionBar!!.title = "Cashback"

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
