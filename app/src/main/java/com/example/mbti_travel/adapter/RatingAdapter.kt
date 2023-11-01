package com.example.mbti_travel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mbti_travel.Model.Rating
import com.example.mbti_travel.R

class RatingAdapter(context: Context, private val ratings: List<Rating>) : ArrayAdapter<Rating>(context, 0, ratings) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.rating_item, parent, false)

        val rating = ratings[position]

        val ratingScoreTextView: TextView = view.findViewById(R.id.ratingScoreTextView)
        val ratingContentTextView: TextView = view.findViewById(R.id.ratingContentTextView)
        val reviewerNameTextView: TextView = view.findViewById(R.id.raterNameTextView)

        ratingScoreTextView.text = "평가점수: ${rating.ratingScore}"
        ratingContentTextView.text = "평가 내용: ${rating.review}"
        reviewerNameTextView.text = "평가자: ${rating.mbti}"

        return view
    }
}
