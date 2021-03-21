package com.oyz.mikesun

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var num: Int? = null
    private var levelString:String? = null
    private var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initSpinner()

        val prefs = getSharedPreferences("data", MODE_PRIVATE)
        val maxGrade = prefs.getInt("MaxGrade", 0)

        //记录是第几次做题
        val code = prefs.getInt("code", 0)

        MaxGrade.text = MaxGrade.text.toString() + maxGrade.toString()

        review.setOnClickListener{
            val intent = Intent(this, Review::class.java)
            startActivity(intent)
        }

        enter.setOnClickListener {
            val intent = Intent(this, Problem::class.java)
            intent.putExtra("code", code)
            intent.putExtra("num", num)
            intent.putExtra("level", levelString)
            startActivity(intent)
            finish()
        }
    }

    private fun initSpinner() {
        var cnum = arrayOf("10", "20", "30", "40", "50")
        //创建一个数组适配器
        val adapter: SpinnerAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cnum)

        spCity.adapter = adapter
        //条目点击事件
        spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected( parent: AdapterView<*>?, view: View?, pos: Int, id: Long
            ) {
                println("onItemSelected 你点击的是:" + cnum[pos])
                num = cnum[pos].toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        var level = arrayOf("简单", "普通","中等", "困难")
        //创建一个数组适配器
        val adapter2: SpinnerAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, level)

        spCity2.adapter = adapter2
        //条目点击事件
        spCity2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected( parent: AdapterView<*>?, view: View?, pos: Int, id: Long
            ) {
                println("onItemSelected 你点击的是:" + level[pos])
                levelString = level[pos].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

    }
}