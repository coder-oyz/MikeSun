package com.oyz.mikesun

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_fail.*
import kotlinx.android.synthetic.main.activity_success.*

class Fail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fail)

        val prefs = getSharedPreferences("data", MODE_PRIVATE)
        val grade = prefs.getInt("Grade", 0)
        FailGrade.text=FailGrade.text.toString() + grade

        fail_back.setOnClickListener(){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}