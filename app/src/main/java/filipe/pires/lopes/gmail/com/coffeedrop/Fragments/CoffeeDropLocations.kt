package filipe.pires.lopes.gmail.com.coffeedrop.Fragments

import android.app.Fragment
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import filipe.pires.lopes.gmail.com.coffeedrop.APIController

import filipe.pires.lopes.gmail.com.coffeedrop.R
import filipe.pires.lopes.gmail.com.coffeedrop.ServiceVolley
import filipe.pires.lopes.gmail.com.coffeedrop.adapters.CoffeeDropStoreCellAdapter
import kotlinx.android.synthetic.main.fragment_coffee_drop_locations.*
import org.json.JSONObject
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.fragment_coffee_drop_locations.view.*
import org.jetbrains.anko.doAsync


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CoffeeDropLocations.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CoffeeDropLocations.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CoffeeDropLocations : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var listener: OnFragmentInteractionListener? = null



    lateinit var mMap: GoogleMap
    private lateinit var prefs: SharedPreferences
    var locations: MutableList<JSONObject> = mutableListOf()
    val mMarkerArray = ArrayList<Marker>()



    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var adapter: CoffeeDropStoreCellAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment and initialize View map
        var v = inflater.inflate(R.layout.fragment_coffee_drop_locations, container, false)
        v.mapF.onCreate(savedInstanceState);
        return v
    }







    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {


        //get map assyncronous
        view!!.mapF.getMapAsync(this);


        //initialize recicler view
        linearLayoutManager = LinearLayoutManager(activity)
        gridLayoutManager = GridLayoutManager(activity, 1)
        recyclerViewF.layoutManager = gridLayoutManager
        adapter = CoffeeDropStoreCellAdapter(ArrayList(locations),this)
        recyclerViewF.adapter = adapter
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

            //make request for locations
            val service = ServiceVolley()
            val apiController = APIController(service)

            val path = "/api/locations"
            val params = JSONObject()
            //params.put("email", "foo@email.com")
            //params.put("password", "barpass")

            apiController.get(path, params) { response, error ->
                // reposnse is a JSONObject


                if(response != null) {

                    //compile response and produce all markers, and objects to present on map and on recicler view

                    var arrayOfLocations = response!!.getJSONArray("data")
                    val builder = LatLngBounds.Builder()


                    for (i in 0..(arrayOfLocations.length() - 1)) {
                        val item = arrayOfLocations.getJSONObject(i)

                        var cordinates = item.getJSONObject("coordinates")
                        val location = LatLng(cordinates.getDouble("latitude"), cordinates.getDouble("longitude"))
                        var markopt = MarkerOptions().position(location).title(item.getString("location"))
                        builder.include(markopt.position)
                        var mark = mMap.addMarker(markopt)
                        mMarkerArray.add(mark)
                        item.put("indexpos", mMarkerArray.size - 1)
                        locations.add(item)
                    }

                    if(response.getJSONObject("links").getString("next") != "null"){
                        var LoadMoreOBJ = JSONObject()
                        LoadMoreOBJ.put("LOADMORE",true)
                        LoadMoreOBJ.put("NEXTPAGE", response.getJSONObject("meta").getInt("current_page") + 1)
                        locations.add(LoadMoreOBJ)
                    }


                    //move map camera to have all marker centered
                    val bounds = builder.build()
                    val width =  mapF.width

                    val height = mapF.height
                    // val height1 = resources.displayMetrics.heightPixels / 2
                    val padding = (width * 0.20).toInt()
                    val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)

                    mMap.animateCamera(cu)
                    adapter = CoffeeDropStoreCellAdapter(ArrayList(locations),this@CoffeeDropLocations)
                    recyclerViewF.adapter = adapter

                }

            }



    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onStart() {

        view.mapF.onStart()
        super.onStart()
    }

    override fun onResume() {
        view.mapF.onResume()
        super.onResume()
    }

    override fun onPause() {
        view.mapF.onPause()
        super.onPause()
    }

    override fun onStop() {
        view.mapF.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        //mapF.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        view.mapF.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        view.mapF.onLowMemory()
        super.onLowMemory()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CoffeeDropLocations.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                CoffeeDropLocations().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
