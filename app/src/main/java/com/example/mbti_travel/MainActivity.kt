package com.example.mbti_travel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mbti_travel.fragment.BoardFragment
import com.example.mbti_travel.fragment.MapsFragment
import com.example.mbti_travel.fragment.RankFragment
import com.example.mbti_travel.fragment.TripFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // Intent로부터 selectPage 값을 가져옵니다.
        val selectPage = intent.getStringExtra("selectPage")

        bottomNav.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.navigation_map -> MapsFragment()
                R.id.navigation_destination -> TripFragment()
                R.id.navigation_board -> BoardFragment()
                R.id.navigation_my_trip -> RankFragment()
                else -> MapsFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()

            true
        }

        // Initial tab 선택을 위해 아래 코드를 추가합니다.
        bottomNav.selectedItemId = when (selectPage) {
            "map" -> R.id.navigation_map
            "destination" -> R.id.navigation_destination
            "board" -> R.id.navigation_board
            "my_trip" -> R.id.navigation_my_trip
            else -> R.id.navigation_map
        }

    }
}