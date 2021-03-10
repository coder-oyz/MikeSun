package com.oyz.mikesun

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("data", MODE_PRIVATE)
        val maxGrade = prefs.getInt("MaxGrade", 0)
        MaxGrade.text = maxGrade.toString()

        enter.setOnClickListener {
            val intent = Intent(this, Problem::class.java)
            startActivity(intent)
            finish()
        }
    }
}