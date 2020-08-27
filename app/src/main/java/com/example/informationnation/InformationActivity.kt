package com.example.informationnation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_information.*
import okhttp3.*
import java.io.IOException

class InformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        fetchJSON()

        val locName = intent.getStringExtra("LOCATION_NAME")
        val yCoordinate = intent.getDoubleExtra("MAPS_Y_COORDINATE", 0.00)
        val xCoordinate = intent.getDoubleExtra("MAPS_X_COORDINATE", 0.00)

        imageView_Info_Pin.setOnClickListener {

            val intent = Intent(this, MapsActivity::class.java)

            intent.putExtra("LOCATION_NAME", locName)
            intent.putExtra("MAPS_X_COORDINATE", xCoordinate)
            intent.putExtra("MAPS_Y_COORDINATE", yCoordinate)

            startActivity(intent)
        }
    }

    private fun fetchJSON(){
        val informationScreenID = intent.getLongExtra(CustomViewHolder.INFORMATION_ID_KEY, -1)
        val informationDetailUrl = "https://www.noforeignland.com/home/api/v1/place?id=$informationScreenID"

        val client = OkHttpClient()
        val request = Request.Builder().url(informationDetailUrl).build()
        client.newCall(request).enqueue(object: Callback{

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    val body = response.body?.string()
                    println(body)

                    val gson = GsonBuilder().create()

                    val place = gson.fromJson(body, Details::class.java).place
                    val imageUrl = place.banner
                    val id = place.id
                    println(id)

                    runOnUiThread{
                        val placeImageHolder = imageView_placeImage
                        if(imageUrl.isNotEmpty()) {
                            Picasso.get().load(imageUrl).fit().into(placeImageHolder)
                        }else{
                            placeImageHolder.setImageResource(R.drawable.compasses)
                        }

                        textView_Location_Name.text = place.name

                        val htmlTextInput = place.comments
                        if(htmlTextInput.isNotEmpty()) {
                            //Kept to allow functionality for API < 24
                            textView_comments.setText(HtmlCompat.fromHtml(htmlTextInput, HtmlCompat.FROM_HTML_MODE_LEGACY))
                        }
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request!")
            }
        })
    }
}
