package filipe.pires.lopes.gmail.com.coffeedrop

import org.json.JSONObject


interface ServiceInterface {
    fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?, error: String?) -> Unit)
    fun postWithparametersResponseString(path: String, params: JSONObject, completionHandler: (response: String?, error: String?) -> Unit)
    fun get(path: String, params: JSONObject, completionHandler: (response: JSONObject?,error: String?) -> Unit)
}