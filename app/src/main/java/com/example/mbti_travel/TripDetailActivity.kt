package com.example.mbti_travel


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.mbti_travel.Model.Rating
import com.example.mbti_travel.Model.Trip
import com.example.mbti_travel.Util.Util
import com.example.mbti_travel.adapter.RatingAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import okhttp3.internal.notify

class TripDetailActivity : AppCompatActivity() {
    private lateinit var trip : Trip
    private lateinit var ratingListView : ListView
    private lateinit var averageScore : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_detail)

        trip = intent.getParcelableExtra<Trip>("selectedTrip")!!

        val imageView: ImageView = findViewById(R.id.imageView)
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val infoTextView: TextView = findViewById(R.id.infoTextView)
        val introduceTextView: TextView = findViewById(R.id.introduceTextView)
        ratingListView = findViewById(R.id.ratingListView)
        averageScore = findViewById(R.id.AverageScoreTextView)
        val rateButton: Button = findViewById(R.id.rateButton)
        rateButton.setOnClickListener {
            showRatingDialog()
        }

        Picasso.get().load(trip.image).into(imageView)
        nameTextView.text = trip.Name
        infoTextView.text = trip.info
        introduceTextView.text = trip.introduce

        Log.d("TripDetail", trip.id)
        loadList()
    }

    fun loadList() {
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
                var averageText = (sum/cnt).toString()
                if (!averageText.equals("NaN")){
                    averageScore.text = "평점: ${averageText}"
                }



                val adapter = RatingAdapter(this, ratings)
                ratingListView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("TripDetailActivity", "Error getting ratings.", exception)
            }
    }

    fun showRatingDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_rating, null)
        val ratingProgressBar: SeekBar = dialogLayout.findViewById(R.id.ratingProgressBar)
        val reviewEditText: EditText = dialogLayout.findViewById(R.id.reviewEditText)
        val cancelButton: Button = dialogLayout.findViewById(R.id.cancelButton)
        val submitRatingButton: Button = dialogLayout.findViewById(R.id.submitRatingButton)


        builder.setView(dialogLayout)
        val alertDialog = builder.create()

        cancelButton.setOnClickListener { alertDialog.dismiss() }
        submitRatingButton.setOnClickListener {
            // Firebase 로직
            updateOrCreateRating(ratingProgressBar.progress.toString(), reviewEditText.text.toString())

            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    fun updateOrCreateRating(ratingScore: String, reviewContent: String) {
        val db = FirebaseFirestore.getInstance()
        val util = Util()
        val nick = util.loadFromSharedPreferences(applicationContext, "nick")
        val mbti = util.loadFromSharedPreferences(applicationContext, "mbti")
        val id = util.loadFromSharedPreferences(applicationContext, "email")
        val ratingRef = db.collection("rating").whereEqualTo("userId", id).whereEqualTo("targetTrip", trip.id)

        ratingRef.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                var newRating: Rating? = null
                // Create new document
                if (nick != null){
                    if (mbti != null) {
                        if( id != null){
                            newRating = Rating(ratingScore.toString(), trip.id, reviewContent, nick, mbti, id)
                        }

                    }
                }



                if (newRating != null) {
                    db.collection("rating").add(newRating)
                    loadList()
                }
            } else {
                // Update existing document
                val documentId = querySnapshot.documents[0].id
                db.collection("rating").document(documentId).update(
                    "ratingScore", ratingScore,
                    "targetTrip", trip.id,
                    "review", reviewContent
                )
                loadList()
            }
        }.addOnFailureListener { exception ->
            Log.w("TripDetailActivity", "Error updating or creating rating.", exception)
        }
    }
}