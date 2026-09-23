package com.example.clonefatsecret2

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Получение данных из Intent
        val ansKM = intent.getIntArrayExtra("KEY") ?: intArrayOf()
        if (ansKM.size < 6 || ansKM.any { it < 0 }) {
            return // Прерываем выполнение, если данные некорректны
        }

        val result = calculateNutrition(ansKM)

        // Инициализация UI-элементов
        val textViewCalories = findViewById<TextView>(R.id.textViewCalories)
        val progressBarCalories = findViewById<ProgressBar>(R.id.progressBarCalories)

        val textViewProteins = findViewById<TextView>(R.id.textViewProteins)
        val progressBarProteins = findViewById<ProgressBar>(R.id.progressBarProteins)

        val textViewFats = findViewById<TextView>(R.id.textViewFats)
        val progressBarFats = findViewById<ProgressBar>(R.id.progressBarFats)

        val textViewCarbs = findViewById<TextView>(R.id.textViewCarbs)
        val progressBarCarbs = findViewById<ProgressBar>(R.id.progressBarCarbs)

        // Значения норм из расчёта
        val normCalories = result["Калории"]?.toInt() ?: 0
        val normProteins = result["Белки (г)"]?.toInt() ?: 0
        val normFats = result["Жиры (г)"]?.toInt() ?: 0
        val normCarbs = result["Углеводы (г)"]?.toInt() ?: 0

        // Текущие значения (например, загружаемые из БД)
        val currentCalories = 0
        val currentProteins = 0
        val currentFats = 0
        val currentCarbs = 0

        // Обновляем UI
        updateUI(currentCalories, normCalories, textViewCalories, progressBarCalories, "Калории")
        updateUI(currentProteins, normProteins, textViewProteins, progressBarProteins, "Белки")
        updateUI(currentFats, normFats, textViewFats, progressBarFats, "Жиры")
        updateUI(currentCarbs, normCarbs, textViewCarbs, progressBarCarbs, "Углеводы")

        // Настройка полноэкранного режима для устройств Android KitKat и выше
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        // Настройка кастомного диалога
        val btnShow: LinearLayout = findViewById(R.id.show_dialog)
        btnShow.setOnClickListener {
            val dialogBinding = layoutInflater.inflate(R.layout.my_custtom_dialog, null)
            val myDialog = Dialog(this)
            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()

            val btnCancel = dialogBinding.findViewById<TextView>(R.id.TextViewCancel)
            btnCancel.setOnClickListener {
                myDialog.dismiss()
            }

            val btnOk = dialogBinding.findViewById<TextView>(R.id.TextViewOk)
            btnOk.setOnClickListener {
                myDialog.dismiss()
            }
        }

        // Переход к экрану добавления продукта
        val btnAdd: ImageView = findViewById(R.id.imageViewAdd)
        btnAdd.setOnClickListener {
            val intent = Intent(this, MealActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    // Обновление UI прогресса
    private fun updateUI(
        current: Int, norm: Int,
        textView: TextView,
        progressBar: ProgressBar,
        label: String
    ) {
        val safeNorm = if (norm > 0) norm else 1 // Избегаем деления на ноль
        textView.text = "$label: $current / $safeNorm"
        progressBar.max = safeNorm
        progressBar.progress = current.coerceAtMost(safeNorm)
    }

    // Расчёт норм питания
    private fun calculateNutrition(ansKM: IntArray): Map<String, Double> {
        if (ansKM.size < 6) return emptyMap()

        val goal = ansKM[0]
        val gender = ansKM[1]
        val activityLevel = ansKM[2]
        val height = ansKM[3].toDouble()
        val weight = ansKM[4].toDouble()
        val age = ansKM[5].toDouble()

        // Проверяем корректность входных данных
        if (height <= 0 || weight <= 0 || age <= 0 || activityLevel !in 0..3) {
            return emptyMap()
        }

        // Расчёт BMR
        val bmr = if (gender == 0) {
            (10 * weight) + (6.25 * height) - (5 * age) + 5
        } else {
            (10 * weight) + (6.25 * height) - (5 * age) - 161
        }

        // Множитель активности
        val activityMultiplier = when (activityLevel) {
            0 -> 1.25
            1 -> 1.375
            2 -> 1.55
            3 -> 1.725
            else -> 1.0
        }

        // Целевые калории
        val calorieGoal = when (goal) {
            0 -> bmr * activityMultiplier * 0.85 // Похудение
            1 -> bmr * activityMultiplier       // Поддержание веса
            2 -> bmr * activityMultiplier * 1.15 // Набор веса
            else -> bmr * activityMultiplier
        }

        // Расчёт макронутриентов
        val proteins = calorieGoal * 0.3 / 4
        val fats = calorieGoal * 0.25 / 9
        val carbs = calorieGoal * 0.45 / 4

        return mapOf(
            "Калории" to calorieGoal,
            "Белки (г)" to proteins,
            "Жиры (г)" to fats,
            "Углеводы (г)" to carbs
        )
    }
}
