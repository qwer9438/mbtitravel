package com.example.mbti_travel.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mbti_travel.Model.Rating
import com.squareup.picasso.Picasso
import com.example.mbti_travel.Model.Trip
import com.example.mbti_travel.R
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Math.round

class TripAdapter(private val context: Context, private val tripList: List<Trip>, private val itemClick: (Trip) -> Unit) :
    RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.trip_item, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = tripList[position]

        holder.name.text = trip.Name
        Picasso.get().load(trip.image).into(holder.image)
        loadaverage(trip, holder.score)

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
        val score : TextView = itemView.findViewById(R.id.tvScore)
    }

    fun loadaverage(trip : Trip, tv_average : TextView) {
        val db = FirebaseFirestore.getInstance()
        // 평가 데이터 가져오기
        db.collection("rating")
            .whereEqualTo("targetTrip", trip.id)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Rating 객체로 변환
                val ratings = querySnapshot.documents.map { document ->
                    document.toObject(Rating::class.java)!!
                }
                var cnt = 0
                var sum = 0.0
                for ( rating in ratings){
                    if (rating.ratingScore.isNotEmpty()) {
                        sum = sum + rating.ratingScore.toFloat()
                        cnt = cnt + 1
                    }
                }
                var averageText = (round((sum/cnt)*10)/10).toString()
                if (!averageText.equals("NaN")){
                    tv_average.text = "평점: ${averageText}"
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TripAdapter", "Error getting ratings.", exception)
            }
    }
}