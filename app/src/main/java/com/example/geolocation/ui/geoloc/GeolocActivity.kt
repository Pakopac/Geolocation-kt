package com.example.geolocation.ui.geoloc

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geolocation.MainActivity
import com.example.geolocation.R
import com.example.geolocation.model.Geoloc
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GeolocActivity : AppCompatActivity() {

    val db = Firebase.firestore
    var geolocAdapter: GeolocAdapter? = null
    val user = Firebase.auth.currentUser;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geoloc)

        val signOutBtn = findViewById<Button>(R.id.signOut)
        val userEmail = findViewById<TextView>(R.id.user_email)

        userEmail.text = user!!.email

        signOutBtn.setOnClickListener {
            Firebase.auth.signOut()
            val intentLogin = Intent(this, MainActivity::class.java)
            startActivity(intentLogin)
            finish()
        }

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1)

        location()

        val query: Query = db.collection("coordinate").whereEqualTo("email",user!!.email)

        val options: FirestoreRecyclerOptions<Geoloc> = FirestoreRecyclerOptions.Builder<Geoloc>()
            .setQuery(query, Geoloc::class.java)
            .build()

        val recyclerGeoloc = findViewById<RecyclerView>(R.id.recycler_geoloc)
        recyclerGeoloc.layoutManager = LinearLayoutManager(this)
        geolocAdapter = GeolocAdapter(options)
        recyclerGeoloc.adapter = geolocAdapter

    }

    override fun onStart() {
        super.onStart()
        geolocAdapter!!.startListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        geolocAdapter!!.stopListening()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            200 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    location()
                } else {
                    Log.v("location", "denied")
                }
                return

            }
        }
    }

    private fun location(){
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val permissionCheck = ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permissionCheck == PackageManager.PERMISSION_GRANTED){

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                60000,
                0F,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        val coordinate = hashMapOf(
                            "longitude" to location.longitude,
                            "latitude" to location.latitude,
                            "email" to  Firebase.auth.currentUser?.email
                        )
                            Log.v("coordinate", location.longitude.toString())


                            db.collection("coordinate")
                                .add(coordinate)
                                .addOnSuccessListener { Log.d("location", "Add coordinate suceess") }
                                .addOnFailureListener{ e -> Log.w("location", "Error adding coordinate", e)  }


                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        //super.onStatusChanged(provider, status, extras)
                    }

                    override fun onProviderEnabled(provider: String) {
                        //super.onProviderEnabled(provider)
                    }

                    override fun onProviderDisabled(provider: String) {
                        //super.onProviderDisabled(provider)
                    }
                }
            )
            return
        }
    }

}