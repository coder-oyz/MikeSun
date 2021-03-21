package com.oyz.mikesun

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.gson.Gson
import com.oyz.mikesun.entity.Question
import com.oyz.mikesun.questUtil.calculate
import com.oyz.mikesun.questUtil.getques
import com.oyz.mikesun.questUtil.toMid
import com.oyz.mikesun.questUtil.toPoland
import kotlinx.android.synthetic.main.activity_problem.*
import java.io.*
import kotlin.collections.ArrayList

class Problem : AppCompatActivity() {
    var grade = 0
    var rnum = 0
    private val filePath1 = "/data/data/com.oyz.mikesun/cache"
    var count1 = 0
    private val count:Int = getTxtFilesCount(File(filePath1))//覆盖原先的文本内容




    //按钮封装了一个 CountDownTimer，它来帮我们计时
    private val countDownTimer: CountDownTimer by lazy {
        object : CountDownTimer(20000, 1000) {
            override fun onFinish() {
                OK.performClick()
            }

            override fun onTick(t: Long) {
                time.text = (t/1000).toString()
            }
        }
    }



    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        countDownTimer.cancel()   //防止内存泄漏
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem)


        //开启倒计时线程
        countDownTimer.start()



        val num = intent.getIntExtra("num", 10)
        val levelString: String = intent.getStringExtra("level").toString()
        var problem = getques(num,levelString)


        random_problem.text = problem[rnum].toString()
        lastnum.text = (num - 1 -rnum).toString()

        num_0.setOnClickListener {
            if(answer.text.toString() == "?"){
                answer.text = "1"
            }else{
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

            var Myanswer: String
            var Tanswer: String

            if (answer.text==null || answer.text.toString()=="" || answer.text.toString()=="?" ){
                Toast.makeText(this, "请输入答案", Toast.LENGTH_SHORT).show()
                answer.text = 0.toString()
            }
            Myanswer = answer.text.toString()
            Tanswer = calculate(toPoland(toMid(random_problem.text.toString()))).toString()
            if(answer.text.toString() == Tanswer || calculate(toPoland(toMid(answer.text.toString() ))).toString() == Tanswer){

                Toast.makeText(this, "答题成功，+10", Toast.LENGTH_SHORT).show()
                grade += 10
                Grade.text = grade.toString()
                rnum++
                if(rnum  == num){
                    Toast.makeText(this, "闯关成功", Toast.LENGTH_SHORT).show()
                    quit()
                }else{
                    random_problem.text = problem[rnum].toString()
                    countDownTimer.start()
                    lastnum.text = (num - 1 -rnum).toString()
                    answer.text = ""
                }

            }else{
                Toast.makeText(this, "答案错误", Toast.LENGTH_SHORT).show()
                rnum++
                if(rnum  == num){
                    Toast.makeText(this, "答题完成", Toast.LENGTH_SHORT).show()
                    quit()
                }else{
                    answer.setTextColor(Color.BLACK)
                    random_problem.text = problem[rnum].toString()
                    countDownTimer.start()
                    lastnum.text = (num - 1 -rnum).toString()
                    answer.text = ""
                }


            }
            save(Question(problem[rnum-1].toString(),Myanswer,Tanswer))
        }

    }


    fun getTxtFilesCount(srcFile: File?): Int {
        // 判断传入的文件是不是为空
        if (srcFile == null || !srcFile.exists()) {
            srcFile?.createNewFile()
            //throw NullPointerException()
        }
        // 把所有目录、文件放入数组
        val files: Array<File> = srcFile?.listFiles() as Array<File>
        // 遍历数组每一个元素
        for (f in files) {
            // 判断元素是不是文件夹，是文件夹就重复调用此方法（递归）
            if (f.isDirectory) {
                getTxtFilesCount(f)
            } else {
                // 判断文件是不是以.txt结尾的文件，并且count++（注意：文件要显示扩展名）
                if (f.name.endsWith(".txt")) {
                    count1++
                }
            }
        }
        // 返回.txt文件个数
        return count1
    }

    fun save(question: Question){

        var code: Int = count +1
        val filePath = "/data/data/com.oyz.mikesun/cache"+ File.separator + "第 $code 次答题记录.txt"

        var gson = Gson()

        File(filePath).appendText(gson.toJson(question)+"\r\n")//覆盖原先的文本内容




    }



    //退出答题
    fun quit() {
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        val codeF = prefs.getInt("code",0)
        val MaxGrade = prefs.getInt("MaxGrade", 0)
        if(MaxGrade <= grade){
            getSharedPreferences("data", Context.MODE_PRIVATE).edit {
                putInt("MaxGrade", grade)
                putInt("Grade", grade)
                putInt("code",codeF+1)
            }
            val grade = prefs.getInt("Grade", 0)
            Toast.makeText(this, "答题成功，$grade", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Success::class.java)
            startActivity(intent)
            finish()
        }else{
            getSharedPreferences("data", Context.MODE_PRIVATE).edit {
                putInt("Grade", grade)
                putInt("code",codeF+1)
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

    fun getLevelNum(level: String) = when{
        level == "简单" -> 2
        level == "普通" -> 3
        level == "困难" -> 5
        else -> 0
    }

    /*private fun  getProblem(num: Int,level: String): Array<Any> {

        var problems = ArrayList<String>()
        val levelNum = getLevelNum(level)
        val simple = arrayOf("+", "-", "x", "÷")
        var count = 0
        var pre = 0
        while (count<num) {
            //随机生成【1-100】之间的随机数
            var str: String = (Math.random() * levelNum*20 + 1).toInt().toString()
            //生成四则运算的数字个数,至少3个,最多五个
            val a = levelNum

            pre = str.toInt()

            for (i in 0 until a - 1) {
                //生成四则运算的数字
                var b = (Math.random() * levelNum*20 + 1).toInt()
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
                else if (opt == 'x')
                    stack.push(stack.pop() * num)
                else stack.push(stack.pop() / num)
                num = 0.0
                opt = ch
            }
        }
        var res = 0.0
        while (!stack.isEmpty()) res += stack.pop()
        return res
    }*/
}




