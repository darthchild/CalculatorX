package com.example.calculatorx

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    private var tvInput: TextView? = null
    //FLAGS
    var lastNumeric = false
    var lastDecimal = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvInput = findViewById(R.id.tvInput)
    }

    fun onClear(view: View){
        tvInput?.text = ""
    }

    fun onDecimal(view: View){
        // if lastNumeric is TRUE and lastDecimal is NOT TRUE
        if(lastNumeric  && !lastDecimal){
            tvInput?.append(".")
            lastNumeric = false
            lastDecimal = true
        }
    }

    fun onDigit(view: View){
        if(tvInput?.text == "0")
            onClear(view)
        tvInput?.append( (view as Button).text)
        lastNumeric = true
        lastDecimal = false
        //we want to record what we pressed last
    }

    fun onOperator(view: View) {
        // checks if the Input textView is empty or not
        // (aka Safe Call) as its a nullable so we have to check first
        tvInput?.text?.let {

            if (lastNumeric && !isOperatorAdded(it.toString())) {
                tvInput?.append((view as Button).text)
                lastNumeric = false
                lastDecimal = false
            }
        }
    }

    fun onEqual(view: View){
        if(lastNumeric){
            // get the text in calc's input field in a string var
            var tvValue = tvInput?.text.toString()
            var prefix = ""

            try{
                //will remove the 0th element of string("-") so it doesn't split it early
                if(tvValue.startsWith("-")){
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }

                // CALCULATIONS
                if(tvValue.contains("-"))
                {
                    val arr = calculate("-", prefix, tvValue)
                    tvInput?.text = clean( (arr[0] - arr[1]).toString() )
                }
                else if(tvValue.contains("+"))
                {
                    val arr = calculate("+", prefix, tvValue)
                    tvInput?.text = clean( (arr[0] + arr[1]).toString() )
                }
                else if(tvValue.contains("×"))
                {
                    val arr = calculate("×", prefix, tvValue)
                    tvInput?.text = clean( (arr[0] * arr[1]).toString() )
                }
                else if(tvValue.contains("÷"))
                {
                    val arr = calculate("÷", prefix, tvValue)
                    tvInput?.text = clean( (arr[0] / arr[1]).toString() )
                }


            }catch(e: ArithmeticException){
                e.printStackTrace() //will print on Logcat
            }
        }
    }

    private fun calculate(symbol: String, prefix: String, tvValue: String): Array<Double> {
        val splitValue = tvValue.split(symbol) // returns an array
        var first = splitValue[0]
        var sec = splitValue[1]

        if (prefix.isNotEmpty()) {
            first = prefix + first //these are strings
        }
        return arrayOf<Double>(first.toDouble(), sec.toDouble())
    }

    private fun clean(result: String) : String{
        // REMOVES the .0 in every number
        var newResult = result
        if(result.contains(".0"))
            newResult = result.substring(0,result.length-2)

        return newResult
    }

    private fun isOperatorAdded(value: String): Boolean{
        return if(value.startsWith("-")){
             false
        } else {
            value.contains("/") || value.contains("+")
                    || value.contains("*")
        }
    }

}

