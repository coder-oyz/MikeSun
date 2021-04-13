package com.oyz.mikesun

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    private var num: Int? = null
    private var levelString:String? = null
    private var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        initSpinner()

        val prefs = getSharedPreferences("data", MODE_PRIVATE)
        val maxGrade = prefs.getInt("MaxGrade", 0)

        //记录是第几次做题
        val code = prefs.getInt("code", 0)
        //记录是第几次下载
        val downCode = prefs.getInt("downCOde", 0)

        MaxGrade.text = MaxGrade.text.toString() + maxGrade.toString()

        review.setOnClickListener{
            val intent = Intent(this, Review::class.java)
            intent.putExtra("rOrd",1)
            startActivity(intent)
        }

        download.setOnClickListener{
            val intent = Intent(this, Review::class.java)
            intent.putExtra("rOrd",2)

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
        var cnum = arrayOf("10", "20", "30", "40", "50","100","1000")
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.backup -> doDownLoad()
        }
        return true
    }

    private fun doDownLoad() {
        val prefs = getSharedPreferences("data", MODE_PRIVATE)
        //记录是第几次做题
        val downCode = prefs.getInt("downCode", 0)
        var set = HashSet<String>()
        while(set.size<= levelString?.toInt()!!){
            val pro = num?.let { levelString?.let { it1 -> questUtil.foroneques(it, it1) } }
            if (pro != null) {
                set.add(pro)
            }
        }

        //val filePath =  "第 $downCode 次下载记录.txt"
        val file =  "${getString(R.string.app_name)}_question_${System.currentTimeMillis()}.txt"
        val uri = fileUtil.saveTextFile(applicationContext ,set,file)
        println(uri.toString()+ "ssssssssssssssssss")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        //var uri2 = Uri.fromFile(File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),filePath))

        intent.setDataAndType(uri, "text/plain")
        startActivity(intent)
        with(AlertDialog.Builder(this)){
            setTitle("文件地址")
            setMessage("${Environment.DIRECTORY_DOWNLOADS}/$file")
            setPositiveButton("知道了"){
                d,w ->
                true
            }
            show()
        }
        //Toast.makeText(this,,Toast.LENGTH_SHORT).show()
        getSharedPreferences("data", Context.MODE_PRIVATE).edit {
            putInt("downCode", downCode+1)
        }
    }
}