package filipe.pires.lopes.gmail.com.coffeedrop.adapters

import android.support.v7.widget.RecyclerView
import android.view.Gravity


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import filipe.pires.lopes.gmail.com.coffeedrop.*
import filipe.pires.lopes.gmail.com.coffeedrop.Fragments.CoffeeDropLocations

import kotlinx.android.synthetic.main.coffee_drop_store_reusable_cell.view.*
import kotlinx.android.synthetic.main.fragment_coffee_drop_locations.*
import org.json.JSONObject


/**
 * Created by filipelopes on 13/12/2017.
 */
//recycler vier adapter
class CoffeeDropStoreCellAdapter(private val StoresToShow: ArrayList<JSONObject>, private val MainView: CoffeeDropLocations? = null) : RecyclerView.Adapter<CoffeeDropStoreCellAdapter.RecordHolder>()  {
    override fun getItemCount() = StoresToShow.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordHolder {
        //inflate cell
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.coffee_drop_store_reusable_cell,parent,false)

        return RecordHolder(inflatedView)

    }

    override fun onBindViewHolder(holder: RecordHolder, position: Int) {
        val Store = StoresToShow[position]

        //bind Store object with cell
        holder.bindRecord(Store,MainView)

    }

    class RecordHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private lateinit var StoreOBJ: JSONObject
        private var MainView: CoffeeDropLocations? = null



        init {
                //set onclick listener to the entire cell to focus on a specific marker whem touched
                v.setOnClickListener(this)

        }


        override fun onClick(v: View) {
            //verifyes if cell is not a dummy cell to load next page
            if(!StoreOBJ.has("LOADMORE")) {
                var cordinates = StoreOBJ.getJSONObject("coordinates")
                MainView!!.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(cordinates.getDouble("latitude"), cordinates.getDouble("longitude")), 14.0f));
                MainView!!.mMarkerArray[StoreOBJ.getInt("indexpos")].showInfoWindow()
            }

        }


        fun bindRecord(NewStore: JSONObject, PMainView: CoffeeDropLocations? = null) {

            //binds the record to the cell
            StoreOBJ = NewStore
            MainView = PMainView

            //select the apropriate click lisnecer depending on the object
            if(StoreOBJ.has("LOADMORE")){


                view.StoreName_LBL.visibility = View.GONE
                view.ViewMore_BTN.setText("LOAD MORE")

                //set clicke listener to loadmore btn
                view.ViewMore_BTN.setOnClickListener{

                    //make request and refocus the map camera
                    MainView!!.locations.removeAt(MainView!!.locations.lastIndex)
                    val service = ServiceVolley()
                    val apiController = APIController(service)

                    val path = "/api/locations?page=" + StoreOBJ.getInt("NEXTPAGE")
                    val params = JSONObject()


                    apiController.get(path,params){response, error ->
                        if(response != null) {
                            var arrayOfLocations = response!!.getJSONArray("data")
                            val builder = LatLngBounds.Builder()

                            for (i in 0..(arrayOfLocations.length() - 1)) {
                                val item = arrayOfLocations.getJSONObject(i)

                                var cordinates = item.getJSONObject("coordinates")
                                val location = LatLng(cordinates.getDouble("latitude"), cordinates.getDouble("longitude"))
                                var markopt = MarkerOptions().position(location).title(item.getString("location"))
                                builder.include(markopt.position)
                                var mark = MainView!!.mMap.addMarker(markopt)
                                MainView!!.mMarkerArray.add(mark)
                                item.put("indexpos", MainView!!.mMarkerArray.size - 1)
                                MainView!!.locations.add(item)
                            }

                            if (response.getJSONObject("links").getString("next") != "null") {
                                var LoadMoreOBJ = JSONObject()
                                LoadMoreOBJ.put("LOADMORE", true)
                                LoadMoreOBJ.put("NEXTPAGE", response.getJSONObject("meta").getInt("current_page") + 1)
                                MainView!!.locations.add(LoadMoreOBJ)
                            }
                            MainView!!.adapter = CoffeeDropStoreCellAdapter(ArrayList(MainView!!.locations), MainView)
                            MainView!!.recyclerViewF.adapter = MainView!!.adapter
                        }
                    }
                }
                //view.ViewMore_BTN.gravity = Gravity.CENTER
            }else{
                //cell represents a store


                view.StoreName_LBL.text = NewStore.getString("location")

                //add click listener to btn to open a new activity with detailed information of the store
                view.ViewMore_BTN.setOnClickListener {
                   MainView!!.activity.launchActivity<LocationInfo> {
                       putExtra("storeOBJ", StoreOBJ.toString(0))
                   }


                }
            }


        }


    }
}