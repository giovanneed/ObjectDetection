package com.giovanne.centennial.objectdetection

import android.util.Log
import org.json.JSONObject

class TempPath {


    var link: String = ""


    fun initWithJSON(json: JSONObject) {


        Log.d("Webservice"," json: " + json )


       this.link = json.getString("link")

        Log.d("Webservice","File link: " + this.link )




    }

}