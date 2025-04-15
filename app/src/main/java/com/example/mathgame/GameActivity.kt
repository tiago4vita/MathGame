package com.example.mathgame

import Score
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Layout
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.delay
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private var questionIndex = 1
    private var rightAnswer = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        getQuestion()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun generatePositiveExpression(n1TextView: TextView, n2TextView: TextView, operatorTextView: TextView) {
        // Geração de sinal aleatório (+ ou -)
        val sign = if (Random.nextBoolean()) "+" else "-"

        // Geração de números e cálculo do resultado, garantindo resultado não negativo
        val num1: Int
        val num2: Int
        val result: Int

        if (sign == "-") {
            // Geração de números onde num2 <= num1 para evitar resultado negativo
            num1 = Random.nextInt(0, 99)
            num2 = Random.nextInt(0, num1 + 1) // Garante que num2 seja menor ou igual a num1
            result = num1 - num2
        } else {
            num1 = Random.nextInt(0, 99)
            num2 = Random.nextInt(0, 99)
            result = num1 + num2
        }
        n1TextView.text = num1.toString()
        n2TextView.text = num2.toString()
        operatorTextView.text = sign
        rightAnswer = result
    }

    fun getQuestion() {
        val view = findViewById<View>(R.id.main)
        view.setBackgroundColor(Color.WHITE)
        val title = findViewById<TextView>(R.id.textViewQuestionIndex)
        title.text = "Pergunta $questionIndex"
        val t1 = findViewById<TextView>(R.id.textN1)
        val t2 = findViewById<TextView>(R.id.textN2)
        val op = findViewById<TextView>(R.id.textOperator)
        generatePositiveExpression(t1, t2, op)
    }

    fun getGuess(view: View) {
        val guess = findViewById<EditText>(R.id.inputGuess).text.toString().trim()
        if (guess.isBlank()) {
            Toast.makeText(this, "Preencha o campo com o número!", Toast.LENGTH_SHORT).show()
            return
        }
        val numberGuess = guess.toInt()
        println("number guess is $numberGuess")
        if (numberGuess == rightAnswer) {
            paintBackground(true)
            score += 20
        }
        else paintBackground(false)
        questionIndex++
        println("question index is $questionIndex")
        if (questionIndex < 6) {
            getQuestion()
        } else goToEnd()
    }

    fun paintBackground(isCorrect: Boolean) {
        val view = findViewById<View>(R.id.main)
        if (isCorrect) {
            view.setBackgroundColor(Color.GREEN)
            Handler(Looper.getMainLooper()).postDelayed({
                view.setBackgroundColor(Color.WHITE)
            }, 20000)
        }
        else {
            view.setBackgroundColor(Color.RED)
            Handler(Looper.getMainLooper()).postDelayed({
                view.setBackgroundColor(Color.WHITE)
            }, 20000)
        }
    }

    fun goToEnd() {
        val intent = Intent(this, activityEnd::class.java)
        val scoreToEnd = Score(score)
        intent.putExtra("score", scoreToEnd)
        startActivity(intent)
    }
}