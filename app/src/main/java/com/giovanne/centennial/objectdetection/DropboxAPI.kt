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

        params["path"] = "/images/objDetectImage.png"

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


    fun uploadImage(imageData: ByteArray?,context: Context, callback: (success: Boolean, error: Error?) -> Unit)  {

        imageData?: return


        Log.d("Webservice","upload Image ")


        val queue = Volley.newRequestQueue(context)
        val url: String = "https://content.dropboxapi.com/2/files/upload"

        val request = object : VolleyFileUploadRequest(
            Method.POST,
            url,
            Response.Listener {
                println("response is: $it")

                Log.d("Webservice","response  $it")

            },
            Response.ErrorListener {
                println("error is: $it")
                Log.d("Webservice","error  " + it.message)

            }
        ) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()

                //insert auth here
                headers["Authorization"] = "Bearer " + Credentials().dropBoxKey
                headers["Content-Type"] = "application/octet-stream"
                headers["Dropbox-API-Arg"] =" {\"path\": \"/Images/objDetectImage.png\", \"mode\": \"add\", \"autorename\": true, \"mute\": false, \"strict_conflict\": false}"


                Log.d("Webservice","Headers  " + headers.toString())


                return headers
            }
            override fun getByteData(): MutableMap<String, FileDataPart> {
                var params = HashMap<String, FileDataPart>()
                params["imageFile"] = FileDataPart("image", imageData!!, "png")
                return params
            }

        }

        Volley.newRequestQueue(context).add(request)



    }

}



