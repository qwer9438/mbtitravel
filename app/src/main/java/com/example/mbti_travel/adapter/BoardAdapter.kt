package com.example.mbti_travel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mbti_travel.Model.Board
import com.example.mbti_travel.R
import java.text.SimpleDateFormat
import java.util.Date

class BoardAdapter(context: Context, private val boards: List<Board>) : ArrayAdapter<Board>(context, R.layout.list_item_board, boards) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val board = getItem(position)!!
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_board, parent, false)

        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val authorTextView: TextView = view.findViewById(R.id.authorTextView)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)

        titleTextView.text = board.title
        authorTextView.text = board.author
        timeTextView.text = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())

        return view
    }
}
