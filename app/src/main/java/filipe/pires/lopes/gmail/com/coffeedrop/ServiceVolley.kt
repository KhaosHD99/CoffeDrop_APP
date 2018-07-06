package filipe.pires.lopes.gmail.com.coffeedrop

import android.util.Log
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import com.android.volley.DefaultRetryPolicy
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.StringRequest
import java.io.StringReader
import java.math.BigDecimal
import java.math.BigInteger
import java.nio.ByteBuffer


class ServiceVolley : ServiceInterface {
    val TAG = ServiceVolley::class.java.simpleName
    val basePath = "http://coffeedrop.staging2.image-plus.co.uk"
    private var mDelivered: Boolean = false


    //make custum request with parameter in json and response in string
    override fun postWithparametersResponseString(path: String, params: JSONObject, completionHandler: (response: String?, error: String?) -> Unit) {

        val stringReq = object : StringRequest(Method.POST,basePath+path,
                Response.Listener<String>{response ->

                    completionHandler(response,null)
                },

                Response.ErrorListener { error ->
                    completionHandler(null,error.localizedMessage)
                }){


             override fun getBody(): ByteArray {
                return params.toString().toByteArray()
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }


        BackendVolley.instance?.addToRequestQueue(stringReq, TAG)
    }


    //Normal post
    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?,error: String?) -> Unit) {
        val jsonObjReq = object : JsonObjectRequest(Method.POST, basePath + path, params,
                Response.Listener<JSONObject> { response ->

                    completionHandler(response,null)
                },
                Response.ErrorListener { error ->

                    completionHandler(null,error.localizedMessage)
                }) {


            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                //cache
                try {
                    var cacheEntry: Cache.Entry? = HttpHeaderParser.parseCacheHeaders(response)

                    if(cacheEntry == null){
                        cacheEntry = Cache.Entry()
                    }

                    var cacheHitButRefreshed: Long = 3*60*100 // 3 min
                    var cacheExpired : Long = 24 * 60 * 60 * 1000 // 24 hrs
                    var now : Long = System.currentTimeMillis();
                    var softExpire : Long = now + cacheHitButRefreshed
                    var ttl : Long = now + cacheExpired

                    cacheEntry.data = response!!.data
                    cacheEntry.softTtl = softExpire
                    cacheEntry.ttl = ttl

                    var headerValue: String?

                    headerValue = response.headers.get("Date")

                    if(headerValue != null){
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }

                    headerValue = response.headers.get("Last-Modified")

                    if(headerValue != null){
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue)
                    }

                    cacheEntry.responseHeaders = response.headers

                    var jsonString = String(response.data, charset( HttpHeaderParser.parseCharset(response.headers)))


                    return Response.success(JSONObject(jsonString), cacheEntry)



                }catch (e: Exception){
                    return Response.error(ParseError(e))



                }

                //return super.parseNetworkResponse(response)
            }

            override fun deliverResponse(response: JSONObject?) {
                if (!mDelivered) {
                    super.deliverResponse(response);
                    mDelivered  = true;
                    cancel();   //if cache delivers response better to cancel scheduled network request

                }

            }

        }

        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }


    //normal get request
    override fun get(path: String,params: JSONObject, completionHandler: (response: JSONObject?, error: String?) -> Unit) {


        val jsonObjReq = object : JsonObjectRequest(Method.GET, basePath + path, params,
                Response.Listener<JSONObject> { response ->

                    completionHandler(response,null)
                },
                Response.ErrorListener { error ->

                    completionHandler(null,error.localizedMessage)
                }) {


            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json")
                return headers
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
               //cache

                try {
                    var cacheEntry: Cache.Entry? = HttpHeaderParser.parseCacheHeaders(response)

                    if(cacheEntry == null){
                        cacheEntry = Cache.Entry()
                    }

                    var cacheHitButRefreshed: Long = 3*60*100 // 3 min
                    var cacheExpired : Long = 24 * 60 * 60 * 1000 // 24 hrs
                    var now : Long = System.currentTimeMillis();
                    var softExpire : Long = now + cacheHitButRefreshed
                    var ttl : Long = now + cacheExpired

                    cacheEntry.data = response!!.data
                    cacheEntry.softTtl = softExpire
                    cacheEntry.ttl = ttl

                    var headerValue: String?

                    headerValue = response.headers.get("Date")

                    if(headerValue != null){
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }

                    headerValue = response.headers.get("Last-Modified")

                    if(headerValue != null){
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue)
                    }

                    cacheEntry.responseHeaders = response.headers

                    var jsonString = String(response.data, charset( HttpHeaderParser.parseCharset(response.headers)))


                        return Response.success(JSONObject(jsonString), cacheEntry)

                }catch (e: Exception){
                    return Response.error(ParseError(e))

                }
            }

            override fun deliverResponse(response: JSONObject?) {
                if (!mDelivered) {
                    super.deliverResponse(response);
                    mDelivered  = true;
                    cancel();   //if cache delivers response better to cancel scheduled network request

                }

            }
        }

        mDelivered = false;
        BackendVolley.instance?.addToRequestQueue(jsonObjReq, TAG)
    }


}