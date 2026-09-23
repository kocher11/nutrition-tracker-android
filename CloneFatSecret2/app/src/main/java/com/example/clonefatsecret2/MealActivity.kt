package com.example.clonefatsecret2

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MealActivity : AppCompatActivity() {

    private val selectedProducts = mutableListOf<Item>() // Список выбранных продуктов
    private lateinit var adapter: ArrayAdapter<Item> // Адаптер для ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        val btnBack: ImageButton = findViewById(R.id.imageButtonBackMain)
        val btnAdd: ImageButton = findViewById(R.id.imageButtonAddProduct)
        val listProduct: ListView = findViewById(R.id.listProduct)

        // Настройка адаптера
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, // Используем стандартный макет
            selectedProducts
        )
        listProduct.adapter = adapter

        // Получение переданного продукта из Intent
        val product = intent.getSerializableExtra("selectedProduct") as? Item
        product?.let {
            selectedProducts.add(it)
            adapter.notifyDataSetChanged()
            updateTotals() // Обновляем итоги
        }

        // Обработчик кнопки "Назад"
        btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Обработчик кнопки "Добавить продукт"
        btnAdd.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    // Функция обновления итоговых значений
    private fun updateTotals() {
        val totalCal = selectedProducts.sumOf { it.cal }
        val totalProteins = selectedProducts.sumOf { it.proteins }
        val totalFats = selectedProducts.sumOf { it.fats }
        val totalCarbs = selectedProducts.sumOf { it.carb }

        findViewById<TextView>(R.id.TextViewCalMeal).text = "Калории: $totalCal"
        findViewById<TextView>(R.id.TextViewProtMeal).text = "Белки: $totalProteins г"
        findViewById<TextView>(R.id.TextViewFatMeal).text = "Жиры: $totalFats г"
        findViewById<TextView>(R.id.TextViewCarbMeal).text = "Углеводы: $totalCarbs г"
    }
}
