package com.example.clonefatsecret2


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        val linkToWelcome: ImageButton = findViewById(R.id.butonBack)
        val userEmail: EditText = findViewById(R.id.editTextTextMail)
        val userPass: EditText = findViewById(R.id.editTextTextPassword)
        val button: Button = findViewById(R.id.buttonAuth)

        linkToWelcome.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val email = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (email == "" || pass == "")
                Toast.makeText(this, "Пожалуйста заполните все поля", Toast.LENGTH_LONG).show()
            else {

                val db = DBWork(this, null)
                val isAuth = db.getUser(email, pass)

                if (isAuth) {
                    Toast.makeText(this, "Пользователь добавлен", Toast.LENGTH_LONG).show()
                    userEmail.text.clear()
                    userPass.text.clear()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)


                } else
                    Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_LONG).show()
            }


        }
    }
}