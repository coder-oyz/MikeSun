package com.oyz.mikesun.fragment

import android.content.Context
import android.os.Bundle
import android.os.FileUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.oyz.mikesun.QuestionContentActivity
import com.oyz.mikesun.R
import com.oyz.mikesun.a
import com.oyz.mikesun.a.getTxtFilesCount

import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.question_title_frag.*
import java.io.File
import java.lang.StringBuilder

class QuestionTitleFragment: Fragment() {
    private var isTwoPane = false
    private var count1=0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.question_title_frag, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        isTwoPane = activity?.findViewById<View>(R.id.questionContentLayout) != null


        val layoutManager = LinearLayoutManager(activity)
        questionTitleRecyclerView.layoutManager = layoutManager
        val adapter = NewsAdapter(getNews())
        questionTitleRecyclerView.adapter = adapter
    }

    fun getTxtFilesCount(srcFile: File?): Int {
        // 判断传入的文件是不是为空
        if (srcFile == null) {
            throw NullPointerException()
        }
        // 把所有目录、文件放入数组
        val files: Array<File> = srcFile.listFiles()
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



    private fun getNews(): List<String> {

        val filePath = "/data/data/com.oyz.mikesun/files"


        val count:Int = getTxtFilesCount(File(filePath))//覆盖原先的文本内容



        val prefs = activity?.getSharedPreferences("data", AppCompatActivity.MODE_PRIVATE)
        //记录是第几次做题
        //val code: Int? = prefs?.getInt("code", 0)
        if(count == 0){
            Toast.makeText(context,"暂无答题记录",Toast.LENGTH_SHORT).show()
        }
        val newsList = ArrayList<String>()
        for (i in 1..count!!) {
            //获取答题记录数
            val news = "第 $i 次答题记录"
            newsList.add(news)
        }
        return newsList
    }



    inner class NewsAdapter(val newsList: List<String>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val newsTitle: TextView = view.findViewById(R.id.questionTitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.question_item, parent, false)
            val holder = ViewHolder(view)
            holder.itemView.setOnClickListener {
                val news = newsList[holder.adapterPosition]
                if (isTwoPane) {
                    // 如果是双页模式，则刷新NewsContentFragment中的内容
                    val fragment = questionContentFrag as QuestionContentFragment
                    fragment.refresh(news) //刷新NewsContentFragment界面
                } else {
                    // 如果是单页模式，则直接启动NewsContentActivity
                    QuestionContentActivity.actionStart(parent.context, news);
                }
            }
            return holder
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val news = newsList[position]
            holder.newsTitle.text = news
        }

        override fun getItemCount() = newsList.size

    }
}