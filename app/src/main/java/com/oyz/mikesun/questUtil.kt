package com.oyz.mikesun

import java.util.*

object questUtil {
    fun getques(total: Int, level: String): Array<Any> {
        /*
         * 参数控制难度*/
        var sdresult = 500
        var sdkuohao = 0
        var sdzifang = 0
        var sdpar = 0
        when (level) {
            "简单" -> {
                //小学一年级难度。没有括号，没有次方，结果在100以下,运算符为1到2位
                sdresult = 100
                sdkuohao = 0
                sdzifang = 0
                sdpar = 1
            }
            "普通" -> {
                //小学二年级难度。没有括号，没有次方，结果在600以下,运算符为2到3位
                sdresult = 600
                sdkuohao = 0
                sdzifang = 0
                sdpar = 2
            }
            "中等" -> {
                //小学三年级难度。有括号，没有次方，结果在800以下,运算符为2到3位
                sdresult = 800
                sdkuohao = 1
                sdzifang = 0
                sdpar = 3
            }
            "困难" -> {
                //小学四年级难度。有括号，有次方，结果在1000以下,运算符为3到4位
                sdresult = 1000
                sdkuohao = 1
                sdzifang = 1
                sdpar = 4
            }
            else -> {
                sdresult = 1000
                sdkuohao = 0
                sdzifang = 0
                sdpar = 1
            }
        }
        val simple = arrayOf("+", "-", "x", "/", "^")
        var count = 0
        var problems = ArrayList<String>()
        var pre = 0
        while (count < total) {
            //随机生成【1-100】之间的随机数
            var str: String = (Math.random() * 100 + 1) .toInt().toString()
            pre = str.toInt()
            //生成四则运算的数字个数,至少三个,最多五个
            val a = (Math.random() * 2+ sdpar).toInt()
            //运算符位置
            var p = 4
            if (sdzifang == 1) { //可以有次方
                p = 5
            }
            for (i in 0 until a ) {
                //生成四则运算的数字
                var b = (Math.random() * 100 + 1).toInt()
                //生成运算符的位置
                var c = (Math.random() * p).toInt()
                if (c == 4) {
                    b = (Math.random() * 3 + 1).toInt()
                    p = 4
                }
                if (c == 3) {
                    var ac = 5
                    while (ac != 0 && pre % ac != 0) {
                        ac += (Math.random() * 2 - 2).toInt()
                    }
                    if (ac != 0 && ac != 1) {
                        b = ac
                    } else {
                        c = 2
                    }
                }
                pre = b
                str += simple[c] + b.toString()
            }
            if (sdkuohao == 1 && Math.random() * 4 > 2) {
                val len = str.length
                val charstr = str.toCharArray()
                var right = len + 1
                run {
                    var i = 1
                    while (i < len) {
                        if (isNum(charstr[i].toString() + "") && Math.random() * 4 > 2) {
                            while (i >= 1 && isNum(charstr[i - 1].toString() + "")) {
                                i--
                            }
                            str = str.substring(0, i) + "(" + str.substring(i)
                            println("随机后$str")
                            right = i
                            break
                        }
                        i++
                    }
                }
                var countsym = 0
                for (i in right until len) {
                    if (isSymble(charstr[i])) {
                        if (countsym > Math.random() * 2 + 1) {
                            println(charstr[i])
                            str = str.substring(0, i + 1) + ")" + str.substring(i + 1)
                            break
                        }
                        countsym++
                    }
                }
                println("2随机后$str")
                if (str.contains("(") && !str.contains(")")) {
                    str = "$str)"
                }
                println("3随机后$str")
            }
            println(toPoland(toMid(str)))
            val answer = calculate(toPoland(toMid(str)))
                .toInt()
            if (answer >= 0 && answer < sdresult) {
                count++
                val trueans = (Math.random() * 4).toInt()
                val ansarr = IntArray(4)
                ansarr[trueans] = answer
                for (i in 0..3) {
                    if (i != trueans) {
                        ansarr[i] = (Math.random() * 100).toInt()
                        while (ansarr[i] == answer) {
                            ansarr[i] = (Math.random() * 100).toInt()
                        }
                    }
                }

                problems.add(str)
            }
        }
        return problems.toArray()
    }

