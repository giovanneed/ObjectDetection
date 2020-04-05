package com.giovanne.centennial.objectdetection

import android.content.Context
import android.util.Base64
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject



class IBMCloudAPI {

    val baseURL = "https://api.us-south.visual-recognition.watson.cloud.ibm.com/instances/03a876f4-a0c6-445f-bd86-e805aa9eaea9/v3/"

    fun getDescription(fileLink: String,context: Context, callback: (classifierParser: ClassifierParser?, error: Error?) -> Unit)  {


        val queue = Volley.newRequestQueue(context)
        val url: String = baseURL + "classify?url=" + fileLink + "&version=2018-03-19"



        val req = object : JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("Webservice","response: " + response )

                if (response == null ) {
                    val error = Error()
                    callback.invoke(null,error)
                    return@Listener
                }

                val classifierParser = ClassifierParser()
                val allImages = response.getJSONArray("images")
                val firstImage = allImages[0] as JSONObject
                val allClassifiers = firstImage.getJSONArray("classifiers")
                val classifierModel = allClassifiers[0] as JSONObject
                if (classifierModel != null) {
                    classifierParser.initWithJSON(classifierModel)
                    callback.invoke(classifierParser,null)
                }




            }, Response.ErrorListener { error ->

                val error = Error()
                error.messege = error.messege
                callback.invoke(null,error)

            }) {

            /**
             * Passing some request headers
             */
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()

                //headers["Authorization"] = "auth"

                //insert auth here
               // headers["apikey"] = apiKey

                val credentials = "apikey:" + Credentials().IBMKey
                val auth =
                    "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = auth

                return headers
            }
        }

        queue.add(req)
    }

}