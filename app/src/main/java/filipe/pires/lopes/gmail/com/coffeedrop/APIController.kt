package filipe.pires.lopes.gmail.com.coffeedrop

import org.json.JSONObject

// create this class to be easier to change services for comunication
class APIController constructor(serviceInjection: ServiceInterface): ServiceInterface {
    private val service: ServiceInterface = serviceInjection

    override fun postWithparametersResponseString(path: String, params: JSONObject, completionHandler: (response: String?, error: String?) -> Unit) {
        service.postWithparametersResponseString(path, params, completionHandler)
    }
    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?,error: String?) -> Unit) {
        service.post(path, params, completionHandler)
    }


    override fun get(path: String, params: JSONObject, completionHandler: (response: JSONObject?, error: String?) -> Unit) {
        service.get(path,params,completionHandler)
    }
}