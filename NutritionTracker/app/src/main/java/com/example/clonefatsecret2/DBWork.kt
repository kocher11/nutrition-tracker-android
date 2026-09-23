package com.example.clonefatsecret2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBWork(val context: Context, val factory: SQLiteDatabase.CursorFactory?): SQLiteOpenHelper(context, "app", factory, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE User (id INT PRIMARY KEY, email VARCHAR(255), pass NVARCHAR(255))"
        db!!.execSQL(query)
        db?.execSQL("""
            CREATE TABLE Product (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name NVARCHAR(255) NOT NULL,
                cal INT NOT NULL,
                proteins INT NOT NULL,
                fats INT NOT NULL,
                carbs INT NOT NULL
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS user")
        onCreate(db)
        db?.execSQL("DROP TABLE IF EXISTS products")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put("email", user.email)
        values.put("pass", user.pass)

        val db = this.writableDatabase
        db.insert("user", null, values)

        db.close()
    }

    fun getUser(email: String, pass: String): Boolean {
        val db = this.readableDatabase

        val result = db.rawQuery("SELECT * FROM user WHERE email = '$email' AND pass = '$pass'", null)
        return result.moveToFirst()
    }

    fun insertProduct(name: String, cal: Int, proteins: Int, fats: Int, carbs: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("cal", cal)
            put("proteins", proteins)
            put("fats", fats)
            put("carbs", carbs)
        }
        db.insert("products", null, values)
        db.close()
    }

    fun searchProducts(query: String): List<Item> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM products WHERE name LIKE ?",
            arrayOf("%$query%")
        )
        val items = mutableListOf<Item>()
        if (cursor.moveToFirst()) {
            do {
                items.add(
                    Item(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("cal")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("proteins")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("fats")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("carbs"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return items
    }

}