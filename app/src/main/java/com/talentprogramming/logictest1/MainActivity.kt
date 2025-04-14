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
        if (calculationComplete) {
            resetCalculator()
        }

        val button = view as Button
        val number = button.text.toString()
        val currentText = binding.etCalculate.text.toString()

        if (isNewCalculation) {
            binding.etCalculate.text = number
            isNewCalculation = false
        } else {
            if (currentText == "0") {
                binding.etCalculate.text = number
            } else {
                binding.etCalculate.append(number)
            }
        }

        calculationComplete = false
    }

    fun onOperatorClicked(view: View) {
        if (calculationComplete) {
            calculationComplete = false
        }

        val button = view as Button
        val operator = button.text.toString()
        val currentText = binding.etCalculate.text.toString()

        if (currentText.isEmpty()) {
            Toast.makeText(this, "Enter a number first", Toast.LENGTH_SHORT).show()
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
        if (calculationComplete) {
            return
        }

        val secondOperandText = binding.etCalculate.text.toString()

        if (secondOperandText.isEmpty()) {
            Toast.makeText(this, "Enter a second number", Toast.LENGTH_SHORT).show()
            return
        }

        if (firstOperand == null || currentOperator == null) {
            Toast.makeText(this, "Start a calculation first", Toast.LENGTH_SHORT).show()
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
        } catch (e: ArithmeticException) {
            showError(e.message ?: "Calculation error")
        } catch (e: NumberFormatException) {
            showError("Invalid number format")
        }
    }

    private fun calculate(first: BigDecimal, second: BigDecimal, operator: String): BigDecimal {
        return when (operator) {
            "+" -> first.add(second)
            "-" -> first.subtract(second)
            "×" -> first.multiply(second)
            "÷" -> {
                if (second.compareTo(BigDecimal.ZERO) == 0) {
                    throw ArithmeticException("Cannot divide by zero")
                }
                first.divide(second, 10, RoundingMode.HALF_UP)
            }
            else -> throw ArithmeticException("Unknown operator")
        }
    }

    private fun formatResult(result: BigDecimal): String {
        // Remove trailing zeros and convert to plain string
        val stripped = result.stripTrailingZeros()
        return stripped.toPlainString()
    }

    private fun formatOperand(operand: BigDecimal): String {
        // Format operands to show decimals only when needed
        return if (operand.scale() <= 0) {
            operand.toLong().toString()
        } else {
            operand.stripTrailingZeros().toPlainString()
        }
    }

    fun onClearClicked(view: View) {
        resetCalculator()
    }

    fun onBackSpaceClicked(view: View) {
        if (calculationComplete) {
            return
        }

        val currentText = binding.etCalculate.text.toString()
        if (currentText.isNotEmpty()) {
            binding.etCalculate.text = currentText.dropLast(1)
            if (binding.etCalculate.text.isEmpty()) {
                binding.etCalculate.text = "0"
                isNewCalculation = true
            }
        }
    }

    fun onDecimalClicked(view: View) {
        if (calculationComplete) {
            resetCalculator()
        }

        val currentText = binding.etCalculate.text.toString()
        if (isNewCalculation) {
            binding.etCalculate.text = "0."
            isNewCalculation = false
        } else if (!currentText.contains(".")) {
            binding.etCalculate.append(".")
        }
    }

    fun onPercentageClicked(view: View) {
        if (calculationComplete) {
            resetCalculator()
        }

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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        resetCalculator()
    }
}