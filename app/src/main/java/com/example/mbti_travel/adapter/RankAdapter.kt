package com.example.mbti_travel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.example.mbti_travel.Model.Trip
import com.example.mbti_travel.R

class RankAdapter(private val context: Context, private val tripList: List<Trip>, private val itemClick: (Trip) -> Unit) :
    RecyclerView.Adapter<RankAdapter.TripViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.trip_item, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = tripList[position]

        holder.name.text = trip.Name
        Picasso.get().load(trip.image).into(holder.image)

        holder.itemView.setOnClickListener {
            itemClick(trip)
        }
    }

    override fun getItemCount(): Int {
        return tripList.size
    }

    inner class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val image: ImageView = itemView.findViewById(R.id.ivImage)
    }
}