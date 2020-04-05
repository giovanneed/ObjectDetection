package com.giovanne.centennial.objectdetection

import android.util.Log
import org.json.JSONObject

class Classifier {


    var className: String = ""

    var score: Double = 0.00

    var type_hierarchy: String = ""




    fun initWithJSON(json: JSONObject) {


        Log.d("Webservice"," json: " + json )


        this.className = json.getString("class")
        this.score = json.getDouble("score")
        //this.type_hierarchy = json.getString("type_hierarchy")

        Log.d("Webservice","File link: " + this.className )




    }

}