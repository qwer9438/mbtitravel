package com.example.mbti_travel.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mbti_travel.Model.Trip
import com.example.mbti_travel.R
import com.example.mbti_travel.TripDetailActivity
import com.example.mbti_travel.adapter.TripAdapter

import com.google.firebase.firestore.FirebaseFirestore

class TripFragment : Fragment() {

    private lateinit var tripRecyclerView: RecyclerView
    private val trips = mutableListOf<Trip>()
    private lateinit var ctx: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_trip, container, false)
        tripRecyclerView = view.findViewById(R.id.tripRecyclerView)
        tripRecyclerView.layoutManager = GridLayoutManager(context, 2)  // 2행의 그리드 레이아웃

        fetchData()

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context  // 여기에서 context를 초기화
    }

    private fun fetchData() {
        val db = FirebaseFirestore.getInstance()

        db.collection("trips")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val trip = document.toObject(Trip::class.java)
                    trip.id = document.id
                    trips.add(trip)
                }

                val adapter = TripAdapter(ctx, trips) { selectedTrip ->
                    val intent = Intent(context, TripDetailActivity::class.java)
                    intent.putExtra("selectedTrip", selectedTrip)
                    startActivity(intent)
                }

                tripRecyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }
}
