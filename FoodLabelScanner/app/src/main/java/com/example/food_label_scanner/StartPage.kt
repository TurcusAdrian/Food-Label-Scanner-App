package com.example.food_label_scanner

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class StartPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("StartPage", "StartPage Activity Launched")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_page)

        val titleText: TextView = findViewById(R.id.titleText)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        titleText.startAnimation(fadeIn)



        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}