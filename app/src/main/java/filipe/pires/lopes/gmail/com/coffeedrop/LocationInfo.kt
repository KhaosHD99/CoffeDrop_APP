package filipe.pires.lopes.gmail.com.coffeedrop

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_location_info.*
import kotlinx.android.synthetic.main.fragment_coffee_drop_locations.*
import org.json.JSONObject

class LocationInfo : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap

    //CoffeeDropStore JsonObjcet
    private lateinit var StoreOBJ: JSONObject


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_info)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //get store object from intent
        StoreOBJ = JSONObject(intent.getStringExtra("storeOBJ"))

        //set location name as labes text
        locationName_LBL.text = StoreOBJ.getString("location") + " (" + StoreOBJ.getJSONObject("address").getString("postcode") + ")"

        //get the distance from the object and if distance == 0 dont show. This is a problem if the user inputs the postcode of the store and the backend returns distance 0
        var distance = StoreOBJ.getString("distance")
        if( distance != "0 Miles"){
            distance_LBL.text = "Distance: " + distance
            distance_LBL.visibility = View.VISIBLE
        }

        //set listener for button close
        close_BTN.setOnClickListener {
            finish()
        }

        //get oppening squedule
        var openings = StoreOBJ.getJSONArray("openings")

        //apply opening squedule to the table
        for (i in 0..openings.length() - 1){
            val item = openings.getJSONObject(i)
            when (item.getString("day")){
                "monday"->{
                    monday_open_LBL.text = item.getString("open")
                    monday_close_LBL.text = item.getString("closed")
                }
                "tuesday"->{
                    tuesday_open_LBL.text = item.getString("open")
                    tuesday_close_LBL.text = item.getString("closed")
                }
                "wednesday"->{
                    wednesday_open_LBL.text = item.getString("open")
                    wednesday_close_LBL.text = item.getString("closed")
                }
                "thursday"->{
                    thursday_open_LBL.text = item.getString("open")
                    thursday_close_LBL.text = item.getString("closed")
                }
                "friday"->{
                    friday_open_LBL.text = item.getString("open")
                    friday_close_LBL.text = item.getString("closed")
                }
                "saturday"->{
                    saturday_open_LBL.text = item.getString("open")
                    saturday_close_LBL.text = item.getString("closed")
                }
                "sunday"->{
                    sunday_open_LBL.text = item.getString("open")
                    sunday_close_LBL.text = item.getString("closed")
                }
                else-> println("ERROR")
            }
        }












    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // add marker on location and move the camera
        var cordinates = StoreOBJ.getJSONObject("coordinates")
        val Markerposition = LatLng(cordinates.getDouble("latitude"), cordinates.getDouble("longitude"))
        var marker = mMap.addMarker(MarkerOptions().position(Markerposition).title(StoreOBJ.getString("location")))
        val builder = LatLngBounds.Builder()
        builder.include(Markerposition)
        val bounds = builder.build()
        val width =  resources.displayMetrics.widthPixels

        val height = 300
        // val height1 = resources.displayMetrics.heightPixels / 2
        val padding = (width * 0.10).toInt()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)

        mMap.animateCamera(cu)
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Markeropt))
    }
}
