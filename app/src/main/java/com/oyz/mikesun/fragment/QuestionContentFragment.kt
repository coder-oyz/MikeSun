package com.oyz.mikesun.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.oyz.mikesun.R
import kotlinx.android.synthetic.main.question_content_frag.*

class QuestionContentFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.question_content_frag, container, false)
    }

    //刷新
    fun refresh(title: String){
        //设为可见
        contentLayout.visibility = View.VISIBLE
        questionTitle.text = title  //刷新的标题

    }

}