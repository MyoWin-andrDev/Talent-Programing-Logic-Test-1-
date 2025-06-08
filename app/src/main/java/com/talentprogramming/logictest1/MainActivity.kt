package com.talentprogramming.logictest1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.talentprogramming.logictest1.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var firstOperand: BigDecimal? = null
    private var currentOperator: String? = null
    private var isNewCalculation = true
    private var calculationComplete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onNumberClicked(view: View) {
        if (calculationComplete) resetCalculator()

        val number = (view as Button).text.toString()
        val currentText = binding.etCalculate.text.toString()

        binding.etCalculate.text = when {
            isNewCalculation || currentText == "0" || currentText == "00" -> number
            else -> currentText + number
        }

        isNewCalculation = false
        calculationComplete = false
    }

    fun onOperatorClicked(view: View) {
        if (calculationComplete) calculationComplete = false

        val operator = (view as Button).text.toString()
        val currentText = binding.etCalculate.text.toString()

        if (currentText.isEmpty()) {
            showToast("Enter a number first")
            return
        }

        try {
            firstOperand = currentText.toBigDecimal()
            currentOperator = operator
            binding.tvOperation.text = currentText
            binding.tvOperator.text = operator
            binding.etCalculate.text = ""
            isNewCalculation = true
        } catch (e: NumberFormatException) {
            showError("Invalid number format")
        }
    }

    @SuppressLint("SetTextI18n")
    fun onEqualClicked(view: View) {
        if (calculationComplete) return

        val secondOperandText = binding.etCalculate.text.toString()

        if (secondOperandText.isEmpty()) {
            showToast("Enter a second number")
            return
        }

        if (firstOperand == null || currentOperator == null) {
            showToast("Start a calculation first")
            return
        }

        try {
            val secondOperand = secondOperandText.toBigDecimal()
            val result = calculate(firstOperand!!, secondOperand, currentOperator!!)

            binding.tvOperation.text = "${formatOperand(firstOperand!!)} $currentOperator ${formatOperand(secondOperand)}"
            binding.etCalculate.text = formatResult(result)
            binding.tvOperator.text = "="

            calculationComplete = true
            isNewCalculation = true
        } catch (e: Exception) {
            showError(e.message ?: "Calculation error")
        }
    }

    fun onClearClicked(view: View) = resetCalculator()

    fun onBackSpaceClicked(view: View) {
        if (calculationComplete) return

        val currentText = binding.etCalculate.text.toString()
        val updatedText = currentText.dropLast(1)

        binding.etCalculate.text = if (updatedText.isEmpty()) {
            isNewCalculation = true
            "0"
        } else {
            updatedText
        }
    }

    fun onDecimalClicked(view: View) {
        if (calculationComplete) resetCalculator()

        val currentText = binding.etCalculate.text.toString()
        if (isNewCalculation) {
            binding.etCalculate.text = "0."
            isNewCalculation = false
        } else if (!currentText.contains(".")) {
            binding.etCalculate.append(".")
        }
    }

    fun onPercentageClicked(view: View) {
        if (calculationComplete) resetCalculator()

        val currentText = binding.etCalculate.text.toString()
        if (currentText.isNotEmpty()) {
            try {
                val value = currentText.toBigDecimal()
                val percentage = value.divide(BigDecimal(100))
                binding.etCalculate.text = formatResult(percentage)
                isNewCalculation = true
            } catch (e: NumberFormatException) {
                showError("Invalid number format")
            }
        }
    }

    // ------------------------
    // Helper methods
    // ------------------------

    private fun calculate(first: BigDecimal, second: BigDecimal, operator: String): BigDecimal {
        return when (operator) {
            "+" -> first + second
            "-" -> first - second
            "×" -> first * second
            "÷" -> {
                if (second == BigDecimal.ZERO) throw ArithmeticException("Cannot divide by zero")
                first.divide(second, 10, RoundingMode.HALF_UP)
            }
            else -> throw ArithmeticException("Unknown operator")
        }
    }

    private fun formatResult(result: BigDecimal): String =
        result.stripTrailingZeros().toPlainString()

    private fun formatOperand(operand: BigDecimal): String =
        if (operand.scale() <= 0) operand.toLong().toString()
        else operand.stripTrailingZeros().toPlainString()

    private fun resetCalculator() {
        firstOperand = null
        currentOperator = null
        binding.etCalculate.text = ""
        binding.tvOperation.text = ""
        binding.tvOperator.text = ""
        isNewCalculation = true
        calculationComplete = false
    }

    private fun showError(message: String) {
        showToast(message)
        resetCalculator()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
