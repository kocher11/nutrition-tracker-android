package com.example.clonefatsecret2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

            supportActionBar?.hide()
            Handler().postDelayed({
                val intent = Intent(this@LoadActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)

        }
    }