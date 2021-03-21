package com.oyz.mikesun

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.oyz.mikesun.entity.Question
import com.oyz.mikesun.fragment.QuestionContentFragment
import kotlinx.android.synthetic.main.activity_question_content.*
import kotlinx.android.synthetic.main.question_content_frag.*
import java.io.File

class QuestionContentActivity : AppCompatActivity() {
    companion object {
        //利用intent传如title和content，启动activity
        fun actionStart(context: Context, title: String) {
                val intent = Intent(context, QuestionContentActivity::class.java).apply {
                    putExtra("question_title", title)
            }
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_content)

        var index: Int =0;

        val title = intent.getStringExtra("question_title")
        var questions = ArrayList<Question>()
        if(title !=null ) {
            val fragment = questionContentFrag as QuestionContentFragment
            fragment.refresh(title)  //刷新NewsContentFragment界面
            initReview(title,questions)
        }


        getProblemAndAnswer(index,questions)
        val size = questions.size

        next.setOnClickListener{
            if(index + 1 <= size-1){
                getProblemAndAnswer(index+1,questions)
                index++;
            }else{
                Toast.makeText(this,"已经是最后一题了",Toast.LENGTH_SHORT).show()
            }

        }

        last.setOnClickListener{
            if(index > 0){
                getProblemAndAnswer(index-1,questions)
            }else{
                Toast.makeText(this,"已经是第一题了",Toast.LENGTH_SHORT).show()
            }
            index --;
        }
    }

    private fun initReview(title: String,questions: ArrayList<Question>)  {
        val filePath = "/data/data/com.oyz.mikesun/files" + File.separator + "$title.txt"
        var gson = Gson()

        File(filePath).forEachLine(){
            var question1 = gson.fromJson(it, Question::class.java)
            questions.add(question1)
        }


    }

    private fun getProblemAndAnswer(index: Int,questions: ArrayList<Question>){
        val q= questions[index]
        problem.text = q.problem
        Myanswer.text = q.Myanswer
        Tanswer.text = q.Tanswer.split(".")[0]
    }
}