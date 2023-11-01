package com.example.mbti_travel.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.mbti_travel.BoardDetailActivity
import com.example.mbti_travel.BoardWriteActivity
import com.example.mbti_travel.Model.Board
import com.example.mbti_travel.R
import com.example.mbti_travel.Util.Util
import com.example.mbti_travel.adapter.BoardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class BoardFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var fab: FloatingActionButton
    private val db = FirebaseFirestore.getInstance()
    private val boards = ArrayList<Board>()
    private val TAG = "BoardFragment"
    private lateinit var ctx: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_board, container, false)
        listView = view.findViewById(R.id.listView)
        fab = view.findViewById(R.id.fab)

        checkNick()
        loadBoards()

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedBoard = boards[position]
            val intent = Intent(activity, BoardDetailActivity::class.java)
            intent.putExtra("selectedBoard", selectedBoard)
            startActivity(intent)

        }

        fab.setOnClickListener {
            startActivity(Intent(activity, BoardWriteActivity::class.java))
            activity?.finish()
        }

        return view
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context  // 여기에서 context를 초기화
    }

    private fun checkNick() {
        val util = Util()
        val nick = util.loadFromSharedPreferences(ctx, "nick")
        if (nick != null) {
            Log.d("NICK_TEST",nick)
        }
        if (nick != null) {
            if (nick.isBlank()) {
                val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_nickname, null)
                val builder = AlertDialog.Builder(ctx)
                    .setView(dialogView)
                    .setCancelable(false) // 옵션입니다. 다이얼로그 외부를 터치했을 때 다이얼로그가 닫히지 않도록 설정합니다.

                val dialog = builder.create()

                dialogView.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
                    val editTextNick = dialogView.findViewById<EditText>(R.id.editNickname)
                    val newNick = editTextNick.text.toString()

                    if (newNick.isNotBlank()) {
                        // 파이어베이스에 쿼리하여 닉네임 중복을 확인합니다.
                        val db = FirebaseFirestore.getInstance()
                        db.collection("User").whereEqualTo("nick", newNick).get()
                            .addOnSuccessListener { documents ->
                                if (documents.isEmpty) {
                                    // 닉네임이 중복되지 않는 경우, SharedPreference와 파이어베이스를 업데이트합니다.
                                    util.saveToSharedPreferences(ctx,"nick", newNick)

                                    val email = util.loadFromSharedPreferences(ctx, "email") ?: ""
                                    val plat = util.loadFromSharedPreferences(ctx, "plat") ?: ""

                                    if (email.isNotBlank() && plat.isNotBlank()) {
                                        db.collection("User")
                                            .whereEqualTo("email", email)
                                            .whereEqualTo("plat", plat)
                                            .get()
                                            .addOnSuccessListener { users ->
                                                for (user in users) {
                                                    db.collection("User").document(user.id)
                                                        .update("nick", newNick)
                                                }
                                                util.saveToSharedPreferences(ctx,"nick",newNick)
                                            }
                                    }

                                    dialog.dismiss()
                                } else {
                                    // 닉네임이 중복되는 경우
                                    Toast.makeText(activity, "중복된 닉네임이 있습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(activity, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }

                dialog.show()
            }
        }
    }

    public fun loadBoards() {
        db.collection("board").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val board = document.toObject(Board::class.java)
                    board.id = document.id.toString()
                    boards.add(board)
                }
                val adapter = BoardAdapter(ctx, boards)
                listView.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->

                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}
