package com.example.mbti_travel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ReportFragment.Companion.reportFragment

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mbti_travel.databinding.ActivityMapsBinding
import com.example.mbti_travel.Model.Trip
import com.example.mbti_travel.Util.Util
import android.content.Context
import com.google.gson.Gson
import java.io.IOException
import android.util.Log
import android.widget.Toast
import android.content.Intent
import android.net.Uri
import android.widget.Button




class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener  {
    private  var now_marker: Marker? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var btn_bottomButton_maps = findViewById<Button>(R.id.btn_bottomButton_maps)
        btn_bottomButton_maps.setOnClickListener{
            val marker = now_marker
            if (marker != null) {
                val intent = Intent(Intent.ACTION_VIEW)

                // 선택한 마커의 위치 정보 (위도 및 경도)
                val latLng = marker.position
                val latitude = latLng.latitude
                val longitude = latLng.longitude

                // 주변 맛집을 검색하기 위한 URI 생성
                val uri = Uri.parse("geo:$latitude,$longitude?q=restaurants")

                // Intent에 URI를 설정하여 구글 지도 앱을 엽니다.
                intent.data = uri
                intent.setPackage("com.google.android.apps.maps") // 구글 지도 앱 지정
                startActivity(intent)

            }
        }
    }




    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        val util = Util()
        val jsonString = util.readJsonFromAssets(this, "data.json") // context는 액티비티나 앱의 컨텍스트입니다.
        if (jsonString.isNotEmpty()) {
            /*val trip = util.parseJsonToTrip(jsonString)
            for ((key, place) in trip) {
                Log.d("Place$key", "Name: ${place.Name}")
                Log.d("Place$key", "lat: ${place.lat}")
                Log.d("Place$key", "lon: ${place.lon}")
                Log.d("Place$key", "info: ${place.info}")
                Log.d("Place$key", "introduce: ${place.introduce}")
                Log.d("Place$key", "mbti: ${place.mbti}")
                Log.d("Place$key", "image: ${place.image}")

                val point = LatLng(place.lat, place.lon)
                mMap.addMarker(MarkerOptions().position(point).title(place.Name))
            }*/
        } else {
            // JSON 파일을 읽어오지 못한 경우에 대한 처리를 여기에 추가합니다.
        }

        // 충청북도 좌표
        val center_point = LatLng(36.6357, 127.4919)
        val zoomLevel = 10f
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center_point, zoomLevel))
        mMap.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(marker: Marker) {
        // 마커의 정보창이 클릭되었을 때 실행될 코드를 여기에 작성합니다.
        if (marker != null) {
            val title = marker.title
            //showToast(title + " 페이지로 이동")
            val intent = Intent(Intent.ACTION_VIEW)

            // 선택한 마커의 위치 정보 (위도 및 경도)
            val latLng = marker.position
            val latitude = latLng.latitude
            val longitude = latLng.longitude

            // 주변 맛집을 검색하기 위한 URI 생성
            val uri = Uri.parse("geo:$latitude,$longitude?q=restaurants")

            // Intent에 URI를 설정하여 구글 지도 앱을 엽니다.
            intent.data = uri
            intent.setPackage("com.google.android.apps.maps") // 구글 지도 앱 지정
            startActivity(intent)
        }
    }
    override fun onMarkerClick(marker: Marker): Boolean {
        val title = marker.title
        now_marker = marker
        showToast("마커 클릭: $title")
        return true // true 반환하면 마커 클릭 이벤트가 소비되고, false를 반환하면 기본 동작 수행
    }


    fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }
}