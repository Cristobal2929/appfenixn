package com.fenixn.app

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.fenixn.app.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var operand1: Double? = null
    private var pendingOperator: Char? = null
    private var isNewOperand: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize display
        binding.txtDisplay.text = "0"

        // Digit buttons
        val digitButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6, binding.btn7,
            binding.btn8, binding.btn9
        )
        digitButtons.forEach { button ->
            button.setOnClickListener { v ->
                val digit = (v as MaterialButton).text[0]
                onDigitPressed(digit)
            }
        }

        // Operator buttons
        binding.btnAdd.setOnClickListener { onOperatorPressed('+') }
        binding.btnSubtract.setOnClickListener { onOperatorPressed('-') }
        binding.btnMultiply.setOnClickListener { onOperatorPressed('*') }
        binding.btnDivide.setOnClickListener { onOperatorPressed('/') }

        // Clear and Equals
        binding.btnClear.setOnClickListener { clearAll() }
        binding.btnEquals.setOnClickListener { calculateResult() }
    }

    private fun onDigitPressed(digit: Char) {
        if (isNewOperand) {
            binding.txtDisplay.text = digit.toString()
            isNewOperand = false
        } else {
            // Avoid leading zeros
            if (binding.txtDisplay.text == "0") {
                binding.txtDisplay.text = digit.toString()
            } else {
                binding.txtDisplay.append(digit.toString())
            }
        }
    }

    private fun onOperatorPressed(operator: Char) {
        val currentValue = binding.txtDisplay.text.toString().toDoubleOrNull()
        if (currentValue != null) {
            if (operand1 == null) {
                operand1 = currentValue
            } else if (pendingOperator != null) {
                operand1 = performOperation(operand1!!, currentValue, pendingOperator!!)
            }
        }
        pendingOperator = operator
        isNewOperand = true
    }

    private fun calculateResult() {
        val currentValue = binding.txtDisplay.text.toString().toDoubleOrNull()
        if (operand1 != null && pendingOperator != null && currentValue != null) {
            val result = performOperation(operand1!!, currentValue, pendingOperator!!)
            binding.txtDisplay.text = result.toString()
            operand1 = null
            pendingOperator = null
            isNewOperand = true
        }
    }

    private fun clearAll() {
        binding.txtDisplay.text = "0"
        operand1 = null
        pendingOperator = null
        isNewOperand = true
    }

    private fun performOperation(a: Double, b: Double, op: Char): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b != 0.0) a / b else Double.NaN
            else -> Double.NaN
        }
    }
}