package com.example.mbti_travel.Model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trip(
    var Name: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var info: String = "",
    var introduce: String = "",
    var mbti: String = "",
    var image: String = "",
    var id : String = ""
) : Parcelable {
    // DocumentSnapshot을 받아 Trip 객체의 데이터를 채우는 생성자
    constructor(document: DocumentSnapshot) : this() {
        Name = document.getString("Name") ?: ""
        lat = document.getDouble("lat") ?: 0.0
        lon = document.getDouble("lon") ?: 0.0
        info = document.getString("info") ?: ""
        introduce = document.getString("introduce") ?: ""
        mbti = document.getString("mbti") ?: ""
        image = document.getString("image") ?: ""
    }

    // Trip 객체의 데이터를 Map 형식으로 반환하는 메소드
    fun toMap(): Map<String, Any> {
        return mapOf(
            "Name" to Name,
            "lat" to lat,
            "lon" to lon,
            "info" to info,
            "introduce" to introduce,
            "mbti" to mbti,
            "image" to image,
            "id" to id
        )
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object : Parceler<Trip> {
        override fun Trip.write(dest: Parcel, flags: Int) {
            dest.writeString(Name)
            dest.writeDouble(lat)
            dest.writeDouble(lon)
            dest.writeString(info)
            dest.writeString(introduce)
            dest.writeString(mbti)
            dest.writeString(image)
            dest.writeString(id)
        }

        override fun create(parcel: Parcel): Trip {
            val Name = parcel.readString() ?: ""
            val lat = parcel.readDouble()
            val lon = parcel.readDouble()
            val info = parcel.readString() ?: ""
            val introduce = parcel.readString() ?: ""
            val mbti = parcel.readString() ?: ""
            val image = parcel.readString() ?: ""
            var id = parcel.readString() ?: ""

            return Trip(Name, lat, lon, info, introduce, mbti, image, id)
        }
    }
}
