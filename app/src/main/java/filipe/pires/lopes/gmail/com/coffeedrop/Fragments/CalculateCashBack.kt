package filipe.pires.lopes.gmail.com.coffeedrop.Fragments

import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.TestLooperManager
import android.text.Editable
import android.text.TextWatcher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import filipe.pires.lopes.gmail.com.coffeedrop.*

import kotlinx.android.synthetic.main.fragment_calculate_cash_back.*
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
 * [CalculateCashBack.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CalculateCashBack.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CalculateCashBack : Fragment() {
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
        return inflater.inflate(R.layout.fragment_calculate_cash_back, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //add text changed listener to all textboxes to hide the total cashback label
        ristrttoTotal_TXT.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                response_LBL.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })

        espressoTotal_TXT.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                response_LBL.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })

        lungoTotal_TXT.addTextChangedListener(object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                response_LBL.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })



        //set click listener to btn
        calculate_BTN.setOnClickListener {

            //make request
            val service = ServiceVolley()
            val apiController = APIController(service)

            val path = "/api/products/quote"

            //create json object to send in body as query
            val params = JSONObject()
            if(ristrttoTotal_TXT.text.isNullOrBlank()){
                params.put("Ristretto",0)
            }else{
                params.put("Ristretto",ristrttoTotal_TXT.text.toString().toInt())
            }

            if(espressoTotal_TXT.text.isNullOrBlank()){
                params.put("Espresso",0)
            }else{
                params.put("Espresso", espressoTotal_TXT.text.toString().toInt())
            }

            if(lungoTotal_TXT.text.isNullOrBlank()){
                params.put("Lungo",0)
            }else{
                params.put("Lungo",lungoTotal_TXT.text.toString().toInt())
            }


            //make request
            apiController.postWithparametersResponseString(path, params) { response, error ->

                if(response != null){
                    //present the amout that the user will recive
                   response_LBL.text = "You will Recive " + "%.2f".format(response.toFloat()) + " £"
                    response_LBL.visibility = View.VISIBLE
                }else{
                    toast(error!!)
                }


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
         * @return A new instance of fragment CalculateCashBack.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
                CalculateCashBack().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
