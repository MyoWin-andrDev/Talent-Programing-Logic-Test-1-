package com.talentprogramming.logictest1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.talentprogramming.logictest1.databinding.ActivityMainBinding

@Suppress("CAST_NEVER_SUCCEEDS", "IMPLICIT_CAST_TO_ANY")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var firstValueText : String = ""
    private var result : Number? = null
    private var lastOperator: String? = null
    private var operator : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onNumberClicked(view: View) {
        if(lastOperator == "="){
            clearCalculation()
        }
        val btnNumber = view as Button
        val etCalculate = binding.etCalculate.text.toString()
        if(etCalculate == "0" || etCalculate == "00"){
            binding.etCalculate.text = "0"
        }
        else{
            binding.etCalculate.append(btnNumber.text.toString())
        }
    }
    fun onOperatorClicked(view: View) {
        val btnOperator = view as Button
        operator = btnOperator.text.toString()
        firstValueText = binding.etCalculate.text.toString()
        if(firstValueText == ""){
            Toast.makeText(this@MainActivity, "Enter a number first", Toast.LENGTH_SHORT).show()
        }
        else{
            binding.tvOperation.text = firstValueText
            binding.tvOperator.text = operator
            binding.etCalculate.text = ""
        }
    }
    @SuppressLint("SetTextI18n")
    fun onEqualClicked(view : View) {
        Log.d("latest Operator", lastOperator.toString())
        val secondValueText = binding.etCalculate.text.toString()
        lastOperator = binding.tvOperator.text.toString()
        Log.d("latest Operator After", lastOperator.toString())
        if (secondValueText == "") {
            Toast.makeText(this@MainActivity, "Please enter a second number", Toast.LENGTH_SHORT)
                .show()
        }
        else if(firstValueText == "" && secondValueText == ""){
            Toast.makeText(this@MainActivity, "Please enter a number first", Toast.LENGTH_SHORT)
                .show()
        }
        else if(lastOperator != "="  && binding.etCalculate.text.toString() != result.toString()){
            try{
                val firstValue = firstValueText.toDouble()
                val secondValue = secondValueText.toDouble()
                result = when (operator) {
                    "+" -> firstValue + secondValue
                    "-" -> firstValue - secondValue
                    "×" -> firstValue * secondValue
                    "÷" -> ((if(secondValue != 0.0 ){
                        firstValue / secondValue
                    } else{
                        Toast.makeText(this@MainActivity, "Cannot divided by 0", Toast.LENGTH_LONG).show()
                    }) as Number)
                    else -> {
                        Toast.makeText(this@MainActivity, "Calculation Error", Toast.LENGTH_SHORT)
                            .show() as Number
                    }
                }
                binding.tvOperation.text = "$firstValueText $operator $secondValueText"
                binding.etCalculate.text = (if(result!!.toDouble() % 1.0 == 0.0)  result!!.toInt() else result).toString()
                binding.tvOperator.text = "="
            }
            catch (e : Exception){
                Log.d("Error", e.message.toString())
            }
        }

    }
    fun onClearClicked(view: View) {
        firstValueText = ""
        binding.etCalculate.text = ""
        binding.tvOperation.text = ""
        binding.tvOperator.text = ""
        result = null
    }
    private fun clearCalculation(){
        Log.d("Latest Operation", lastOperator.toString())
        firstValueText = ""
        binding.etCalculate.text = ""
        binding.tvOperation.text = ""
        binding.tvOperator.text = ""
        result = null
        lastOperator = null

    }
    fun onBackSpaceClicked(view: View) {
        val etValue = binding.etCalculate.text.toString()
        if(etValue != ""){
            val etNewValue = etValue.substring(0, etValue.length - 1)
            binding.etCalculate.text = etNewValue
        }
        else if(lastOperator == "=" || result != null) {
            clearCalculation()
        }
        else{
            binding.etCalculate.text = "0"
        }

    }

    fun onPercentageClicked(view: View) {}
    fun onDecimalClicked(view: View) {
        val currentValue = binding.etCalculate.text.toString()
        if(!currentValue.contains(".")){
            if(currentValue.isEmpty()){
                binding.etCalculate.text = "0."
            }
            else{
                binding.etCalculate.append(".")
            }
        }
    }
}