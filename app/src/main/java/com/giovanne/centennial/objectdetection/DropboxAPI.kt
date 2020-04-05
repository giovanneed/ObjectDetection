package com.giovanne.centennial.objectdetection

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class DropboxAPI {


    fun getFileTempLink(file: String,context: Context, callback: (tempPath: TempPath?, error: Error?) -> Unit)  {


        val queue = Volley.newRequestQueue(context)
        val url: String = "https://api.dropboxapi.com/2/files/get_temporary_link"

        val params = HashMap<Any?,Any?>()

        params["path"] = "/images/can-coke.png"

        val jsonObject = JSONObject(params)

        val req = object : JsonObjectRequest(Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                Log.d("Webservice","response: " + response )

                if (response == null ) {
                    val error = Error()
                    callback.invoke(null,error)
                    return@Listener
                }

                val link = TempPath()
                link.initWithJSON(response)
                callback.invoke(link,null)


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

                //insert auth here
                headers["Authorization"] = "Bearer " + Credentials().dropBoxKey
                headers["Content-Type"] = "application/json"

                return headers
            }
        }

        queue.add(req)
    }

}