    fun calculate(s: String): Int {
        val stack = Stack<Int>()
        var opt = '+'
        var num = 0
        for (i in 0 until s.length) {
            val ch = s[i]
            if (Character.isDigit(ch)) num = num * 10 + (ch - '0')
            if (!Character.isDigit(ch) && ch != ' ' || i == s.length - 1) {
                if (opt == '+') stack.push(num) else if (opt == '-') stack.push(-num) else if (opt == '*') stack.push(
                    stack.pop() * num
                ) else {
                    if (stack.peek() % num != 0) {
                        return -1
                    }
                    stack.push(stack.pop() / num)
                }
                num = 0
                opt = ch
            }
        }
        var res = 0
        while (!stack.isEmpty()) res += stack.pop()
        return res
    }


    fun Blank(s: String): String {
        return s.replace("\\s+".toRegex(), "")
    }

    fun toMid(s: String): List<String> {
        var s = s
        s = Blank(s)
        val list: MutableList<String> = ArrayList()
        var temp = ""
        for (i in 0 until s.length) {
//			System.out.println("charAt:"+s.charAt(i));
            if (isSymble(s[i])) {
                list.add(s[i].toString() + "")
            } else if (s[i] >= '0' && s[i] <= '9' || s[i] == '.') {
                temp = temp + s[i]
                //				System.out.println(temp);
                if (i == s.length - 1) {
                    list.add(temp)
                } else if (isSymble(s[i + 1])) {
                    list.add(temp)
                    temp = ""
                }
            }
        }
        return list
    }

    fun toPoland(s: List<String>): List<String> {
        val list: MutableList<String> = ArrayList()
        val oper = Stack<String>()
        for (string in s) {
            if (isNum(string)) {
                list.add(string)
            } else if (string == "(") {
                oper.push(string)
            } else if (string == ")") {
                while (oper.peek() != "(") {
                    list.add(oper.pop())
                }
                oper.pop()
            } else {
                while (oper.size != 0 && priority(string) <= priority(oper.peek())) {
                    list.add(oper.pop())
                }
                oper.push(string)
            }
            //			System.out.println("oper="+oper);
        }
        while (!oper.empty()) {
            list.add(oper.pop())
        }
        return list
    }

    fun isSymble(c: Char): Boolean {
        return c == '+' || c == '-' || c == 'x' || c == '/' || c == '(' || c == ')' || c == '^' || c == '*'
    }

    fun isNum(s: String): Boolean {
        return s.matches(Regex("^\\d+.?\\d*$"))
    }

    fun priority(c: String): Int {
        if (c == "+" || c == "-") {
            return 1
        }
        if (c == "x" || c == "/") {
            return 2
        }
        return if (c == "^") {
            3
        } else 0
    }

    fun calculate(s: List<String>): Double {
        var res = 0.0
        var num1 = 0.0
        var num2 = 0.0
        val s1 = Stack<String>()
        for (item in s) {
//			System.out.println("s1="+s1);
            if (isNum(item)) {
                s1.push(item)
            } else {
                num1 = s1.pop().toDouble()
                num2 = s1.pop().toDouble()
                if (item == "+") {
                    res = num1 + num2
                } else if (item == "-") {
                    res = num2 - num1
                } else if (item == "x") {
                    res = num1 * num2
                } else if (item == "/") {
                    if (num2 % num1 != 0.0) {
                        return (-1).toDouble()
                    }
                    res = num2 / num1
                } else if (item == "^") {
                    if (num1 < 0) {
                        return (-1).toDouble()
                    }
                    res = num2
                    while (num1 > 1) {
                        res *= num2
                        num1--
                    }
                } else {
                    throw RuntimeException("运算符有误")
                }
                s1.push("" + res)
            }
        }
        return s1.pop().toDouble()
    }

}