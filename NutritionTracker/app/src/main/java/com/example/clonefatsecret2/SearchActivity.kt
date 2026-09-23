package com.example.clonefatsecret2

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var dbHelper: DBWork
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var productList: List<Item> // Список продуктов

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val searchView: SearchView = findViewById(R.id.searchView)
        val itemsList: ListView = findViewById(R.id.itemsList)

        // Инициализация базы данных
        dbHelper = DBWork(this, null)

        // Добавляем данные в базу (если нужно)
        dbHelper.insertProduct("Яблоко", 52, 0, 0, 14)
        dbHelper.insertProduct("Курица", 165, 31, 3, 0)
        dbHelper.insertProduct("Рис", 130, 2, 0, 28)

        // Получаем список всех продуктов
        productList = dbHelper.searchProducts("")
        val productNames = productList.map { "${it.name}: ${it.cal} ккал/100г" }

        // Настраиваем адаптер для ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, productNames)
        itemsList.adapter = adapter

        // Устанавливаем слушатель для поиска
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchProducts(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchProducts(it) }
                return true
            }
        })

        // Слушатель для выбора продукта
        itemsList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            onProductSelected(productList[position]) // Передаем выбранный продукт
        }
    }

    // Обновление списка продуктов на основе поискового запроса
    private fun searchProducts(query: String) {
        productList = dbHelper.searchProducts(query) // Обновляем список продуктов
        val productNames = productList.map { "${it.name}: ${it.cal} ккал/100г" }
        adapter.clear()
        adapter.addAll(productNames)
        adapter.notifyDataSetChanged()
    }

    // Переход в MealActivity с передачей выбранного продукта
    private fun onProductSelected(item: Item) {
        val intent = Intent(this, MealActivity::class.java).apply {
            putExtra("selectedProduct", item)
        }
        startActivity(intent)
    }
}
