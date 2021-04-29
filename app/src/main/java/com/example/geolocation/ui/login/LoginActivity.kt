package com.example.geolocation.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.geolocation.R
import com.example.geolocation.ui.geoloc.GeolocActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        val btn = findViewById<Button>(R.id.loginBtn)

        btn.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("login", "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Log.w("login", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }


            }
    }


    private fun updateUI(user: FirebaseUser?) {
        if(user === null){
            Toast.makeText(
                this,
                "Invalid informations !!",
                Toast.LENGTH_LONG
            ).show()
        }
        else{
            val intentGeoloc = Intent(this, GeolocActivity::class.java)
            startActivity(intentGeoloc)
            finish()
        }
    }

}