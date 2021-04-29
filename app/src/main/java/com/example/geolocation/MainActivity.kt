package com.example.geolocation

import android.content.Intent
import androidx.biometric.BiometricPrompt;
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.geolocation.ui.geoloc.GeolocActivity
import com.example.geolocation.ui.login.LoginActivity
import com.example.geolocation.ui.register.RegisterActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executor
import com.google.firebase.perf.ktx.performance


class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    val user = Firebase.auth.currentUser;

    override fun onCreate(savedInstanceState: Bundle?) {
        val mainTrace = Firebase.performance.newTrace("main_trace")
        mainTrace.start()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(user !== null){
            executor = ContextCompat.getMainExecutor(this)
            biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback(){
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }



                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()

                    val intentGeoloc = Intent(this@MainActivity, GeolocActivity::class.java)
                    startActivity(intentGeoloc)
                    finish()

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext,
                        "Authentication failed", Toast.LENGTH_SHORT)
                        .show()

                }
            })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for Geolocation")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
        else{

            val toLogin = findViewById<Button>(R.id.to_login)
            val toRegister = findViewById<Button>(R.id.to_register)

            toLogin.setOnClickListener {
                val intentLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentLogin)
                finish()
            }

            toRegister.setOnClickListener {
                val intentRegister = Intent(this, RegisterActivity::class.java)
                startActivity(intentRegister)
                finish()
            }
        }
        mainTrace.stop()

    }
}