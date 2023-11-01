package com.example.mbti_travel.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mbti_travel.Model.Trip
import com.example.mbti_travel.R
import com.example.mbti_travel.TripDetailActivity
import com.example.mbti_travel.adapter.TripAdapter
import com.google.android.gms.tasks.Tasks

import com.google.firebase.firestore.FirebaseFirestore

class RankFragment : Fragment() {
    private lateinit var spinner: Spinner
    private lateinit var tripRecyclerView: RecyclerView
    private val trips = mutableListOf<Trip>()
    private lateinit var ctx: Context

    // 스피너 어댑터 설정
    val mbtiTypes = listOf(
        "ESTP", "ESFP", "ENTP", "ENFP",
        "ESTJ", "ESFJ", "ENTJ", "ENFJ",
        "ISTP", "ISFP", "INTP", "INFP",
        "ISTJ", "ISFJ", "INTJ", "INFJ"
    )


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rank, container, false)
        tripRecyclerView = view.findViewById(R.id.tripRecyclerView)

        tripRecyclerView.layoutManager = GridLayoutManager(context, 2)  // 2행의 그리드 레이아웃
        spinner = view.findViewById(R.id.rank_spinner)
        val adapter = ArrayAdapter<String>(
            view.context,
            android.R.layout.simple_spinner_item, // 드롭다운 목록에서 각 항목의 레이아웃
            mbtiTypes
        )
        spinner.adapter = adapter
        spinner.setSelection(1)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedMBTI = mbtiTypes[position]
                fetchData()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        fetchData()

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context  // 여기에서 context를 초기화
    }

    private fun fetchData() {
        /*val db = FirebaseFirestore.getInstance()

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
            }*/

        val db = FirebaseFirestore.getInstance()

        db.collection("trips")
            .get()
            .addOnSuccessListener { tripDocuments ->
                val tripList: MutableList<Pair<Trip, Double>> = mutableListOf()

                // Asynchronously fetch ratings and sort trips
                val tasks = tripDocuments.map { document ->
                    val tripId = document.id
                    db.collection("rating")
                        .whereEqualTo("targetTrip", tripId)
                        .get()
                        .continueWith { ratingTask ->
                            val ratings = ratingTask.result?.documents?.map {
                                it.getDouble("ratingScore")
                            }?.filterNotNull() ?: listOf()

                            val avgRating = ratings.average()
                            val trip = document.toObject(Trip::class.java)
                            trip.id = tripId
                            tripList.add(trip to avgRating)
                        }
                }

                Tasks.whenAllComplete(tasks).addOnSuccessListener {
                    // Sort by the average rating
                    tripList.sortByDescending { it.second }

                    val sortedTrips = tripList.map { it.first }
                    val adapter = TripAdapter(ctx, sortedTrips) { selectedTrip ->
                        val intent = Intent(context, TripDetailActivity::class.java)
                        intent.putExtra("selectedTrip", selectedTrip)
                        startActivity(intent)
                    }
                    tripRecyclerView.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }
}
