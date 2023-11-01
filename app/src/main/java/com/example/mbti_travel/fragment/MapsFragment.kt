package com.example.mbti_travel.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mbti_travel.R
import com.example.mbti_travel.Util.Util
import com.google.firebase.firestore.FirebaseFirestore

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {


    private var now_marker: Marker? = null
    private lateinit var mMap: GoogleMap
    private lateinit var btn_bottomButton_maps: Button
    private lateinit var btn_bottomButton_hotel_maps: Button
    private lateinit var ctx: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btn_bottomButton_maps = view.findViewById(R.id.btn_bottomButton_maps)
        btn_bottomButton_hotel_maps = view.findViewById(R.id.btn_bottomButton_hotels_maps)
        btn_bottomButton_maps.setOnClickListener {
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

        btn_bottomButton_hotel_maps.setOnClickListener {
            val marker = now_marker
            if (marker != null) {
                val intent = Intent(Intent.ACTION_VIEW)

                // 선택한 마커의 위치 정보 (위도 및 경도)
                val latLng = marker.position
                val latitude = latLng.latitude
                val longitude = latLng.longitude

                // 주변 맛집을 검색하기 위한 URI 생성
                val uri = Uri.parse("geo:$latitude,$longitude?q=hotels")

                // Intent에 URI를 설정하여 구글 지도 앱을 엽니다.
                intent.data = uri
                intent.setPackage("com.google.android.apps.maps") // 구글 지도 앱 지정
                startActivity(intent)


            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context  // 여기에서 context를 초기화
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap = googleMap

        // Firestore에서 데이터를 읽어와 마커를 추가하는 코드
        val db = FirebaseFirestore.getInstance()
        db.collection("trips")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.getString("Name") ?: continue
                    val lat = document.getDouble("lat") ?: continue
                    val lon = document.getDouble("lon") ?: continue

                    val markerOptions = MarkerOptions()
                        .position(LatLng(lat, lon))
                        .title(name)
                    mMap.addMarker(markerOptions)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        // 충청북도 좌표
        val center_point = LatLng(36.6357, 127.4919)
        val zoomLevel = 10f
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center_point, zoomLevel))
        mMap.setOnInfoWindowClickListener(this)
        mMap.setOnMarkerClickListener(this)
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
        //Toast.makeText(ctx, "마커 클릭: $title", Toast.LENGTH_SHORT).show()
        return false // true 반환하면 마커 클릭 이벤트가 소비되고, false를 반환하면 기본 동작 수행
    }

    companion object {
        private const val TAG = "MapsFragment"
    }
}
