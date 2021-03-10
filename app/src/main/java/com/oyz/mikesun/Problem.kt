package com.oyz.mikesun

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putInt
import android.widget.Toast
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_problem.*
import java.util.*

class Problem : AppCompatActivity() {
    var grade = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem)




        random_problem.setText(getProblem())

        num_0.setOnClickListener {
            if(answer.text.toString() != "?"){
                answer.text = answer.text.toString()+0
            }
        }

        num_1.setOnClickListener {
            if(answer.text.toString() == "?"){
                answer.text = "1"
            }else{
                answer.text = answer.text.toString()+1
            }
        }

        num_2.setOnClickListener {
            if(answer.text.toString() == "?"){
                answer.text = "2"
            }else{
                answer.text = answer.text.toString()+2
            }
        }

        num_3.setOnClickListener {
            if(answer.text.toString() == "?"){
                answer.text = "3"
            }else{
                answer.text = answer.text.toString()+3
            }
        }

        num_4.setOnClickListener {
            if(answer.text.toString() == "?"){
                answer.text = "1"
            }else{
                answer.text = answer.text.toString()+4
            }
        }


        num_5.setOnClickListener {
            if(answer.text.toString() == "?"){
                answer.text = "5"
            }else{
                answer.text = answer.text.toString()+5
            }
        }

        num_6.setOnClickListener {
            if(answer.text.toString() == "?"){
                answer.text = "6"
            }else{
                answer.text = answer.text.toString()+6
            }
        }

        num_7.setOnClickListener {
            if(answer.text.toString() == "?"){
                answer.text = "7"
            }else{
                answer.text = answer.text.toString()+7
            }
        }

        num_8.setOnClickListener {
            if(answer.text.toString() == "?"){
                answer.text = "8"
            }else{
                answer.text = answer.text.toString()+8
            }
        }

        num_9.setOnClickListener {
            if(answer.text.toString() == "?"){
                answer.text = "9"
            }else{
                answer.text = answer.text.toString()+9
            }
        }

        div.setOnClickListener {
            if(answer.text.toString() != "?"){
                answer.text = answer.text.toString()+"/"
            }
        }

        clear.setOnClickListener {
            answer.text = ""
        }

        OK.setOnClickListener {
            if (answer.text==null || answer.text.toString()=="" || answer.text.toString()=="?"){
                Toast.makeText(this,"请输入答案",Toast.LENGTH_SHORT).show()
                println("aaaaa")
            }

            if(answer.text.toString() == calculate(random_problem.text.toString()).toString() || calculate(answer.text.toString()).toString() == calculate(
                    random_problem.text.toString()).toString()
            ){
                Toast.makeText(this,"答题成功，+10",Toast.LENGTH_SHORT).show()
                grade += 10
                Grade.text = grade.toString()
                random_problem.text = getProblem()
                answer.text = ""
            }
        }

    }

    //监听返回键
    override fun onBackPressed(){
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        val MaxGrade = prefs.getInt("MaxGrade", 0)
        if(MaxGrade < grade ){
            getSharedPreferences("data", Context.MODE_PRIVATE).edit {
                putInt("MaxGrade", grade)
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
        }
    }


    private fun  getProblem(): String{
        val simple = arrayOf("+", "-", "*", "/")
        while (true) {
            //随机生成【1-100】之间的随机数
            var str: String = (Math.random() * 100 + 1).toInt().toString()
            //生成四则运算的数字个数,至少三个,最多五个
            val a = (Math.random() * 3 + 3).toInt()
            for (i in 0 until a - 1) {
                //生成四则运算的数字
                val b = (Math.random() * 100 + 1).toInt()
                //生成运算符的位置
                val c = (Math.random() * 4).toInt()
                str += simple[c] + b.toString()
            }
            if (calculate(str) >= 0) {
                return str+"=";
            }
        }
    }

    private fun calculate(s: String): Int {
        val stack = Stack<Int>()
        var opt = '+'
        var num = 0
        for (i in 0 until s.length) {
            val ch = s[i]
            if (Character.isDigit(ch)) num = num * 10 + (ch - '0')
            if (!Character.isDigit(ch) && ch != ' ' || i == s.length - 1) {
                if (opt == '+') stack.push(num) else if (opt == '-') stack.push(-num) else if (opt == '*') stack.push(
                    stack.pop() * num
                ) else stack.push(stack.pop() / num)
                num = 0
                opt = ch
            }
        }
        var res = 0
        while (!stack.isEmpty()) res += stack.pop()
        return res
    }
}