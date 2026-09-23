package com.example.clonefatsecret2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val userLogin: EditText = findViewById(R.id.editTextTextName)
        val userEmail: EditText = findViewById(R.id.editTextTextMail)
        val userPass: EditText = findViewById(R.id.editTextTextPassword1)
        val userPass2: EditText= findViewById(R.id.editTextTextPasswordRepeat)
        val buttonReg: Button = findViewById(R.id.buttonReg)
        val buttonBack: ImageButton = findViewById(R.id.imageButtonBack)

        buttonBack.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }

        buttonReg.setOnClickListener{
            val login = userLogin.text.toString().trim()
            val email = userEmail.text.toString().trim()
            val pass = userPass.text.toString().trim()
            val pass2 = userPass2.text.toString().trim()


            if(login == "" || email == "" || pass == "")
                Toast.makeText(this, "Пожалуйста заполните все поля", Toast.LENGTH_LONG).show()
            else if(pass != pass2)
                Toast.makeText(this, "В поле для повторного ввода пароля допущена ошибка", Toast.LENGTH_LONG).show()
            else {
                val user = User(email, pass)

                val db = DBWork(this, null)
                db.addUser(user)

                Toast.makeText(this, "Пользователь $login добавлен", Toast.LENGTH_LONG).show()

                userLogin.text.clear()
                userEmail.text.clear()
                userPass.text.clear()

                val intent = Intent(this, SurveyActivity::class.java)
                startActivity(intent)


            }
        }
    }
}