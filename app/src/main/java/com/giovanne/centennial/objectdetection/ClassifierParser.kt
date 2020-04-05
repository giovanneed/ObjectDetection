package com.giovanne.centennial.objectdetection

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class ClassifierParser {

    var classifiers : ArrayList<Classifier> = ArrayList()


    fun initWithJSON(json: JSONObject) {


        Log.d("Webservice"," json: " + json )


        val jsonArray = json.getJSONArray("classes")


        for (i in 0 until jsonArray.length()) {
            var jsonInner: JSONObject = jsonArray.getJSONObject(i)
            var classifier =  Classifier()
            classifier.initWithJSON(jsonInner)
           classifiers.add(classifier)

        }




    }
}