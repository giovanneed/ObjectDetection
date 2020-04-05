package com.giovanne.centennial.objectdetection

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    private var imageData: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraButton.setOnClickListener {
            Log.i("giovanne","Clicked")

            if (checkPermission()) {
                Log.i("giovanne","PERMISSION_GRANTED")
                dispatchTakePictureIntent()

            }

        }

        analyzeButton.setOnClickListener {
            if (isOnline(this))  {
                Log.d("giovanne","has conectivity")
            } else {
                Log.d("giovanne","doesnt have conectivity")

            }

            getUsers()
        }
    }

    private fun checkPermission(): Boolean {

        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }

        return (permission == PackageManager.PERMISSION_GRANTED)
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA  ),
            REQUEST_IMAGE_CAPTURE)
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_IMAGE_CAPTURE){
            val uri = data?.data
            if (uri != null) {
                //createImageData(uri)
            }
            var bmp = data?.extras?.get("data") as Bitmap
            picImageView.setImageBitmap(bmp)
        }
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    // function for network call
    fun getUsers() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = "https://api.github.com/search/users?q=eyehunt"

        Log.d("giovanne","call get users fun")


        // Request a string response from the provided URL.
        val stringReq = StringRequest(

            Request.Method.GET, url,
            Response.Listener<String> { response ->

                Log.d("giovanne","request was made")

                var strResp = response.toString()
                val jsonObj: JSONObject = JSONObject(strResp)
                val jsonArray: JSONArray = jsonObj.getJSONArray("items")
                var str_user: String = ""
                for (i in 0 until jsonArray.length()) {
                    var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                    str_user = str_user + "\n" + jsonInner.get("login")
                }
                txtView!!.text = "response : $str_user "
            },
            Response.ErrorListener { error->
                txtView!!.text = error.toString()

            })
        queue.add(stringReq)
    }

}
