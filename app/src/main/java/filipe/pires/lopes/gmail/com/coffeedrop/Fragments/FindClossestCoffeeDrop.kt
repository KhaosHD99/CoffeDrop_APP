package filipe.pires.lopes.gmail.com.coffeedrop.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import filipe.pires.lopes.gmail.com.coffeedrop.*

import kotlinx.android.synthetic.main.fragment_find_clossest_coffee_drop.*
import org.jetbrains.anko.toast
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FindClossestCoffeeDrop.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FindClossestCoffeeDrop.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class FindClossestCoffeeDrop : Fragment() {
    // TODO: Rename and change types of parameters


    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_clossest_coffee_drop, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //set listener on serarch btn
        search_BTN.setOnClickListener {

            //disables btn
            search_BTN.isClickable = false

            //creates a timmer to reenable btn
            var timmer = object : CountDownTimer(2000,1000){
                override fun onFinish() {
                    search_BTN.isClickable = true
                }

                override fun onTick(millisUntilFinished: Long) {

                }
            }.start()


            //verifys if the input in empty
            if(!postcode_TXT.text.isNullOrBlank()){

                //make request
                val service = ServiceVolley()
                val apiController = APIController(service)

                val path = "/api/location/postcode?postcode=" + postcode_TXT.text.toString().trim().toUpperCase()
                val params = JSONObject()


                apiController.get(path, params) { response, error ->
                    //response from request
                    if(response != null){
                        activity.launchActivity<LocationInfo> {
                            putExtra("storeOBJ", response.getJSONObject("data").toString(0))
                        }
                    }else{
                        toast(error!!)
                    }


                }

            }else{


                //present error if input is empty
                toast("You need to add one postcode")
                timmer.cancel()
                search_BTN.isClickable = true
            }



        }

        // add text changed listener to the textbox to reenable searchbtn
        postcode_TXT.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                search_BTN.isClickable = true
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })


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
         * @return A new instance of fragment FindClossestCoffeeDrop.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                FindClossestCoffeeDrop().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
