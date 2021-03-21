package com.oyz.mikesun

import java.io.File

object a {
    var count = 0
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
                    count++
                }
            }
        }
        // 返回.txt文件个数
        return count
    }
}