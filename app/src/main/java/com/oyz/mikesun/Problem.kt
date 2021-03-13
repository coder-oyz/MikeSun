package com.oyz.mikesun

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_problem.*
import java.util.*
import kotlin.collections.ArrayList

class Problem : AppCompatActivity() {
    var grade = 0
    var rnum = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem)

        val num = intent.getIntExtra("num", 10)
        var problem = getProblem(num)


        random_problem.text = problem[rnum].toString()
        lastnum.text = (num - 1 -rnum).toString()

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
                Toast.makeText(this, "请输入答案", Toast.LENGTH_SHORT).show()
                println("aaaaa")
            }

            if(answer.text.toString() == calculate(random_problem.text.toString()).toString() || calculate(
                    answer.text.toString()
                ).toString() == calculate(
                    random_problem.text.toString()
                ).toString()
            ){

                Toast.makeText(this, "答题成功，+10", Toast.LENGTH_SHORT).show()
                grade += 10
                Grade.text = grade.toString()
                rnum++
                if(rnum  == num){
                    Toast.makeText(this, "闯关成功", Toast.LENGTH_SHORT).show()
                    quit()
                }else{
                    random_problem.text = problem[rnum].toString()
                    lastnum.text = (num - 1 -rnum).toString()
                    answer.text = ""
                }

            }
        }

    }

    //退出答题
    fun quit() {
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        val MaxGrade = prefs.getInt("MaxGrade", 0)
        if(MaxGrade <= grade){
            getSharedPreferences("data", Context.MODE_PRIVATE).edit {
                putInt("MaxGrade", grade)
                putInt("Grade", grade)
            }
            val grade = prefs.getInt("Grade", 0)
            Toast.makeText(this, "答题成功，$grade", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Success::class.java)
            startActivity(intent)
            finish()
        }else{
            getSharedPreferences("data", Context.MODE_PRIVATE).edit {
                putInt("Grade", grade)
            }
            val intent = Intent(this, Fail::class.java)
            startActivity(intent)
            finish()
        }
    }

    //监听返回键
    override fun onBackPressed(){
        quit()
    }


    private fun  getProblem(num: Int): Array<Any> {
        var problems = ArrayList<String>()
        val simple = arrayOf("+", "-", "*", "/")
        var count = 0
        var pre = 0
        while (count<num) {
            //随机生成【1-100】之间的随机数
            var str: String = (Math.random() * 100 + 1).toInt().toString()
            //生成四则运算的数字个数,至少三个,最多五个
            val a = (Math.random() * 3 + 3).toInt()
            pre = str.toInt()

            for (i in 0 until a - 1) {
                //生成四则运算的数字
                var b = (Math.random() * 100 + 1).toInt()
                //生成运算符的位置
                var c = (Math.random() * 4).toInt()
                if (c == 3) {
                    var ac = 5
                    while (ac != 0 &&  pre % ac != 0) {
                        ac += (Math.random() * 2 - 2).toInt()
                    }
                    if (ac != 0) {
                        b = ac
                    } else {
                        c = 2
                    }
                }
                pre=b
                str += simple[c] + b.toString()
            }
            val answer = calculate(str)
            if (answer >= 0 && answer%1==0.0 && answer<=1000) {
                count++;

                problems.add(str + "=")
            }
        }
        return problems.toArray()
    }

    private fun calculate(s: String): Double {
        val stack = Stack<Double>()
        var opt = '+'
        var num = 0.0
        for (i in 0 until s.length) {
            val ch = s[i]
            if (Character.isDigit(ch)) num = num * 10 + (ch - '0')
            if (!Character.isDigit(ch) && ch != ' ' || i == s.length - 1) {
                if (opt == '+')
                    stack.push(num)
                else if (opt == '-')
                    stack.push(-num)
                else if (opt == '*')
                    stack.push(stack.pop() * num)
                else stack.push(stack.pop() / num)
                num = 0.0
                opt = ch
            }
        }
        var res = 0.0
        while (!stack.isEmpty()) res += stack.pop()
        return res
    }
}