package com.example.informationnation

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_row.view.*

class MainAdapter(val homeFeed: MutableList<Feature>): RecyclerView.Adapter<CustomViewHolder>() {

    //Number of items in RecyclerView
    override fun getItemCount(): Int {
        return homeFeed.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.card_row, parent, false)
        return CustomViewHolder(cellForRow)

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val features = homeFeed[position]
        holder.view.textView_card_title.text = features.properties.name

        holder.view.textView_card_subtitle1.text = features.geometry.coordinates[0].toString()
        holder.view.textView_card_subtitle2.text = features.geometry.coordinates[1].toString()

        holder.feature = features
    }
}

class CustomViewHolder(val view: View, var feature: Feature? = null): RecyclerView.ViewHolder(view) {

    companion object {
        const val INFORMATION_TITLE_KEY = "INFORMATION TITLE"
        const val INFORMATION_ID_KEY = "INFORMATION ID"
    }

    init {
        view.setOnClickListener {

            val intent = Intent(view.context, InformationActivity::class.java)


            intent.putExtra(INFORMATION_TITLE_KEY, feature?.properties?.name)
            intent.putExtra(INFORMATION_ID_KEY, feature?.properties?.id)
            intent.putExtra("LOCATION_NAME", feature?.properties?.name)
            intent.putExtra("MAPS_Y_COORDINATE", feature?.geometry!!.coordinates[0])
            intent.putExtra("MAPS_X_COORDINATE", feature?.geometry!!.coordinates[1])

            view.context.startActivity(intent)

        }

        view.imageView_pin.setOnClickListener {

            val intent = Intent(view.context, MapsActivity::class.java)

            intent.putExtra("LOCATION_NAME", feature?.properties?.name)
            intent.putExtra("MAPS_Y_COORDINATE", feature?.geometry!!.coordinates[0])
            intent.putExtra("MAPS_X_COORDINATE", feature?.geometry!!.coordinates[1])

            view.context.startActivity(intent)

        }
    }
}





