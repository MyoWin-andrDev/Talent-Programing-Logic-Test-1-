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
    private var firstValue : Double? = null
    private var operator : String? = null
    private var isNewCalculation : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun onNumberClicked(view: View) {
        isNewCalculation = renewCalculation()
        val btnNumber = view as Button
        val etCalculate = binding.etCalculate.text.toString()
        if(etCalculate == "0" || etCalculate == "00"){
            binding.etCalculate.setText("0")
        }
        else{
            binding.etCalculate.append(btnNumber.text.toString())
        }
    }
    fun onOperatorClicked(view: View) {
        val btnOperator = view as Button
        operator = btnOperator.text.toString()
        val value = binding.etCalculate.text.toString()
        if(value.equals("")){
            Toast.makeText(this@MainActivity, "Enter a number first", Toast.LENGTH_SHORT).show()
        }
        firstValue = value.toDouble()
        binding.tvOperation.text = firstValue.toString()
        binding.tvOperator.text = operator
        binding.etCalculate.text = ""
    }
    @SuppressLint("SetTextI18n")
    fun onEqualClicked(view: View) {
        val secondValueText = binding.etCalculate.text.toString()
        try {
            if (secondValueText.equals("")) {
                Toast.makeText(this@MainActivity, "Please select a second number", Toast.LENGTH_SHORT)
                    .show()
            }
            val secondValue = secondValueText.toDouble()
            val result = when (operator) {
                "+" -> firstValue!! + secondValue
                "-" -> firstValue!! - secondValue
                "×" -> firstValue!! * secondValue
                "%" -> firstValue!! % secondValue
                "÷" -> if (secondValue != 0.0) firstValue!! / secondValue else "Cannot Divided By 0"
                else -> {
                    Toast.makeText(this@MainActivity, "Calculation Error 5", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            binding.tvOperation.text = "$firstValue $operator $secondValue"
            binding.etCalculate.text = result.toString()
            binding.tvOperator.text = "="
            isNewCalculation = true
        }catch (e : Exception){
            Log.d("Error", e.message.toString())
        }
    }
    fun onClearClicked(view: View) {
        binding.etCalculate.text = ""
        binding.tvOperation.text = null
        binding.tvOperator.text = null
        isNewCalculation = false

    }
    fun onBackSpaceClicked(view: View) {
        val etValue = binding.etCalculate.text.toString()
        if(!etValue.equals("")){
            val etNewValue = etValue.substring(0, etValue.length - 1)
            binding.etCalculate.text = etNewValue
        }
        else{
            binding.etCalculate.setText("0")
        }

    }

    private fun renewCalculation() : Boolean{
        if(isNewCalculation){
            onClearClicked(binding.btClear)
        }
        return false
    }

}