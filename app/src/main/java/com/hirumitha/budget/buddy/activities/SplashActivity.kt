package com.hirumitha.budget.buddy.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.hirumitha.budget.buddy.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val splashScreenTimeOut: Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, splashScreenTimeOut)
    }
}